package com.codewithme.store.dtos;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtResponse {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
