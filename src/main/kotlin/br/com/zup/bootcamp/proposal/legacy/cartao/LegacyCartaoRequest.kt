package br.com.zup.bootcamp.proposal.legacy.cartao

data class LegacyCartaoRequest(
        val documento: String,
        val nome: String,
        val idProposta: String
)