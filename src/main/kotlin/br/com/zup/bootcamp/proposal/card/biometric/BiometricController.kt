package br.com.zup.bootcamp.proposal.card.biometric

import br.com.zup.bootcamp.proposal.card.CardRepository
import br.com.zup.bootcamp.proposal.core.utils.LocationUriUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/biometrics"])
class BiometricController(
        private val biometricRepository: BiometricRepository,
        private val cardRepository: CardRepository
) {
    @PostMapping("/v1/create")
    fun create(@RequestParam cardId: UUID, @Valid @RequestBody biometricRequest: BiometricRequest): ResponseEntity<BiometricResponse> {
        return try {
            val card = cardRepository.findById(cardId).orElseThrow { EntityNotFoundException() }
            val biometric = biometricRepository.save(biometricRequest.toEntity())
            card.biometrics.add(biometric)
            cardRepository.save(card)
            val location = LocationUriUtil().getUriFromMethodName(javaClass, "findById", biometric.id)
            ResponseEntity.created(location).body(biometric.toBiometricResponse())
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
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