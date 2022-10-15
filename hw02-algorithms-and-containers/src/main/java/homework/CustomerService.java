package homework;

import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    TreeMap<Customer, String> Customers = new TreeMap<>();

    private Map.Entry<Customer, String> cloneEntry(Map.Entry<Customer, String> originalEntry) {
        if (originalEntry == null) {
            return null;
        }

        Customer customer = originalEntry.getKey();
        return new AbstractMap.SimpleEntry<Customer, String>(
                new Customer(customer.getId(),
                        customer.getName(),
                        customer.getScores()),
                originalEntry.getValue());
    }

    public Map.Entry<Customer, String> getSmallest() {
        return cloneEntry(Customers.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return cloneEntry(Customers.higherEntry(customer));
    }

    public void add(Customer customer, String data) {

        Customers.put(customer, data);
    }
}
