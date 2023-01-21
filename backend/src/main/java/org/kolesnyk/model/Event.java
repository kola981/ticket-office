package org.kolesnyk.model;

import java.time.LocalDateTime;

public interface Event {
    long getId();
    void setId(long id);
    String getTitle();
    void setTitle(String title);
    LocalDateTime getDate();
    void setDate(LocalDateTime date);
}
