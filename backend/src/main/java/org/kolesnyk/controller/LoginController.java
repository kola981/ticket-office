package org.kolesnyk.controller;

import org.kolesnyk.dto.LoggedUserDto;
import org.kolesnyk.dto.LoginDto;
import org.kolesnyk.dto.UserRole;
import org.kolesnyk.exceptions.IncorrectLoginException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping(path="login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoggedUserDto> login(@RequestBody LoginDto loginDto) {

        LoggedUserDto userDto = getToken(loginDto);

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(){
        return ResponseEntity.noContent().build();
    }

    private LoggedUserDto getToken(LoginDto loginDto) {
        if ("user123".equals(loginDto.getUsername()) && "password".equals(loginDto.getPassword())) {

                    LoggedUserDto dto =   LoggedUserDto.builder()
                                .name("Andrej")
                                .token("Noop_usertoken")
                                .role(UserRole.USER)
                                .build();
            return dto;

        }
        else if ("admin".equals(loginDto.getUsername()) && "password".equals(loginDto.getPassword())) {
            return LoggedUserDto.builder()
                    .name("Admin")
                    .token("Noop_usertoken")
                    .role(UserRole.ADMIN)
                    .build();
        }
        else throw new IncorrectLoginException("Incorrect username or password");

    }
}
