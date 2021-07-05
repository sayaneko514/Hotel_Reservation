package service;

import model.Customer;

import java.util.*;

public class CustomerService
{
    private final static CustomerService INSTANCE = new CustomerService();
    private CustomerService(){}
    public static CustomerService getInstance()
    {
        return INSTANCE;
    }

    private final Map<String,Customer> customerMap = new HashMap<String,Customer>();

    public void addCustomer(String email, String firstName, String lastName)
    {
        Customer customer = new Customer(firstName,lastName,email);
        customerMap.put(email,customer);
    }

    public Customer getCustomer(String customerEmail)
    {
        return customerMap.get(customerEmail);
    }

    public Collection<Customer> getAllCustomer()
    {
        return customerMap.values();
    }

}