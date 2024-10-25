package com.assignment_two_starter.model.services;

import com.assignment_two_starter.model.repository.CustomerRepository;
import com.assignment_two_starter.model.entities.Customer;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerById(Integer id) {
        Optional<Customer> c = customerRepository.findById(id);
        if(c.isPresent())
            return c.get();
        else
            return null;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void createCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }
}

