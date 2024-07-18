package ru.otus.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;
import reactor.util.annotation.NonNull;

@AllArgsConstructor(onConstructor_ = {@PersistenceCreator})
@Getter
@Table("message")
@ToString
public class Message {

    @Id
    private final Long id;

    @NonNull
    private final String roomId;

    @NonNull
    private final String msgText;

    public Message(@NonNull String roomId, @NonNull String msgText) {
        this(null, roomId, msgText);
    }

}
