package com.example.map223rebecadomocosmaragheorghe.domain;

import java.util.Objects;

/**Define a Tuple of generic type entities
 * @param <E> - tuple first entity type
 */
public class Tuple<E extends Comparable<E>> {
    private E element1;
    private E element2;

    /**Class constructor.
     * @param element1 E representing the first element of the Tuple.
     * @param element2 E representing the second element of the Tuple.
     */
    public Tuple(E element1, E element2) {
        this.element1 = element1;
        this.element2 = element2;
    }

    /**Gets the first element of the tuple.
     * @return E representing the first element of the tuple.
     */
    public E getLeft() {
        return element1;
    }

    /**Sets the first element of the tuple.
     * @param element1 E representing the first element of the tuple.
     */
    public void setLeft(E element1) {
        this.element1 = element1;
    }

    /**Gets the second element of the tuple.
     * @return E representing the second element of the tuple.
     */
    public E getRight() {
        return element2;
    }

    /**Sets the second element of the tuple.
     * @param element2 E representing the second element of the tuple.
     */
    public void setRight(E element2) {
        this.element2 = element2;
    }

    @Override
    public boolean equals(Object obj) {
        return this.element1.equals(((Tuple) obj).element1) && this.element2.equals(((Tuple) obj).element2)
                || this.element1.equals(((Tuple)obj).element2) && this.element2.equals(((Tuple)obj).element1);
    }

    @Override
    public int hashCode() {
        return this.element1.compareTo(this.element2) < 0 ? Objects.hash(element1, element2) : Objects.hash(element2, element1);
    }
}