package br.com.zup.proposta.newproposal;

import br.com.zup.proposta.utils.Requester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NewProposalControllerTest {

    @Autowired
    AllProposals allProposals;

    @Autowired
    Requester requester;

    @Test
    @DisplayName("Should create new proposal, return status 200 and location based persisted proposal")
    void add_new_proposal() throws Exception {
        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(1000));

        ResultActions post = requester.post("/api/proposals", request).andExpect(status().isCreated());

        Proposal proposalSearched = allProposals.findByDocument(request.document).get();
        post.andExpect(redirectedUrlPattern("http://*/api/proposals/" + proposalSearched.getId()));
        Assertions.assertEquals(request.document, proposalSearched.getDocument());
    }


    @Test
    @DisplayName("Should return by id created proposal, return status 200 and response based object proposal")
    void get_proposal_by_id() throws Exception {
        Proposal proposal = new Proposal("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(1000));
        allProposals.save(proposal);

        requester.get("/api/proposals/{id}", proposal.getId())
                .andExpect(status().isOk())
                .andExpect(content().json(requester.toJson(NewProposalResponse.of(proposal))));
    }


    @Test
    @DisplayName("Should not return invalid id proposal, return status 404")
    void get_proposal_by_invalid_id() throws Exception {
        requester.get("/api/proposals/{id}", Long.MAX_VALUE)
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Should not create new proposal without document")
    void add_new_proposal_without_document() throws Exception {

        NewProposalRequest request = new NewProposalRequest(null,
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(1000));

        requester.post("/api/proposals", request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should not create new proposal without email")
    void add_new_proposal_without_email() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                null,
                "Kyo Kusanagi",
                BigDecimal.valueOf(1000));

        requester.post("/api/proposals", request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should not create new proposal without name")
    void add_new_proposal_without_name() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                null,
                BigDecimal.valueOf(1000));

        requester.post("/api/proposals", request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should not create new proposal without salary")
    void add_new_proposal_without_salary() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                null);

        requester.post("/api/proposals", request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should not create new proposal with negative salary")
    void add_new_proposal_with_negative_salary() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(-10000));

        requester.post("/api/proposals", request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should not create new duplicated proposal")
    void add_new_proposal_duplicate() throws Exception {

        NewProposalRequest request = new NewProposalRequest("03098082003",
                "email@email.com",
                "Kyo Kusanagi",
                BigDecimal.valueOf(10000));

        allProposals.save(request.toProposal());
        requester.post("/api/proposals", request)
                .andExpect(status().isUnprocessableEntity());
    }
}