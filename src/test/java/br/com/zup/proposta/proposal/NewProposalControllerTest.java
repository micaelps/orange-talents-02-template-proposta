package br.com.zup.proposta.proposal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NewProposalControllerTest {

    private final String URL_CREATE_PROPOSAL = "/api/proposals";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AllProposals allProposals;

    @MockBean
    FinancialVerification financialVerification;

    @Test
    @WithMockUser
    @DisplayName("Should create new proposal, return status 200, location based persisted proposal, eligible proposal")
    void add_new_eligible_proposal() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(1000), new AddressRequest("Baker Street", 221,"0000-000" ));

        Mockito.when(financialVerification.check(any(FinancialVerificationRequest.class))).thenReturn(new FinancialVerificationResponse(request.document, request.name, null, null));
        ResultActions post = mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isCreated());
        Proposal proposalSearched = allProposals.findByDocument(request.document).get();

        Assertions.assertEquals(ProposalStatus.ELEGIBLE,proposalSearched.getStatus());
        post.andExpect(redirectedUrlPattern("http://*/api/proposals/" + proposalSearched.getId()));
        Assertions.assertEquals(request.document, proposalSearched.getDocument());
    }

    @Test
    @WithMockUser
    @DisplayName("Should create new proposal, return status 200, location based persisted proposal, not eligible proposal")
    void add_new_not_eligible_proposal() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(1000), new AddressRequest("Baker Street", 221,"0000-000" ));

        Mockito.when(financialVerification.check(any(FinancialVerificationRequest.class))).thenThrow(FeignException.UnprocessableEntity.class);

        ResultActions post = mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isCreated());

        Proposal proposalSearched = allProposals.findByDocument(request.document).get();

        Assertions.assertEquals(ProposalStatus.NOT_ELIGIBLE,proposalSearched.getStatus());
        post.andExpect(redirectedUrlPattern("http://*/api/proposals/" + proposalSearched.getId()));
        Assertions.assertEquals(request.document, proposalSearched.getDocument());
    }


    @Test
    @WithMockUser
    @DisplayName("Should return by id created proposal, return status 200 and response based object proposal")
    void get_proposal_by_id() throws Exception {
        Proposal proposal = new Proposal("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(1000), new Address("Baker Street", 221,"0000-000" ));
        allProposals.save(proposal);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_CREATE_PROPOSAL+"/{id}", proposal.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(NewProposalResponse.of(proposal))));
    }


    @Test
    @WithMockUser
    @DisplayName("Should not return invalid id proposal, return status 404")
    void get_proposal_by_invalid_id() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_CREATE_PROPOSAL+"/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser
    @DisplayName("Should not create new proposal without document")
    void add_new_proposal_without_document() throws Exception {

        NewProposalRequest request = new NewProposalRequest(null,
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(1000), new AddressRequest("Baker Street", 221,"0000-000" ));

       mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should not create new proposal without email")
    void add_new_proposal_without_email() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                null,
                "Kyo Kusanagi",
                BigDecimal.valueOf(1000), new AddressRequest("Baker Street", 221,"0000-000" ));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should not create new proposal without name")
    void add_new_proposal_without_name() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                null,
                BigDecimal.valueOf(1000), new AddressRequest("Baker Street", 221,"0000-000" ));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should not create new proposal without salary")
    void add_new_proposal_without_salary() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                null, new AddressRequest("Baker Street", 221,"0000-000" ));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should not create new proposal with negative salary")
    void add_new_proposal_with_negative_salary() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(-10000), new AddressRequest("Baker Street", 221,"0000-000" ));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should not create new duplicated proposal")
    void add_new_proposal_duplicate() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(10000), new AddressRequest("Baker Street", 221,"0000-000" ));

        allProposals.save(request.toProposal());
        mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    @WithMockUser
    @DisplayName("Should not create new proposal without address street")
    void add_new_proposal_without_address_street() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(10000), new AddressRequest("", 221,"0000-000" ));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    @DisplayName("Should not create new proposal without address cep")
    void add_new_proposal_without_address_cep() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(10000), new AddressRequest("", 221,"" ));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser
    @DisplayName("Should not create new proposal without address number")
    void add_new_proposal_without_address_number() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(10000), new AddressRequest("Baker Street", null,"0000-000" ));

        mockMvc.perform(MockMvcRequestBuilders.post(URL_CREATE_PROPOSAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    public String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

}