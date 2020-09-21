package br.com.zup.bootcamp.proposal.card.travel_notice

data class TravelNoticeRequest(
        var travelDestination: String,
        var startOfTrip: String,
        var endOfTrip: String
)