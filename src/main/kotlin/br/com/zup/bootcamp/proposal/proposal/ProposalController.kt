package br.com.zup.bootcamp.proposal.proposal

import br.com.zup.bootcamp.proposal.financial_analyze.FinancialAnalyzeService
import br.com.zup.bootcamp.proposal.financial_analyze.exceptions.FinancialAnalyzeException
import br.com.zup.bootcamp.proposal.financial_analyze.exceptions.FinancialAnalyzeNotEligibleException
import br.com.zup.bootcamp.proposal.requester.RequesterRepository
import br.com.zup.bootcamp.proposal.core.utils.LocationUriUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/proposals"])
class ProposalController(
        private val proposalRepository: ProposalRepository,
        private val requesterRepository: RequesterRepository,
        private val financialAnalyzeService: FinancialAnalyzeService
) {
    @PostMapping("/v1/create")
    fun create(@Valid @RequestBody proposalRequest: ProposalRequest): ResponseEntity<ProposalResponse> {
        try {
            val requester = requesterRepository.findByDocument(proposalRequest.cpfOrCnpj).orElseThrow { EntityNotFoundException() }
            proposalRepository.findByRequesterId(requester.id).orElseThrow { EntityNotFoundException() }
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build()
        } catch (e: EntityNotFoundException) {
            var responseEntity: ResponseEntity<ProposalResponse>
            val proposal = proposalRequest.toEntity()

            try {
                financialAnalyzeService.makeFinancialAnalysis(proposal)
                proposal.status = ProposalStatus.ELIGIBLE
                val location = LocationUriUtil().getUriFromMethodName(javaClass, "findById", proposal.id)
                responseEntity = ResponseEntity.created(location).body(proposal.toProposalResponse())
            } catch (e: FinancialAnalyzeNotEligibleException) {
                proposal.status = ProposalStatus.NOT_ELIGIBLE
                responseEntity = ResponseEntity.ok().build()
            } catch (e: FinancialAnalyzeException) {
                proposal.status = ProposalStatus.EXCEPTION
                responseEntity = ResponseEntity.ok().build()
            }

            proposalRepository.save(proposal)
            return responseEntity
        }
    }

    @GetMapping("/v1/findById/{id}")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<ProposalResponse> {
        return try {
            val proposal = proposalRepository.findById(id).orElseThrow { EntityNotFoundException() }
            ResponseEntity.ok(proposal.toProposalResponse())
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}