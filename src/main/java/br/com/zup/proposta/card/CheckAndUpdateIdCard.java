package br.com.zup.proposta.card;

import br.com.zup.proposta.proposal.AllProposals;
import br.com.zup.proposta.proposal.Proposal;
import br.com.zup.proposta.proposal.ProposalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
class CheckAndUpdateIdCard {

    final AllProposals allProposals;

    final CardVerification cardVerification;

    public CheckAndUpdateIdCard(AllProposals allProposals, CardVerification cardVerification) {
        this.allProposals = allProposals;
        this.cardVerification = cardVerification;
    }

    @Scheduled(fixedDelay=5000)
    public void check() {
        List<Proposal> proposalsCardIdNull = allProposals.findTop10ByStatusAndCardIsNull(ProposalStatus.ELEGIBLE);
        proposalsCardIdNull.forEach(this::updateCardId);
    }

    @Transactional
    private void updateCardId(Proposal proposal) {
        CardVerificationRequest request = new CardVerificationRequest(proposal.getDocument(), proposal.getName(), proposal.getId().toString());
        CardVerificationResponse response = cardVerification.verify(request);
        proposal.setCard(response.toCard(proposal));
        allProposals.save(proposal);


    }
}