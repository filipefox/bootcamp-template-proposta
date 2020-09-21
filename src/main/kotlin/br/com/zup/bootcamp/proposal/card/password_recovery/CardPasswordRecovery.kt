package br.com.zup.bootcamp.proposal.card.password_recovery

import br.com.zup.bootcamp.proposal.card.Card
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity(name = "zupcamp_cards_password_recoveries")
class CardPasswordRecovery(
        @Id
        @GeneratedValue
        var id: Long? = null,
        @ManyToOne
        var card: Card,
        var ip: String,
        var userAgent: String,
        var createAt: String
)

fun CardPasswordRecovery.toCardPasswordRecoveryResponse() = CardPasswordRecoveryResponse(
        id = id,
        card = card,
        ip = ip,
        userAgent = userAgent,
        createdAt = createAt
)