package br.com.zup.bootcamp.proposal.legacy.cartao

import br.com.zup.bootcamp.proposal.card.Card
import br.com.zup.bootcamp.proposal.legacy.cartao.aviso.Aviso
import br.com.zup.bootcamp.proposal.legacy.cartao.bloqueio.Bloqueio
import br.com.zup.bootcamp.proposal.legacy.cartao.carteira.Carteira
import br.com.zup.bootcamp.proposal.legacy.cartao.parcela.Parcela
import br.com.zup.bootcamp.proposal.legacy.cartao.renegociacao.Renegociacao
import br.com.zup.bootcamp.proposal.legacy.cartao.vencimento.Vencimento
import java.util.*

data class LegacyCartaoResponse(
        var id: String,
        var emitidoEm: String?,
        var titular: String?,
        var blocks: List<Bloqueio>?,
        var warnings: List<Aviso>?,
        var carteiras: List<Carteira>?,
        var parcels: List<Parcela>?,
        var limite: Int?,
        var renegociacao: Renegociacao?,
        var vencimento: Vencimento?,
        var idProposta: String
)

fun LegacyCartaoResponse.toCard() = Card(
        id = UUID.fromString(id),
        issuedOn = emitidoEm,
        holderName = titular,
        cardBlocks = null,
        warnings = null,
        wallets = null,
        parcels = null,
        limitAmount = limite,
        renegotiation = null,
        dueDate = null,
        proposalId = idProposta.toLong()
)