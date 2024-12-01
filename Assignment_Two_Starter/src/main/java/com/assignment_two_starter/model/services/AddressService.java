package com.assignment_two_starter.model.services;

import com.assignment_two_starter.model.entities.Address;
import com.assignment_two_starter.model.repositories.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address saveAddress(Address address) {
        address.setCreatedAt(new Date());
        address.setUpdatedAt(new Date());
        return addressRepository.save(address);
    }

    public List<Address> getAddressesByUserId(Integer userId) {
        Optional<List<Address>> c = addressRepository.findByUserId_UserId(userId);

        if (c.isPresent()) {
            return c.get();
        } else {
            return null;
        }
    }

    public Address updateAddress(Address address) {
        address.setUpdatedAt(new Date());
        return addressRepository.save(address);
    }

    public Address getAddressById(Integer addressId) {
        Optional<Address> c = addressRepository.findById(addressId);

        if (c.isPresent()) {
            return c.get();
        } else {
            return null;
        }
    }

    public void deleteAddress(Integer addressId) {
        addressRepository.deleteById(addressId);
    }
}
