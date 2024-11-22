package com.assignment_two_starter.authentication;

import lombok.Data;

/*
This class is used to send the generated JWT back to the client after
successful authentication.
 */
@Data
public class AuthenticationResponse {
    private final String jwt;
}