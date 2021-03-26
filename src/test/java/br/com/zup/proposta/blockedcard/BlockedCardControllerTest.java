package br.com.zup.proposta.blockedcard;

import br.com.zup.proposta.card.AllCards;
import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.card.CardVerificationClient;
import br.com.zup.proposta.proposal.Address;
import br.com.zup.proposta.proposal.Proposal;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BlockedCardControllerTest {

    Proposal proposal;
    Card card;

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

    @BeforeEach
    void setup(){
        proposal = new Proposal("70810536455", "email@email.com.br", "name", BigDecimal.valueOf(1000),new Address("rua", 222, "45080"));
        card = new Card("123321", "holder", LocalDateTime.now(), BigDecimal.valueOf(10000), proposal);
    }

    @Test
    @WithMockUser
    @DisplayName("Should create blocked card and return 200")
    void create_blocked_card() throws Exception {

        Mockito.when(allCardsMock.findByExternalCardId(card.getExternalCardId())).thenReturn(Optional.of(card));

        Map<String, String> response = new HashMap<>();
        Mockito.when(cardVerificationClient.lock("1")).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/lock/cards/"+card.getExternalCardId())
                .header("User-Agent","teste"))
                .andExpect(status().isOk());

        Optional<BlockedCard> blockedCard = allBlockedCards.findById(1L);

        Assertions.assertEquals(true, card.isBlocked());
        Assertions.assertEquals(blockedCard.get().getExternaId(), card.getExternalCardId());
    }

    @Test
    @WithMockUser
    @DisplayName("Should not create blocked card and return 404")
    void create_blocked_card_invalid_id() throws Exception {

        Map<String, String> response = new HashMap<>();
        Mockito.when(cardVerificationClient.lock(card.getExternalCardId())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/lock/cards/"+Long.MAX_VALUE)
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

        Mockito.when(allCardsMock.findByExternalCardId(card.getExternalCardId())).thenReturn(Optional.of(card));
        Mockito.when(cardVerificationClient.lock(card.getExternalCardId())).thenThrow(FeignException.UnprocessableEntity.class);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/lock/cards/"+card.getExternalCardId())
                .header("User-Agent","teste"))
                .andExpect(status().isUnprocessableEntity());

        Iterable<BlockedCard> all = allBlockedCards.findAll();
        List<BlockedCard> collect = StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
        Assertions.assertEquals(0, collect.size());

        Card persistedCard = allCards.findByExternalCardId(card.getExternalCardId()).get();
        Assertions.assertEquals(persistedCard.isBlocked(), false);

    }

    @Test
    @WithMockUser
    @DisplayName("Should not block when receiving 500 external API")
    void block_card_server_error() throws Exception {

        Mockito.when(allCardsMock.findByExternalCardId(card.getExternalCardId())).thenReturn(Optional.of(card));
        Mockito.when(cardVerificationClient.lock(card.getExternalCardId())).thenThrow(FeignException.InternalServerError.class);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/lock/cards/"+card.getExternalCardId())
                .header("User-Agent","teste"))
                .andExpect(status().isInternalServerError());

        Optional<BlockedCard> blockedCard = allBlockedCards.findById(1L);

        Iterable<BlockedCard> all = allBlockedCards.findAll();
        List<BlockedCard> collect = StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
        Assertions.assertEquals(0, collect.size());

        Card persistedCard = allCards.findByExternalCardId(card.getExternalCardId()).get();
        Assertions.assertEquals(persistedCard.isBlocked(), false);

    }
}