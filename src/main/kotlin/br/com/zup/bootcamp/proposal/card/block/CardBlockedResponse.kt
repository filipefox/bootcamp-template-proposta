package br.com.zup.bootcamp.proposal.card.block

import br.com.zup.bootcamp.proposal.card.Card

data class CardBlockedResponse(
        val blocked: Boolean
)

fun Card.toCardBlockedResponse() = CardBlockedResponse(
        blocked = blocked
)