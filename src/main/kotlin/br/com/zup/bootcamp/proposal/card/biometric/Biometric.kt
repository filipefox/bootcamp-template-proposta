package br.com.zup.bootcamp.proposal.card.biometric

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "zupcamp_biometrics")
class Biometric(
        var fingerprint: String
) {
    @Id
    @GeneratedValue
    var id: Long? = null
    var associatedIn: String? = null
}