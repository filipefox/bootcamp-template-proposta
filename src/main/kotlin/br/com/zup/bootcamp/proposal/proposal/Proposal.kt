package br.com.zup.bootcamp.proposal.proposal

import br.com.zup.bootcamp.proposal.card.Card
import br.com.zup.bootcamp.proposal.requester.Requester
import javax.persistence.*

@Entity(name = "zupcamp_proposals")
class Proposal(
        @Id
        @GeneratedValue
        var id: Long? = null,
        @OneToOne(cascade = [CascadeType.ALL])
        var requester: Requester,
        @OneToOne
        var card: Card? = null,
        var status: ProposalStatus = ProposalStatus.RECEIVED
)