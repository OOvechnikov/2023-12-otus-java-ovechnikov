package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Manager {

    @Id
    private Long no;
    private String label;
    private String param1;

    public Manager(String label) {
        this.label = label;
    }

}
