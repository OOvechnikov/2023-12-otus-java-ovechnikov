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
//@RequiredArgsConstructor(onConstructor_ = @PersistenceCreator)
@RequiredArgsConstructor
@Table(name = "phone")
public class Phone implements Persistable<UUID> {

    @JsonIgnore
    @Id
    @Column("client_id")
    private final UUID clientId;

    private final String number;

    @Transient
    private final boolean isNew;

    @PersistenceCreator
    public Phone(UUID clientId, String number) {
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
