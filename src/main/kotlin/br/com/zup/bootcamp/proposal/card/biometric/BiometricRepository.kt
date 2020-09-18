package br.com.zup.bootcamp.proposal.card.biometric

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BiometricRepository : JpaRepository<Biometric, Long>