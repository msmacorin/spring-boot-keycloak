package br.com.macorin.securityapi.resources;

import org.springframework.http.ResponseEntity;
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

}
