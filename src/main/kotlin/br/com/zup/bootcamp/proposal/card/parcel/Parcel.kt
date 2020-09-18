package br.com.zup.bootcamp.proposal.card.parcel

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "zupcamp_parcels")
class Parcel(
        @Id
        var id: String,
        var quantidade: Int,
        var valor: Int
)