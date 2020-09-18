package br.com.zup.bootcamp.proposal.card

import br.com.zup.bootcamp.proposal.legacy.cartao.LegacyCartaoClient
import br.com.zup.bootcamp.proposal.legacy.cartao.toCard
import br.com.zup.bootcamp.proposal.proposal.ProposalRepository
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CardService(
        private val cardRepository: CardRepository,
        private val proposalRepository: ProposalRepository,
        private val legacyCartaoClient: LegacyCartaoClient
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 60 * 1000)
    fun check() {
        val proposalsWithoutCard = proposalRepository.findByCardIsNull()
        proposalsWithoutCard.forEach { proposal ->
            try {
                val legacyCartaoResponse = legacyCartaoClient.getByIdProposta(proposal.id.toString())
                val card = cardRepository.save(legacyCartaoResponse.toCard())
                proposal.card = card
                proposalRepository.save(proposal)
            } catch (e: FeignException) {
                log.error("Proposta número ${proposal.id} ainda não foi processada. Aguardando próxima iteração.")
            }
        }
    }
}