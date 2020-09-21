package br.com.zup.bootcamp.proposal.utils

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.net.URI

class LocationUriUtil {
    fun getUriFromMethodName(javaClass: Class<Any>, methodName: String, vararg id: Any?): URI {
        return MvcUriComponentsBuilder.fromMethodName(javaClass, methodName, id).buildAndExpand(id).toUri()
    }
}