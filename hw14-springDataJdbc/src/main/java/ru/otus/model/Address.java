package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "address")
public class Address implements Persistable<UUID> {

    @JsonIgnore
    @Id
    @Column("client_id")
    private UUID clientId;

    private final String street;

    @Transient
    private final boolean isNew;

    public Address(String street) {
        this(null, street, false);
    }

    @PersistenceCreator
    private Address(UUID clientId, String street) {
        this(clientId, street, false);
    }

    @JsonIgnore
    @Override
    public UUID getId() {
        return getClientId();
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return isNew;
    }

}
