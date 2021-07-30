package br.com.macorin.securityapi.infra.securities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classe responsavel por converter o token gerado pelo Keycloak
 * de maneira a atribuir os authorities do jeito que o spring security
 * utiliza.
 */
@Component
@RequiredArgsConstructor
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final ObjectMapper objectMapper;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream
                .concat(defaultGrantedAuthoritiesConverter.convert(jwt).stream()
                        , extractAuthorities(jwt).stream())
                .collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * orquestra a extração dos authorities
     * @param jwt
     * @return Collection<GrantedAuthority> contendo as roles e scopes do jwt
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Set<String> rolesWithPrefix = new HashSet<>();
        rolesWithPrefix.addAll(getRealmRoles(jwt));
        rolesWithPrefix.addAll(getResourceRoles(jwt));
        return AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));
    }

    /**
     * Busca as roles de acesso ao realm
     * @param jwt
     * @return uma colection contendo as roles
     */
    private Set<String> getRealmRoles(Jwt jwt) {
        Set<String> rolesWithPrefix = new HashSet<>();
        JsonNode json = objectMapper.convertValue(jwt.getClaim("realm_access"), JsonNode.class);
        json.elements().forEachRemaining(
                e -> e.elements().forEachRemaining(r -> rolesWithPrefix.add(createRole(r.asText()))));
        return rolesWithPrefix;
    }

    /**
     * Busca as roles de acesso a determinado resource
     * @param jwt
     * @return uma colection contendo as roles
     */
    private Set<String> getResourceRoles(Jwt jwt) {
        Set<String> rolesWithPrefix = new HashSet<>();
        Map<String, JsonNode> map = objectMapper.convertValue(jwt.getClaim("resource_access"), new TypeReference<Map<String, JsonNode>>(){});
        for (Map.Entry<String, JsonNode> jsonNode : map.entrySet()) {
            jsonNode
                    .getValue()
                    .elements()
                    .forEachRemaining(e -> e
                            .elements()
                            .forEachRemaining(r -> rolesWithPrefix.add(createRole(jsonNode.getKey(), r.asText()))));
        }
        return rolesWithPrefix;
    }

    private String createRole(String... values) {
        StringBuilder role = new StringBuilder("ROLE");
        for (String value : values) {
            role.append("_").append(value.toUpperCase());
        }
        return role.toString();
    }
}
