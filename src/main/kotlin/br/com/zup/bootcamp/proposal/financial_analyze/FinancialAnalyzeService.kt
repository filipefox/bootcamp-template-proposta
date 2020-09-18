package br.com.zup.bootcamp.proposal.financial_analyze

import br.com.zup.bootcamp.proposal.financial_analyze.exceptions.FinancialAnalyzeException
import br.com.zup.bootcamp.proposal.financial_analyze.exceptions.FinancialAnalyzeNotEligibleException
import br.com.zup.bootcamp.proposal.legacy.financial_analyze.FinancialAnalyzeClient
import br.com.zup.bootcamp.proposal.legacy.financial_analyze.toFinancialAnalyzeRequest
import br.com.zup.bootcamp.proposal.proposal.Proposal
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FinancialAnalyzeService(
        private val financialAnalyzeClient: FinancialAnalyzeClient
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun makeFinancialAnalyze(proposal: Proposal) {
        try {
            val cardAnalyzeRequest = financialAnalyzeClient.make(proposal.toFinancialAnalyzeRequest())

            if (cardAnalyzeRequest.resultadoSolicitacao == "SEM_RESTRICAO") {
                log.info("Proposta de número ${proposal.id} sem restrições")
            } else {
                log.warn("Proposta de número ${proposal.id} possui a restrição \"${cardAnalyzeRequest.resultadoSolicitacao}\"")
                throw FinancialAnalyzeNotEligibleException()
            }
        } catch (e: FeignException.FeignClientException) {
            log.error("exceção na proposta de número ${proposal.id}", e)
            throw FinancialAnalyzeException()
        }
    }
}