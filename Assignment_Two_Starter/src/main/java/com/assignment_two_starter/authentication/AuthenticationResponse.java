package com.assignment_two_starter.authentication;

import lombok.Data;

/*
This class is used to send the generated JWT back to the client after
successful authentication.
 */
@Data
public class AuthenticationResponse {
    private String jwt;
    private String message;

    public AuthenticationResponse(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;
    }
}