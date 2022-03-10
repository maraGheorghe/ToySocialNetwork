package com.example.map223rebecadomocosmaragheorghe.domain;
import java.io.Serializable;
import java.util.Objects;

public class Entity<ID> implements Serializable {

    /**The ID of the entity.
     */
    private ID id;

    /**Gets the entity's ID.
     * @return an ID representing the ID of the entity.
     */
    public ID getId() {
        return id;
    }

    /**Sets the entity's ID.
     * @param id an ID representing the ID of the entity.
     */
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
