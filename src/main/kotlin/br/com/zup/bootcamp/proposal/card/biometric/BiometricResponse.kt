package br.com.zup.bootcamp.proposal.card.biometric

data class BiometricResponse(
        val id: Long?,
        val fingerprint: String,
        val associatedIn: String?
)

fun Biometric.toBiometricResponse() = BiometricResponse(
        id = id,
        fingerprint = fingerprint,
        associatedIn = associatedIn
)