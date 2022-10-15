package homework;


import java.util.LinkedList;

public class CustomerReverseOrder {

    LinkedList<Customer> Customers = new LinkedList<>();
    public void add(Customer customer) {
        Customers.addLast(customer);
    }

    public Customer take() {
        return Customers.removeLast();
    }
}
