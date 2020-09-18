package br.com.zup.bootcamp.proposal.card.biometric

import javax.validation.constraints.NotBlank

data class BiometricRequest(
        @field:NotBlank
        var fingerprint: String
)

fun BiometricRequest.toEntity() = Biometric(
        fingerprint = fingerprint
)