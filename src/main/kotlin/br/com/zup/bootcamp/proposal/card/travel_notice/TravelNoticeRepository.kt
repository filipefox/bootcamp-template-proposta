package br.com.zup.bootcamp.proposal.card.travel_notice

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TravelNoticeRepository : JpaRepository<TravelNotice, UUID>