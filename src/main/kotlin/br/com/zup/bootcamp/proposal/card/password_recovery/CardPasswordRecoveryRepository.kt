package br.com.zup.bootcamp.proposal.card.password_recovery

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CardPasswordRecoveryRepository : JpaRepository<CardPasswordRecovery, UUID>