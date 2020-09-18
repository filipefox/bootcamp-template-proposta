package br.com.zup.bootcamp.proposal.proposal

import br.com.zup.bootcamp.proposal.requester.Requester
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

data class ProposalRequest(
        @field:Pattern(regexp = "(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)|(^\\d{2}.\\d{3}.\\d{3}/\\d{4}-\\d{2}$)")
        var cpfOrCnpj: String,

        @field:NotBlank
        @field:Email
        var email: String,

        @field:NotBlank
        var name: String,

        @field:NotBlank
        var address: String,

        @field:NotBlank
        @field:Positive
        var salary: String
)

fun ProposalRequest.toEntity() = Proposal(
        requester = Requester(
                document = cpfOrCnpj,
                email = email,
                name = name,
                address = address,
                salary = salary
        )
)