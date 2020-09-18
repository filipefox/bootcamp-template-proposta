package br.com.zup.bootcamp.proposal.proposal

import br.com.zup.bootcamp.proposal.legacy.financial_analyze.FinancialAnalyzeService
import br.com.zup.bootcamp.proposal.legacy.financial_analyze.toFinancialAnalyzeRequest
import br.com.zup.bootcamp.proposal.requester.RequesterRepository
import feign.FeignException
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import javax.persistence.EntityNotFoundException
import javax.validation.Valid


@RestController
@RequestMapping(path = ["/api/proposals"])
class ProposalController(
        private val proposalRepository: ProposalRepository,
        private val requesterRepository: RequesterRepository,
        private val financialAnalyzeService: FinancialAnalyzeService,
        private val meterRegistry: MeterRegistry
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("/v1/create")
    fun save(@Valid @RequestBody proposalRequest: ProposalRequest): ResponseEntity<ProposalResponse> {
        return try {
            val requester = requesterRepository.findByDocument(proposalRequest.cpfOrCnpj).orElseThrow { EntityNotFoundException() }
            proposalRepository.findByRequesterId(requester.id).orElseThrow { EntityNotFoundException() }
            ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build()
        } catch (e: EntityNotFoundException) {
            var proposal = proposalRequest.toEntity()
            proposal = proposalRepository.save(proposal)

            try {
                val cardAnalyzeRequest = financialAnalyzeService.make(proposal.toFinancialAnalyzeRequest())

                if (cardAnalyzeRequest.resultadoSolicitacao == "SEM_RESTRICAO") {
                    proposal.status = "ELEGIVEL"
                } else {
                    proposal.status = "NAO_ELEGIVEL"
                }
            } catch (e: FeignException.FeignClientException) {
                proposal.status = "ERROR"
            }

            val tags: ArrayList<Tag> = arrayListOf()
            tags.add(Tag.of("bank", "Itaú"))
            tags.add(Tag.of("issuer", "Mastercard"))
            val counter = meterRegistry.counter("created_proposal", tags)
            counter.increment()

            // TODO: Remover esse save aqui só pra poder salvar o status!
            proposalRepository.save(proposal)

            val location = MvcUriComponentsBuilder
                    .fromMethodName(javaClass, "findById", proposal.id)
                    .buildAndExpand(proposal.id)
                    .toUri()
            ResponseEntity.created(location).body(proposal.toProposalResponse())
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
}