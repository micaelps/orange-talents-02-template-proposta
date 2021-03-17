package br.com.zup.proposta.proposal;

import br.com.zup.proposta.proposal.feignClients.CardVerificationRequest;
import br.com.zup.proposta.proposal.feignClients.CardVerificationResponse;
import br.com.zup.proposta.proposal.feignClients.CardVerificattion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
class CheckAndUpdateIdCard {

    @Autowired
    AllProposals allProposals;

    @Autowired
    CardVerificattion cardVerificattion;

    @Scheduled(fixedDelay=5000)
    public void check() {
        List<Proposal> proposalsCardIdNull = allProposals.findTop10ByStatusAndCardIdIsNull(ProposalStatus.ELEGIBLE);
        proposalsCardIdNull.forEach(this::updateCardId);
    }

    @Transactional
    private void updateCardId(Proposal proposal) {
        CardVerificationRequest request = new CardVerificationRequest(proposal.getDocument(), proposal.getName(), proposal.getId().toString());
        CardVerificationResponse response = cardVerificattion.verify(request);
        proposal.setCardId(response.getId());
        allProposals.save(proposal);
    }
}
