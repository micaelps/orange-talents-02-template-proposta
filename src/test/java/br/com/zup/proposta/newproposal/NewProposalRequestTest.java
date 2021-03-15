package br.com.zup.proposta.newproposal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class NewProposalRequestTest {

    @ParameterizedTest
    @CsvSource({"166.161.280-67, 16616128067", "19.602.675/0001-39, 19602675000139"})
    void test1(String originalDocument, String expectedDocument){
        NewProposalRequest request = new NewProposalRequest(originalDocument,
                "email@email.com",
                "Joe Higashi",
                BigDecimal.valueOf(1000));

        Proposal proposal = request.toProposal();
        assertEquals(proposal.getDocument(), expectedDocument);
    }
}