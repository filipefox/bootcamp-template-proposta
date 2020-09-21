package br.com.zup.bootcamp.proposal.legacy.cartao

import br.com.zup.bootcamp.proposal.legacy.cartao.aviso.AvisoRequest
import br.com.zup.bootcamp.proposal.legacy.cartao.aviso.AvisoResponse
import br.com.zup.bootcamp.proposal.legacy.cartao.bloqueio.BloqueioCartaoRequest
import br.com.zup.bootcamp.proposal.legacy.cartao.bloqueio.BloqueioCartaoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(name = "LegacyCartaoClient", url = "http://localhost:8888")
interface LegacyCartaoClient {
    @GetMapping(value = ["/api/cartoes"])
    fun getByIdProposta(@RequestParam idProposta: String): LegacyCartaoResponse

    @PostMapping(value = ["/api/cartoes/{id}/bloqueios"])
    fun callBlockCard(@PathVariable id: String, @RequestBody bloqueioCartaoRequest: BloqueioCartaoRequest): BloqueioCartaoResponse

    @PostMapping(value = ["/api/cartoes/{id}/avisos"])
    fun callTravelNotice(@PathVariable id: String, @RequestBody avisoRequest: AvisoRequest): AvisoResponse
}