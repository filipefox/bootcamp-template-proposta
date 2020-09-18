package br.com.zup.bootcamp.proposal.card

import br.com.zup.bootcamp.proposal.card.biometric.Biometric
import br.com.zup.bootcamp.proposal.card.block.Block
import br.com.zup.bootcamp.proposal.card.due_date.DueDate
import br.com.zup.bootcamp.proposal.card.parcel.Parcel
import br.com.zup.bootcamp.proposal.card.renegotiation.Renegotiation
import br.com.zup.bootcamp.proposal.card.wallet.Wallet
import br.com.zup.bootcamp.proposal.card.warning.Warning
import java.util.*
import javax.persistence.*

@Entity(name = "zupcamp_cards")
class Card(
        @Id
        var id: UUID,
        var issuedOn: String?,
        var holderName: String?,
        @OneToMany
        var blocks: MutableList<Block>?,
        @OneToMany
        var warnings: MutableList<Warning>?,
        @OneToMany
        var wallets: MutableList<Wallet>?,
        @OneToMany
        var parcels: MutableList<Parcel>?,
        var limitAmount: Int?,
        @OneToOne(fetch = FetchType.LAZY)
        var renegotiation: Renegotiation?,
        @OneToOne(fetch = FetchType.LAZY)
        var dueDate: DueDate?,
        var proposalId: Long,
        var blocked: Boolean = false
) {
    @OneToMany
    @JoinTable(
            joinColumns = [JoinColumn(name = "card_id", referencedColumnName = "id")],
            inverseJoinColumns = [JoinColumn(name = "biometric_id", referencedColumnName = "id")]
    )
    var biometrics: MutableList<Biometric> = mutableListOf()
}