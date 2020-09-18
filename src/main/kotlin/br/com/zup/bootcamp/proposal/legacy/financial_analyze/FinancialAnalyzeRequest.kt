package br.com.zup.bootcamp.proposal.legacy.financial_analyze

import br.com.zup.bootcamp.proposal.proposal.Proposal

data class FinancialAnalyzeRequest(
        val documento: String,
        val nome: String,
        val idProposta: String
)

fun Proposal.toFinancialAnalyzeRequest() = FinancialAnalyzeRequest(
        documento = requester.document,
        nome = requester.name,
        idProposta = id.toString()
)