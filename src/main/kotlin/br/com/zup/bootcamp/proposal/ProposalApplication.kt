package br.com.zup.bootcamp.proposal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableFeignClients
@EnableAsync
@EnableScheduling
class ProposalApplication

fun main(args: Array<String>) {
    runApplication<ProposalApplication>(*args)
}
