package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Builder(toBuilder = true)
@Data
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "client")
public class Client implements Persistable<UUID> {

    @JsonIgnore
    @Id
    private UUID id;

    private final String name;

    @MappedCollection(idColumn = "client_id")
    private final Address address;

    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    @Transient
    private final boolean isNew;

    public Client(String name, Address address, Set<Phone> phones) {
        this(null, name, address, phones, true);
    }

    @PersistenceCreator
    private Client(UUID id, String name, Address address, Set<Phone> phones) {
        this(id, name, address, phones, false);
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return isNew;
    }

}
