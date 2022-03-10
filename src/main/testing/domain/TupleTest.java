package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TupleTest {

    Tuple<Integer> integerTuple;
    @BeforeEach
    void setUp() {
        integerTuple = new Tuple<>(1, 2);
    }

    @Test
    void getLeft() {
        assertEquals(integerTuple.getLeft(), 1);
    }

    @Test
    void setLeft() {
        integerTuple.setLeft(3);
        assertEquals(integerTuple.getLeft(), 3);
    }

    @Test
    void getRight() {
        assertEquals(integerTuple.getRight(), 2);
    }

    @Test
    void setRight() {
        integerTuple.setRight(3);
        assertEquals(integerTuple.getRight(), 3);
    }


    @Test
    void testEquals() {
        Tuple<Integer> newTuple = new Tuple<>(2, 1);
        Tuple<Integer> newTuple0 = new Tuple<>(1, 2);
        assertEquals(integerTuple, newTuple);
        assertEquals(integerTuple, newTuple0);
    }

    @Test
    void testHashCode() {
        Tuple<Integer> newTuple = new Tuple<>(2, 1);
        assertEquals(integerTuple.hashCode(), newTuple.hashCode());
    }
}