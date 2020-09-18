package br.com.zup.bootcamp.proposal.core.configurations

import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class HttpSecurityConfiguration : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests {
            it
                    .antMatchers(HttpMethod.POST, "/api/proposals/**").hasAuthority("SCOPE_proposals:create")
                    .antMatchers(HttpMethod.GET, "/api/proposals/**").hasAuthority("SCOPE_proposals:read")

                    .antMatchers(HttpMethod.POST, "/api/cards/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/cards/**").permitAll()
        }
                .oauth2ResourceServer { it.jwt() }
    }
}