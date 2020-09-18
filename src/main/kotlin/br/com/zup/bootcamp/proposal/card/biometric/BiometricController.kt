package br.com.zup.bootcamp.proposal.card.biometric

import br.com.zup.bootcamp.proposal.card.CardRepository
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/biometrics"])
class BiometricController(
        private val biometricRepository: BiometricRepository,
        private val cardRepository: CardRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("/v1/create")
    fun save(@RequestParam cardId: UUID, @Valid @RequestBody biometricRequest: BiometricRequest): ResponseEntity<BiometricResponse> {
        return try {
            val card = cardRepository.findById(cardId).orElseThrow { EntityNotFoundException() }
            val biometric = biometricRepository.save(biometricRequest.toEntity())
            card.biometrics.add(biometric)
            cardRepository.save(card)
            val location = MvcUriComponentsBuilder
                    .fromMethodName(javaClass, "findById", biometric.id)
                    .buildAndExpand(biometric.id)
                    .toUri()
            ResponseEntity.created(location).body(biometric.toBiometricResponse())
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/v1/findAll")
    fun findAll(): ResponseEntity<List<BiometricResponse>> {
        val biometrics = biometricRepository.findAll()
        return ResponseEntity.ok(biometrics.map { biometric -> biometric.toBiometricResponse() }.toList())
    }

    @PutMapping("/v1/updateById/{id}")
    fun updateById(@PathVariable("id") id: Long, @Valid @RequestBody biometricRequest: BiometricRequest): ResponseEntity<BiometricResponse> {
        biometricRepository.findById(id).orElseThrow { EntityNotFoundException() }
        var biometric = biometricRequest.toEntity()
        biometric.id = id
        biometric = biometricRepository.save(biometric)
        return ResponseEntity.ok(biometric.toBiometricResponse())
    }

    @DeleteMapping("/v1/deleteById/{id}")
    fun deleteById(@PathVariable("id") id: Long) {
        biometricRepository.deleteById(id)
    }

    @GetMapping("/v1/findById/{id}")
    fun findById(@PathVariable("id") id: Long): ResponseEntity<BiometricResponse> {
        return try {
            val proposal = biometricRepository.findById(id).orElseThrow { EntityNotFoundException() }
            ResponseEntity.ok(proposal.toBiometricResponse())
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}