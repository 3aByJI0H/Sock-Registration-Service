package org.vetirdoit.sock.registration.dtos;

import lombok.Getter;
import org.vetirdoit.sock.registration.dtos.utils.Converter;
import org.vetirdoit.sock.registration.services.SockRegistrationService;

@Getter
public class BiPredicateDto {
    private final SockRegistrationService.BiPredicate operation;

    public BiPredicateDto(String  predicateStr) {
        operation =  SockRegistrationService.BiPredicate.valueOf( Converter.fromCamelToUpperSnakeCase(predicateStr) ) ;
    }
}
