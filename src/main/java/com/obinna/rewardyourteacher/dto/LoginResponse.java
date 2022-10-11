package com.decagon.rewardyourteacher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LoginResponse {
    private String username;
    private long userId;
    private String token;
}
