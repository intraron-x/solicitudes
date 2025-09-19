// Archivo: src/main/java/com/intraron/api/security/CustomReactiveUserDetailsService.java
package com.intraron.api.security;

import com.intraron.consumer.UserRestConsumer;
import com.intraron.model.loan.dto.UserLoanDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    // intraron: Se inyecta el consumidor REST ya existente.
    private final UserRestConsumer userRestConsumer;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        // intraron: Se usa el consumidor para llamar a la API del otro microservicio.
        return userRestConsumer.findUserByEmail(email)
                .doOnNext(userDTO -> log.info("UserLoanDTO recibido del microservicio de autenticación: Email={}, Roles={}", userDTO.getCorreoElectronico(), userDTO.getRoles()))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Usuario no encontrado: " + email)))
                .map(this::mapUserToUserDetails);
    }

    private UserDetails mapUserToUserDetails(UserLoanDTO userDTO) {

        Set<String> roles = Optional.ofNullable(userDTO.getRoles()).orElse(Collections.emptySet());

        log.info("Los roles que trae el token es: {}.", roles);

        // intraron: Se crea un objeto UserDetails a partir del DTO para Spring Security.
        return new org.springframework.security.core.userdetails.User(
                userDTO.getCorreoElectronico(),
                // intraron: La contraseña no es necesaria para la validación del JWT, por lo que se deja vacía.
                "",
                roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList())
        );
    }
}