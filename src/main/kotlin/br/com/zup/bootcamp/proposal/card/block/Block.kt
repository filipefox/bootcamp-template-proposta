package br.com.zup.bootcamp.proposal.card.block

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "zupcamp_blocks")
class Block(
        @Id
        var id: String,
        var bloqueadoEm: String,
        var sistemaResponsavel: String,
        var ativo: Boolean
)