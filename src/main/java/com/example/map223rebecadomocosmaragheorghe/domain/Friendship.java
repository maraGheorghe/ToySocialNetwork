package com.example.map223rebecadomocosmaragheorghe.domain;
import com.example.map223rebecadomocosmaragheorghe.utils.constants.Constants;
import java.time.LocalDate;

public class Friendship extends Entity<Tuple<Long>> {
    LocalDate date;
    Long requestID;

    /**Class constructor for a friendship.
     * @param user1 a Long representing the first friend of the friendship.
     * @param user2 a Long representing the second friend of the friendship.
     * @param requestID a Long representing the id of the request.
     */
    public Friendship(Long user1, Long user2, Long requestID) {
        setId(new Tuple<>(user1, user2));
        this.requestID = requestID;
        this.date = LocalDate.now();
    }

    /**Class constructor for a friendship.
     * @param user1 a Long representing the first friend of the friendship.
     * @param user2 a Long representing the second friend of the friendship.
     * @param date a LocalDate representing the date of the friendship.
     * @param requestID a Long representing the id of the request.
     */
    public Friendship(Long user1, Long user2, LocalDate date, Long requestID) {
        setId(new Tuple<>(user1, user2));
        this.date = date;
        this.requestID = requestID;
    }

    public Long getRequestID() {
        return requestID;
    }

    /**Gets the date when the friendship was created.
     * @return a LocalDate representing the date when the friendship was created.
     */
    public LocalDate getDate() {
        return date;
    }

    /**Sets the friendship's date.
     * @param date a LocalDate representing the date when the friendship was created.
     */
    public void setDate(LocalDate date){
        this.date = date;
    }

    @Override
    public String toString() {
        return super.getId().getLeft() + " and " + super.getId().getRight() + "\n"
                + "FROM: " + getDate().format(Constants.DATE_FORMATTER) + "\n";
    }
}
