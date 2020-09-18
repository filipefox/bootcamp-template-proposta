package br.com.zup.bootcamp.proposal.proposal

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProposalRepository : JpaRepository<Proposal, Long> {
    fun findByRequesterId(id: Long?): Optional<Proposal>
    fun findByCardIsNull(): List<Proposal>
}