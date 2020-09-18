package br.com.zup.bootcamp.proposal.requester

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RequesterRepository : JpaRepository<Requester, Long> {
    fun findByDocument(cpfOrCnpj: String): Optional<Requester>
}