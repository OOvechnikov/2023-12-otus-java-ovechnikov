package ru.otus.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "address")
public class Address implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Expose
    @Column(name = "street")
    private String street;

    public Address(String street) {
        this.street = street;
    }

    @Override
    protected Address clone() {
        return new Address(this.id, this.street);
    }

}
