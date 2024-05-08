package ru.otus.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "phone")
public class Phone implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @ManyToOne
    private Client client;
    @Expose
    @Column(name = "number")
    private String number;

    public Phone(String number) {
        this.number = number;
    }

    private Phone(UUID id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    protected Phone clone() {
        return new Phone(this.id, this.number);
    }

}
