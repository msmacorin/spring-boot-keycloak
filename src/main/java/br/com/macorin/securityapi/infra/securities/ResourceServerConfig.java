package br.com.macorin.securityapi.infra.securities;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    private final JwtAccessTokenCustomizer jwtAccessTokenCustomizer;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests(authz -> authz.antMatchers("/security/**").authenticated()).oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAccessTokenCustomizer);
    }
}
