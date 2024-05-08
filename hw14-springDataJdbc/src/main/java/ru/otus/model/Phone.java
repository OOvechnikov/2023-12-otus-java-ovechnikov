package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "phone")
public class Phone implements Persistable<UUID> {

    @JsonIgnore
    @Id
    @Column("client_id")
    private UUID clientId;

    private final String number;

    @Transient
    private final boolean isNew;

    public Phone(String number) {
        this(null, number, false);
    }

    @PersistenceCreator
    private Phone(UUID clientId, String number) {
        this(clientId, number, false);
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
