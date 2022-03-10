package com.example.map223rebecadomocosmaragheorghe.domain;

public class Notification {

    private NotificationType type;
    private Entity<?> entity;

    public Notification(NotificationType type, Entity<?> entity) {
        this.type = type;
        this.entity = entity;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Entity<?> getEntity() {
        return entity;
    }

    public void setEntity(Entity<?> entity) {
        this.entity = entity;
    }
}
