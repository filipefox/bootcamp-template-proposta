package br.com.zup.bootcamp.proposal.card

import br.com.zup.bootcamp.proposal.card.block.CardBlockedResponse
import br.com.zup.bootcamp.proposal.card.block.toCardBlockedResponse
import br.com.zup.bootcamp.proposal.card.password_recovery.CardPasswordRecovery
import br.com.zup.bootcamp.proposal.card.password_recovery.CardPasswordRecoveryRepository
import br.com.zup.bootcamp.proposal.card.password_recovery.CardPasswordRecoveryResponse
import br.com.zup.bootcamp.proposal.card.password_recovery.toCardPasswordRecoveryResponse
import br.com.zup.bootcamp.proposal.card.travel_notice.TravelNotice
import br.com.zup.bootcamp.proposal.card.travel_notice.TravelNoticeRepository
import br.com.zup.bootcamp.proposal.card.travel_notice.TravelNoticeResponse
import br.com.zup.bootcamp.proposal.card.travel_notice.toTravelNoticeResponse
import br.com.zup.bootcamp.proposal.legacy.cartao.LegacyCartaoClient
import br.com.zup.bootcamp.proposal.legacy.cartao.aviso.AvisoRequest
import br.com.zup.bootcamp.proposal.legacy.cartao.bloqueio.BloqueioCartaoRequest
import br.com.zup.bootcamp.proposal.utils.LocationUriUtil
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/cards"])
class CardController(
        private val cardRepository: CardRepository,
        private val legacyCartaoClient: LegacyCartaoClient,
        private val passwordRecoveryRepository: CardPasswordRecoveryRepository,
        private val travelNoticeRepository: TravelNoticeRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/v1/blockCard/{id}")
    fun blockCard(@PathVariable("id") id: String): ResponseEntity<CardBlockedResponse> {
        return try {
            val uuid = UUID.fromString(id)
            val card = cardRepository.findById(uuid).orElseThrow { EntityNotFoundException() }

            try {
                legacyCartaoClient.callBlockCard(id, BloqueioCartaoRequest("XPTO"))
                card.blocked = true
                cardRepository.save(card)
                val location = LocationUriUtil().getUriFromMethodName(javaClass, "findPasswordRecoveryById", card.id)
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

    @GetMapping("/v1/blockCard/findById/{id}")
    fun findBlockCardById(@PathVariable("id") id: String): ResponseEntity<CardBlockedResponse> {
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

    @GetMapping("/v1/passwordRecovery/{id}")
    fun passwordRecovery(@PathVariable("id") id: String, httpServletRequest: HttpServletRequest): ResponseEntity<Any> {
        return try {
            val uuid = UUID.fromString(id)
            val card = cardRepository.findById(uuid).orElseThrow { EntityNotFoundException() }
            val passwordRecovery = passwordRecoveryRepository.save(
                    CardPasswordRecovery(
                            card = card,
                            createAt = Date().toString(),
                            ip = httpServletRequest.remoteAddr,
                            userAgent = httpServletRequest.getHeader("User-Agent"))
            )
            val location = LocationUriUtil().getUriFromMethodName(javaClass, "findPasswordRecoveryById", passwordRecovery.id)
            ResponseEntity.created(location).build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/v1/passwordRecovery/findById/{id}")
    fun findPasswordRecoveryById(@PathVariable("id") id: String): ResponseEntity<CardPasswordRecoveryResponse> {
        return try {
            val uuid = UUID.fromString(id)
            val cardPasswordRecovery = passwordRecoveryRepository.findById(uuid).orElseThrow { EntityNotFoundException() }
            ResponseEntity.ok(cardPasswordRecovery.toCardPasswordRecoveryResponse())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/v1/travelNotice/{id}")
    fun travelNotice(@PathVariable("id") id: String, @Valid @RequestBody travelNoticeResponse: TravelNoticeResponse, httpServletRequest: HttpServletRequest): ResponseEntity<Any> {
        return try {
            val uuid = UUID.fromString(id)
            val card = cardRepository.findById(uuid).orElseThrow { EntityNotFoundException() }

            try {
                legacyCartaoClient.callTravelNotice(id, AvisoRequest(travelNoticeResponse.travelDestination, travelNoticeResponse.endOfTrip))
                val travelNotice = travelNoticeRepository.save(
                        TravelNotice(
                                travelDestination = travelNoticeResponse.travelDestination,
                                startOfTrip = travelNoticeResponse.startOfTrip,
                                endOfTrip = travelNoticeResponse.endOfTrip,
                                card = card,
                                createdAt = Date().toString(),
                                ip = httpServletRequest.remoteAddr,
                                userAgent = httpServletRequest.getHeader("User-Agent"))
                )
                val location = LocationUriUtil().getUriFromMethodName(javaClass, "findTravelNoticeById", travelNotice.id)
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

    @GetMapping("/v1/travelNotice/findById/{id}")
    fun findTravelNoticeById(@PathVariable("id") id: String): ResponseEntity<TravelNoticeResponse> {
        return try {
            val uuid = UUID.fromString(id)
            val travelNotice = travelNoticeRepository.findById(uuid).orElseThrow { EntityNotFoundException() }
            ResponseEntity.ok(travelNotice.toTravelNoticeResponse())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: EntityNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}