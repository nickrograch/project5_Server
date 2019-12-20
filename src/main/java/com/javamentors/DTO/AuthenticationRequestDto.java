package com.javamentors.DTO;

public class AuthenticationRequestDto {

    private String login;
    private String password;

    public AuthenticationRequestDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getName() {
        return login;
    }

    public void setName(String username) {
        this.login = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
