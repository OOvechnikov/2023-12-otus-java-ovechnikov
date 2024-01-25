package ru.otus.hw02;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> map = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> firstEntry = map.firstEntry();
        return getEntryCopy(firstEntry);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = map.higherEntry(customer);
        return getEntryCopy(higherEntry);
    }

    private Map.Entry<Customer, String> getEntryCopy(Map.Entry<Customer, String> entry) {
        if (entry == null) {
            return null;
        }
        Customer copyKey = new Customer(entry.getKey());
        String value = entry.getValue();
        return Map.entry(copyKey, value);
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }

}
