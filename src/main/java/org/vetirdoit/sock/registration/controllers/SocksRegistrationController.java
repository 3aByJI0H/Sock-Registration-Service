package org.vetirdoit.sock.registration.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vetirdoit.sock.registration.dtos.BiPredicateDto;
import org.vetirdoit.sock.registration.dtos.ColorDto;
import org.vetirdoit.sock.registration.dtos.SockTypeDto;
import org.vetirdoit.sock.registration.dtos.utils.Converter;
import org.vetirdoit.sock.registration.services.SockRegistrationService;
import org.vetirdoit.sock.registration.services.exceptions.InvalidOperationException;

@RestController
@RequestMapping("/api/socks")
public class SocksRegistrationController {
    private final SockRegistrationService sockRegistrationService;

    @Autowired
    public SocksRegistrationController(SockRegistrationService sockRegistrationService) {
        this.sockRegistrationService = sockRegistrationService;
    }

    @PostMapping("/income")
    public void registerIncomingSocks(@RequestBody SockTypeDto sockTypeDto) {

        sockRegistrationService.registerIncomingSocks( Converter.toSockType(sockTypeDto) );
    }

    @PostMapping("/outcome")
    public void registerOutgoingSocks(@RequestBody SockTypeDto sockTypeDto) {

        try {
            sockRegistrationService.registerOutgoingSocks( Converter.toSockType(sockTypeDto) );
        } catch (InvalidOperationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping
    public long getAllRequiredSocks(@RequestParam ColorDto color,
                                    @RequestParam BiPredicateDto operation,
                                    @RequestParam int cottonPart) {

        return sockRegistrationService.countRequiredSocks(color.getColor(), operation.getOperation(), cottonPart);
    }
}
