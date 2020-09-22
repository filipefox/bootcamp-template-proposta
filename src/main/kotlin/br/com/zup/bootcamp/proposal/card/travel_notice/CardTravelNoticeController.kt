package br.com.zup.bootcamp.proposal.card.travel_notice

import br.com.zup.bootcamp.proposal.card.CardRepository
import br.com.zup.bootcamp.proposal.legacy.cartao.LegacyCartaoClient
import br.com.zup.bootcamp.proposal.legacy.cartao.aviso.AvisoRequest
import br.com.zup.bootcamp.proposal.core.utils.LocationUriUtil
import feign.FeignException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/cards/{cardId}"])
class CardTravelNoticeController(
        private val cardRepository: CardRepository,
        private val legacyCartaoClient: LegacyCartaoClient,
        private val cardTravelNoticeRepository: CardTravelNoticeRepository
) {
    @GetMapping("/v1/travelNotices")
    fun travelNotice(@PathVariable("cardId") cardId: String, @Valid @RequestBody cardTravelNoticeRequest: CardTravelNoticeRequest, httpServletRequest: HttpServletRequest): ResponseEntity<Any> {
        return try {
            val uuid = UUID.fromString(cardId)
            val card = cardRepository.findById(uuid).orElseThrow { EntityNotFoundException() }

            try {
                legacyCartaoClient.callTravelNotice(cardId, AvisoRequest(cardTravelNoticeRequest.travelDestination, cardTravelNoticeRequest.endOfTrip))
                val travelNotice = cardTravelNoticeRepository.save(
                        CardTravelNotice(
                                travelDestination = cardTravelNoticeRequest.travelDestination,
                                startOfTrip = cardTravelNoticeRequest.startOfTrip,
                                endOfTrip = cardTravelNoticeRequest.endOfTrip,
                                card = card,
                                createdAt = Date().toString(),
                                ip = httpServletRequest.remoteAddr,
                                userAgent = httpServletRequest.getHeader("User-Agent"))
                )
                val location = LocationUriUtil().getUriFromMethodName(javaClass, "findById", travelNotice.id)
                ResponseEntity.created(location).build()
            } catch (e: FeignException) {
                ResponseEntity.noContent().build()
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/v1/travelNotices/findById/{id}")
    fun findById(@PathVariable("cardId") cardId: String, @PathVariable("id") id: String): ResponseEntity<CardTravelNoticeResponse> {
        return try {
            val uuid = UUID.fromString(id)
            val travelNotice = cardTravelNoticeRepository.findById(uuid).orElseThrow { EntityNotFoundException() }
            ResponseEntity.ok(travelNotice.toTravelNoticeResponse())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}