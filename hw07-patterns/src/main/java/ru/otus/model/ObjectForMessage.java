package ru.otus.model;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ObjectForMessage implements Cloneable {

    private List<String> data;

    @Override
    protected ObjectForMessage clone() throws CloneNotSupportedException {
        val clone = new ObjectForMessage();
        clone.setData(new ArrayList<>(data));
        return clone;
    }

}
