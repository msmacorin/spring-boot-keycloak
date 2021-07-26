package br.com.macorin.securityapi.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/security")
public class SecurityResource {

    @GetMapping
    public ResponseEntity<Void> security() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/has-role")
    @PreAuthorize(value = "hasRole('ROLE_USER')")
    public ResponseEntity<Void> securityHasRole() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/has-authority")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Void> securityHasAuthority() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/has-other-authority")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Void> securityOtherHasAuthority() {
        return ResponseEntity.ok().build();
    }

}
