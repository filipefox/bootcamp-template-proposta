package br.com.zup.bootcamp.proposal.card.password_recovery

import br.com.zup.bootcamp.proposal.card.Card
import javax.persistence.GeneratedValue
import javax.persistence.Id

class CardPasswordRecoveryResponse(
        @Id
        @GeneratedValue
        var id: Long? = null,
        var card: Card,
        var ip: String,
        var userAgent: String,
        var createdAt: String
)