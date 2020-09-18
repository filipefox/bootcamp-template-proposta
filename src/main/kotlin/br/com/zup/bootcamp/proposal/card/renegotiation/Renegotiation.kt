package br.com.zup.bootcamp.proposal.card.renegotiation

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "zupcamp_renegotiations")
class Renegotiation(
        @Id
        var id: String,
        var quantidade: Int,
        var valor: Int,
        var dataDeCriacao: String
)