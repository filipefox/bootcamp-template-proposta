package br.com.zup.bootcamp.proposal.legacy.financial_analyze

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping

@Service
@FeignClient(name = "FinancialAnalyzeService", url = "http://localhost:9999")
interface FinancialAnalyzeService {
    @GetMapping(value = ["/api/solicitacao"])
    fun make(financialAnalyzeRequest: FinancialAnalyzeRequest): FinancialAnalyzeResponse
}