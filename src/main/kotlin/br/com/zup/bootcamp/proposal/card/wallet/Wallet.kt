package br.com.zup.bootcamp.proposal.card.wallet

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "zupcamp_wallets")
class Wallet(
        @Id
        var id: String,
        var email: String,
        var associadaEm: String,
        var emissor: String
)