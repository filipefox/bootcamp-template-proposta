package br.com.zup.bootcamp.proposal.card

data class CardBlockedResponse(
        val blocked: Boolean
)

fun Card.toCardBlockedResponse() = CardBlockedResponse(
        blocked = blocked
)