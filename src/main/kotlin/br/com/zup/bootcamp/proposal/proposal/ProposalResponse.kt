package br.com.zup.bootcamp.proposal.proposal

data class ProposalResponse(
        val id: Long?,
        val requesterId: Long?,
        val status: ProposalStatus
)

fun Proposal.toProposalResponse() = ProposalResponse(
        id = id,
        requesterId = requester.id,
        status = status
)