package br.com.zup.bootcamp.proposal.card.password_recovery

import br.com.zup.bootcamp.proposal.card.CardRepository
import br.com.zup.bootcamp.proposal.core.utils.LocationUriUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(path = ["/api/cards/{cardId}"])
class CardPasswordRecoveryController(
        private val cardRepository: CardRepository,
        private val passwordRecoveryRepository: CardPasswordRecoveryRepository
) {
    @GetMapping("/v1/passwordRecoveries")
    fun passwordRecovery(@PathVariable("cardId") cardId: String, httpServletRequest: HttpServletRequest): ResponseEntity<Any> {
        return try {
            val uuid = UUID.fromString(cardId)
            val card = cardRepository.findById(uuid).orElseThrow { EntityNotFoundException() }
            val passwordRecovery = passwordRecoveryRepository.save(
                    CardPasswordRecovery(
                            card = card,
                            createAt = Date().toString(),
                            ip = httpServletRequest.remoteAddr,
                            userAgent = httpServletRequest.getHeader("User-Agent"))
            )
            val location = LocationUriUtil().getUriFromMethodName(javaClass, "findById", passwordRecovery.id)
            ResponseEntity.created(location).build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/v1/passwordRecoveries/findById/{id}")
    fun findById(@PathVariable("cardId") cardId: String, @PathVariable("id") id: String): ResponseEntity<CardPasswordRecoveryResponse> {
        return try {
            val uuid = UUID.fromString(cardId)
            val cardPasswordRecovery = passwordRecoveryRepository.findById(uuid).orElseThrow { EntityNotFoundException() }
            ResponseEntity.ok(cardPasswordRecovery.toCardPasswordRecoveryResponse())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}