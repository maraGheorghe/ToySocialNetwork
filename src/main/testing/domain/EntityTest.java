package domain;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @org.junit.jupiter.api.Test
    void getId() {
        Entity<Integer> integerEntity = new Entity<>();
        integerEntity.setId(1);
        assertEquals(integerEntity.getId(), 1);
    }
}