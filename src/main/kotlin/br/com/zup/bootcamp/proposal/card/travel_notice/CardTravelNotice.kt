package br.com.zup.bootcamp.proposal.card.travel_notice

import br.com.zup.bootcamp.proposal.card.Card
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity(name = "zupcamp_cards_travel_notices")
class CardTravelNotice(
        @Id
        @GeneratedValue
        var id: Long? = null,
        @ManyToOne
        var card: Card,
        var travelDestination: String,
        var startOfTrip: String,
        var endOfTrip: String,
        var ip: String,
        var userAgent: String,
        var createdAt: String
)
/*
fun CardPasswordRecovery.toCardPasswordRecoveryResponse() = CardPasswordRecoveryResponse(
)*/
