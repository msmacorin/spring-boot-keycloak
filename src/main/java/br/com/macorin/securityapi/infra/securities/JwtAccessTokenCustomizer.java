package br.com.macorin.securityapi.infra.securities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAccessTokenCustomizer extends JwtAuthenticationConverter {

    private final ObjectMapper objectMapper;

    @Override
    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Set<String> rolesWithPrefix = new HashSet<>();
        JsonNode json = objectMapper.convertValue(jwt.getClaim("resource_access"), JsonNode.class);
        json.elements().forEachRemaining(
                e -> e.path("roles").elements().forEachRemaining(r -> rolesWithPrefix.add("ROLE_" + r.asText())));
        return AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
    }
}
