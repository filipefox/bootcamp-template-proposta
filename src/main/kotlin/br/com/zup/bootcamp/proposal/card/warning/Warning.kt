package br.com.zup.bootcamp.proposal.card.warning

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "zupcamp_warnings")
class Warning(
        @Id
        var id: String,
        var validoAte: String,
        var destino: String
)