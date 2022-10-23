package homework;


import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private final Deque<Customer> Customers = new ArrayDeque<>();
    public void add(Customer customer) {
        Customers.addLast(customer);
    }

    public Customer take() {
        return Customers.removeLast();
    }
}
