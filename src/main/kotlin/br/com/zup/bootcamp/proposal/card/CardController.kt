package br.com.zup.bootcamp.proposal.card

import br.com.zup.bootcamp.proposal.legacy.cartao.LegacyCartaoClient
import br.com.zup.bootcamp.proposal.legacy.cartao.bloqueio.BloqueioCartaoRequest
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.*
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping(path = ["/api/cards"])
class CardController(
        private val cardRepository: CardRepository,
        private val legacyCartaoClient: LegacyCartaoClient
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("/v1/blockCard/{id}")
    fun blockCard(@PathVariable("id") id: String): ResponseEntity<CardBlockedResponse> {
        return try {
            val uuid = UUID.fromString(id)
            val card = cardRepository.findById(uuid).orElseThrow { EntityNotFoundException() }

            try {
                legacyCartaoClient.blockCard(id, BloqueioCartaoRequest("XPTO"))
                card.blocked = true
                cardRepository.save(card)
                val location = MvcUriComponentsBuilder.fromMethodName(javaClass, "findById", card.id).buildAndExpand(card.id).toUri()
                ResponseEntity.created(location).body(card.toCardBlockedResponse())
            } catch (e: FeignException) {
                ResponseEntity.ok().body(card.toCardBlockedResponse())
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/v1/findById/{id}")
    fun findById(@PathVariable("id") id: String): ResponseEntity<CardBlockedResponse> {
        return try {
            val uuid = UUID.fromString(id)
            val card = cardRepository.findById(uuid).orElseThrow { EntityNotFoundException() }
            ResponseEntity.ok(card.toCardBlockedResponse())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}