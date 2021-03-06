package br.com.zup.bootcamp.proposal.card.travel_notice

import br.com.zup.bootcamp.proposal.card.Card

data class CardTravelNoticeResponse(
        var id: Long?,
        var card: Card,
        var travelDestination: String,
        var startOfTrip: String,
        var endOfTrip: String,
        var ip: String,
        var userAgent: String,
        var createdAt: String
)

fun CardTravelNotice.toTravelNoticeResponse() = CardTravelNoticeResponse(
        id = id,
        card = card,
        travelDestination = travelDestination,
        startOfTrip = startOfTrip,
        endOfTrip = endOfTrip,
        ip = ip,
        userAgent = userAgent,
        createdAt = createdAt
)