package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Client {

    @Id
    private Long id;
    private String name;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

}
