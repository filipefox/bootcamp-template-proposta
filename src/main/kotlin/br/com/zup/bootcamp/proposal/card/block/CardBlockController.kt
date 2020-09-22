package br.com.zup.bootcamp.proposal.card.block

import br.com.zup.bootcamp.proposal.card.CardRepository
import br.com.zup.bootcamp.proposal.legacy.cartao.LegacyCartaoClient
import br.com.zup.bootcamp.proposal.legacy.cartao.bloqueio.BloqueioCartaoRequest
import br.com.zup.bootcamp.proposal.core.utils.LocationUriUtil
import feign.FeignException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping(path = ["/api/cards/{cardId}"])
class CardBlockController(
        private val cardRepository: CardRepository,
        private val legacyCartaoClient: LegacyCartaoClient
) {
    @GetMapping("/v1/blocks")
    fun block(@PathVariable("cardId") cardId: String): ResponseEntity<CardBlockedResponse> {
        return try {
            val uuid = UUID.fromString(cardId)
            val card = cardRepository.findById(uuid).orElseThrow { EntityNotFoundException() }

            try {
                legacyCartaoClient.callBlockCard(cardId, BloqueioCartaoRequest("XPTO"))
                card.blocked = true
                cardRepository.save(card)
                val location = LocationUriUtil().getUriFromMethodName(javaClass, "findById", card.id)
                ResponseEntity.created(location).body(card.toCardBlockedResponse())
            } catch (e: FeignException) {
                ResponseEntity.noContent().build()
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/v1/blocks/findById/{id}")
    fun findById(@PathVariable("cardId") cardId: String, @PathVariable("id") id: String): ResponseEntity<CardBlockedResponse> {
        return try {
            val uuid = UUID.fromString(cardId)
            val card = cardRepository.findById(uuid).orElseThrow { EntityNotFoundException() }
            ResponseEntity.ok(card.toCardBlockedResponse())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}