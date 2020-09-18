package br.com.zup.bootcamp.proposal.builders

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.net.URI

class LocationUriUtil {
    fun getUriFromMethodName(javaClass: Class<Any>, methodName: String, id: Long?): URI {
        return MvcUriComponentsBuilder.fromMethodName(javaClass, methodName, id).buildAndExpand(id).toUri()
    }
}