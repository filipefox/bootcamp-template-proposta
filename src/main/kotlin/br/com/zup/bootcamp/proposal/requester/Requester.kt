package br.com.zup.bootcamp.proposal.requester

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "zupcamp_requesters")
class Requester(
        var document: String,
        var email: String,
        var name: String,
        var address: String,
        var salary: String
) {
    @Id
    @GeneratedValue
    val id: Long? = null
}