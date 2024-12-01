package com.assignment_two_starter.model.services;

import com.assignment_two_starter.model.entities.Role;
import com.assignment_two_starter.model.repositories.CustomerRepository;
import com.assignment_two_starter.model.entities.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer getCustomerById(Integer id) {
        Optional<Customer> c = customerRepository.findById(id);
        if (c.isPresent())
            return c.get();
        else
            return null;
    }

    public Customer getCustomerByEmail(String email) {
        Optional<Customer> c = customerRepository.findByEmail(email);
        if (c.isPresent())
            return c.get();
        else
            return null;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> getSuspendedCustomers() {
        Optional<List<Customer>> suspendedCustomers = customerRepository.findByLockedTrue();

        if (suspendedCustomers.isPresent()) {
            return suspendedCustomers.get();
        } else {
            return null;
        }
    }

    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }

    public Customer registerCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());
        customer.setRoles(new HashSet<>(Collections.singletonList(new Role(1L, "CUSTOMER"))));
        return customerRepository.save(customer);
    }

    public boolean emailExists(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void suspendAccount(Integer userId) {
        Optional<Customer> customer = customerRepository.findById(userId);
        if (customer.isPresent()) {
            customer.get().setLocked(true);
            customerRepository.save(customer.get());
        }
    }

    @Transactional
    public void activateAccount(Integer userId) {
        Optional<Customer> customer = customerRepository.findById(userId);
        if(customer.isPresent()){
            customer.get().setLocked(false);
            customerRepository.save(customer.get());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        System.out.println("Customer Email: " + customer.getEmail());

        List<GrantedAuthority> authorities = customer.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        System.out.println("Granted authorities: " + authorities);

        return new org.springframework.security.core.userdetails.User(
                customer.getEmail(),
                customer.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }

    /*
    This method converts a set of Role objects into a collection of GrantedAuthority
    objects prefixed with "ROLE_" for use in authorisation.
    */
    public Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return authorities;
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}

