package br.com.zup.proposta.travelnotice;

import br.com.zup.proposta.card.AllCards;
import br.com.zup.proposta.card.Card;
import br.com.zup.proposta.common.AddressRequest;
import br.com.zup.proposta.common.CardVerificationClient;
import br.com.zup.proposta.proposal.Proposal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NewTravelNoticeControllerTest {

    Card card;
    Proposal proposal;
    AddressRequest addressRequest;
    final String URL_TRAVEL_NOTICE_CARD_ID_1 = "/api/travel/notice/1";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonMapper;

    @MockBean
    AllCards allCards;

    @Autowired
    AllTravelNotices allTravelNotices;

    @PersistenceContext
    EntityManager em;

    @MockBean
    CardVerificationClient cardVerificationClient;

    @BeforeEach
    void setup(){
        addressRequest = new AddressRequest("rua tal", 222, "45080");
        proposal = new Proposal("854.395.670-62", "email@email.com.br", "name", BigDecimal.valueOf(1000), addressRequest.toAdress());
        card = new Card("123321", "holder", LocalDateTime.now(), BigDecimal.valueOf(10000), proposal);
        em.persist(proposal);
        em.persist(card);
    }

    @Test
    @WithMockUser
    @DisplayName("Should create new travel notice and return 200")
    void create_new_travel_notice() throws Exception{

        when(allCards.findById(1L)).thenReturn(Optional.of(card));
        Map<String, String> response = new HashMap<>();
        NewTravelNoticeRequest request = new NewTravelNoticeRequest(addressRequest, LocalDate.now());
        when(cardVerificationClient.notifyTravel(anyString(), any(TravelNotifyRequest.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_TRAVEL_NOTICE_CARD_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header("User-Agent","teste"))
                .andExpect(status().isOk());

        Assertions.assertEquals(1L,allTravelNotices.count());
    }


    @Test
    @WithMockUser
    @DisplayName("Should not create new travel notice and return internal server error")
    void create_new_travel_notice_error_500() throws Exception{

        when(allCards.findById(1L)).thenReturn(Optional.of(card));
        NewTravelNoticeRequest request = new NewTravelNoticeRequest(addressRequest, LocalDate.now());
        when(cardVerificationClient.notifyTravel(anyString(), any(TravelNotifyRequest.class))).thenThrow(FeignException.InternalServerError.class);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_TRAVEL_NOTICE_CARD_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header("User-Agent","teste"))
                .andExpect(status().isInternalServerError());

        Assertions.assertEquals(0L,allTravelNotices.count());
    }

    @Test
    @WithMockUser
    @DisplayName("Should not create new travel notice and return 422")
    void create_new_travel_notice_error_422() throws Exception{

        when(allCards.findById(1L)).thenReturn(Optional.of(card));
        NewTravelNoticeRequest request = new NewTravelNoticeRequest(addressRequest, LocalDate.now());
        when(cardVerificationClient.notifyTravel(anyString(), any(TravelNotifyRequest.class))).thenThrow(FeignException.UnprocessableEntity.class);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_TRAVEL_NOTICE_CARD_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header("User-Agent","teste"))
                .andExpect(status().isUnprocessableEntity());

        Assertions.assertEquals(0L,allTravelNotices.count());
    }

    @Test
    @WithMockUser
    @DisplayName("Should not create travel notice without valid card")
    void create_new_travel_notice_without_card() throws Exception{
        when(allCards.findById(2L)).thenReturn(Optional.of(card));
        NewTravelNoticeRequest request = new NewTravelNoticeRequest(addressRequest, LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post(URL_TRAVEL_NOTICE_CARD_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header("User-Agent","teste"))
                .andExpect(status().isNotFound());

        Assertions.assertEquals(0L,allTravelNotices.count());
    }

    @Test
    @WithMockUser
    @DisplayName("Should not create travel notice without valid card")
    void create_new_travel_notice_without_address() throws Exception{
        NewTravelNoticeRequest request = new NewTravelNoticeRequest(addressRequest, LocalDate.now());
        when(allCards.findById(1L)).thenReturn(Optional.of(card));
        mockMvc.perform(MockMvcRequestBuilders.post(URL_TRAVEL_NOTICE_CARD_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0L,allTravelNotices.count());
    }

    @WithMockUser
    @DisplayName("Should not create travel notice with valid address street")
    @ParameterizedTest
    @NullAndEmptySource
    void create_new_travel_notice_without_address_street(String invalidStreet) throws Exception{
        AddressRequest addressWithoutStreet = new AddressRequest(invalidStreet,4, "00000");
        NewTravelNoticeRequest travelNoticeAddressTestRequest = new NewTravelNoticeRequest(addressWithoutStreet, LocalDate.now());
        when(allCards.findById(1L)).thenReturn(Optional.of(card));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_TRAVEL_NOTICE_CARD_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(travelNoticeAddressTestRequest)))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0L,allTravelNotices.count());
    }

    @WithMockUser
    @DisplayName("Should not create travel notice with valid address cep")
    @ParameterizedTest
    @NullAndEmptySource
    void create_new_travel_notice_without_address_cep(String invalidCep) throws Exception{
        AddressRequest addressWithoutStreet = new AddressRequest("rua tal",4, invalidCep);
        NewTravelNoticeRequest travelNoticeAddressTestRequest = new NewTravelNoticeRequest(addressWithoutStreet, LocalDate.now());
        when(allCards.findById(1L)).thenReturn(Optional.of(card));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_TRAVEL_NOTICE_CARD_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(travelNoticeAddressTestRequest)))
                .andExpect(status().isBadRequest());

        Assertions.assertEquals(0L,allTravelNotices.count());
    }


    private String toJson(Object obj) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(obj); }
}