package br.com.zup.bootcamp.proposal.proposal

import br.com.zup.bootcamp.proposal.builders.LocationUriUtil
import br.com.zup.bootcamp.proposal.financial_analyze.FinancialAnalyzeService
import br.com.zup.bootcamp.proposal.financial_analyze.exceptions.FinancialAnalyzeException
import br.com.zup.bootcamp.proposal.financial_analyze.exceptions.FinancialAnalyzeNotEligibleException
import br.com.zup.bootcamp.proposal.requester.RequesterRepository
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
    fun save(@Valid @RequestBody proposalRequest: ProposalRequest): ResponseEntity<ProposalResponse> {
        return try {
            val requester = requesterRepository.findByDocument(proposalRequest.cpfOrCnpj).orElseThrow { EntityNotFoundException() }
            proposalRepository.findByRequesterId(requester.id).orElseThrow { EntityNotFoundException() }
            ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build()
        } catch (e: EntityNotFoundException) {
            val proposal = proposalRepository.save(proposalRequest.toEntity())

            try {
                financialAnalyzeService.makeFinancialAnalyze(proposal)
                saveProposalStatus(proposal, "ELEGIVEL")
                val location = LocationUriUtil().getUriFromMethodName(javaClass, "findById", proposal.id)
                ResponseEntity.created(location).body(proposal.toProposalResponse())
            } catch (e: FinancialAnalyzeNotEligibleException) {
                saveProposalStatus(proposal, "NAO_ELEGIVEL")
                ResponseEntity.ok().build()
            } catch (e: FinancialAnalyzeException) {
                saveProposalStatus(proposal, "ERROR")
                ResponseEntity.ok().build()
            }
        }
    }

    @GetMapping("/v1/findAll")
    fun findAll(): ResponseEntity<List<ProposalResponse>> {
        val proposals = proposalRepository.findAll()
        return ResponseEntity.ok(proposals.map { proposal -> proposal.toProposalResponse() }.toList())
    }

    @PutMapping("/v1/updateById/{id}")
    fun updateById(@PathVariable("id") id: Long, @Valid @RequestBody proposalRequest: ProposalRequest): ResponseEntity<ProposalResponse> {
        proposalRepository.findById(id).orElseThrow { EntityNotFoundException() }
        var proposal = proposalRequest.toEntity()
        proposal.id = id
        proposal = proposalRepository.save(proposal)
        return ResponseEntity.ok(proposal.toProposalResponse())
    }

    @DeleteMapping("/v1/deleteById/{id}")
    fun deleteById(@PathVariable("id") id: Long) {
        proposalRepository.deleteById(id)
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

    private fun saveProposalStatus(proposal: Proposal, status: String) {
        proposal.status = status
        proposalRepository.save(proposal)
    }
}