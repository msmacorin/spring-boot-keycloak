package br.com.macorin.securityapi.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/non-security")
public class NonSecurityResource {
    
    @GetMapping
    public ResponseEntity<Void> nonSecurity() {
        return ResponseEntity.ok().build();
    }
}
