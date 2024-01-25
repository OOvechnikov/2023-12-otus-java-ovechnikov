package ru.otus.hw02;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode(exclude = {"name", "scores"})
@Getter
@Setter
@ToString
public class Customer implements Comparable<Customer> {

    private long id;
    private String name;
    private long scores;

    public Customer(Customer copyFor) {
        this.id = copyFor.id;
        this.name = copyFor.name;
        this.scores = copyFor.scores;
    }

    @Override
    public int compareTo(Customer customer) {
        if (this.scores < customer.getScores()) {
            return -1;
        } else if (this.scores > customer.getScores()) {
            return 1;
        }
        return 0;
    }

}
