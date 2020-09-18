package br.com.zup.bootcamp.proposal.card.due_date

import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "zupcamp_due_dates")
class DueDate(
        @Id
        var id: String,
        var dia: Int,
        var dataDeCriacao: String
)