package org.kolesnyk.model;

public interface User {
    long getId();
    void setId(long id);
    String getName();
    void setName(String name);
    String getEmail();
    void setEmail(String email);
}
