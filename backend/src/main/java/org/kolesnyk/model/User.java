package org.kolesnyk.model;

import org.kolesnyk.dto.UserRole;

public interface User {
    long getId();
    void setId(long id);
    String getName();
    void setName(String name);
    String getEmail();
    void setEmail(String email);

    UserRole getRole();
    void setRole(UserRole role);

    String getPassword();
    void setPassword(String password);
}
