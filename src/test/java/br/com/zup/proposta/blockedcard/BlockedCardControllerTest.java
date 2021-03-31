package br.com.zup.proposta.blockedcard;

import br.com.zup.proposta.card.AllCards;
import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.common.CardVerificationClient;
import br.com.zup.proposta.common.Address;
import br.com.zup.proposta.proposal.Proposal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BlockedCardControllerTest {

    Proposal proposal;
    Card card;
    NewBlockedCardRequest request;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AllCards allCardsMock;

    @Autowired
    AllCards allCards;

    @MockBean
    CardVerificationClient cardVerificationClient;

    @Autowired
    AllBlockedCards allBlockedCards;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        request = new NewBlockedCardRequest("teste");
        proposal = new Proposal("85439567062", "email@email.com.br", "name", BigDecimal.valueOf(1000),new Address("rua", 222, "45080"));
        card = new Card("123321", "holder", LocalDateTime.now(), BigDecimal.valueOf(10000), proposal);
    }

    @Test
    @WithMockUser
    @DisplayName("Should create blocked card and return 200")
    void create_blocked_card() throws Exception {

        Mockito.when(allCardsMock.findById((1L))).thenReturn(Optional.of(card));

        Map<String, String> response = new HashMap<>();
        Mockito.when(cardVerificationClient.lock(anyString(), any(NewBlockedCardRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/lock/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header("User-Agent","teste"))
                .andExpect(status().isOk());

        Assertions.assertEquals(true, card.isBlocked());

    }

    @Test
    @WithMockUser
    @DisplayName("Should not create blocked card and return 404")
    void create_blocked_card_invalid_id() throws Exception {

        Map<String, String> response = new HashMap<>();
        Mockito.when(cardVerificationClient.lock(card.getExternalCardId(), request)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/lock/cards/"+Long.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header("User-Agent","teste"))
                .andExpect(status().isNotFound());

        Iterable<BlockedCard> all = allBlockedCards.findAll();
        List<BlockedCard> collect = StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
        Assertions.assertEquals(0, collect.size());
        Assertions.assertEquals(false, card.isBlocked());


    }

    @Test
    @WithMockUser
    @DisplayName("Should not block a blocked card and return 422")
    void block_card_blocked() throws Exception {

        Mockito.when(allCardsMock.findById(1L)).thenReturn(Optional.of(card));

        Mockito.when(cardVerificationClient.lock(anyString(),any(NewBlockedCardRequest.class))).thenThrow(FeignException.UnprocessableEntity.class);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/lock/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header("User-Agent","teste"))
                .andExpect(status().isUnprocessableEntity());

        Assertions.assertEquals(false, card.isBlocked());
        Assertions.assertEquals(0, allBlockedCards.count());

    }

    @Test
    @WithMockUser
    @DisplayName("Should not block when receiving 500 external API")
    void block_card_server_error() throws Exception {

        Mockito.when(allCardsMock.findById(1L)).thenReturn(Optional.of(card));
        Mockito.when(cardVerificationClient.lock(anyString(),any(NewBlockedCardRequest.class))).thenThrow(FeignException.InternalServerError.class);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/lock/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header("User-Agent","teste"))
                .andExpect(status().isInternalServerError());

        Assertions.assertEquals(0, allBlockedCards.count());

    }


    public String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}