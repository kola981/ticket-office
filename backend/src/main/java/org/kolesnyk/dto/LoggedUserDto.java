package org.kolesnyk.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoggedUserDto {
    private String name;
    private UserRole role;
    private String token;
}