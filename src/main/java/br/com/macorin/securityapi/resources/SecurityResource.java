package br.com.macorin.securityapi.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/security")
public class SecurityResource {

    /**
     * endpoint sem nenhuma validação de role
     */
    @GetMapping
    public ResponseEntity<Void> isAuthenticated() {
        return ResponseEntity.ok().build();
    }

    /**
     * endpoint onde o usuario tem que ter a role user
     */
    @GetMapping(value = "/has-role")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Void> isUser() {
        return ResponseEntity.ok().build();
    }

    /**
     * endpoint onde o usuario tem que ter a role admin
     */
    @GetMapping(value = "/is-admin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> isAdmin() {
        return ResponseEntity.ok().build();
    }

}
