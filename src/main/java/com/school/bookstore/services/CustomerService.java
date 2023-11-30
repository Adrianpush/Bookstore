package com.school.bookstore.services;

import com.school.bookstore.models.dtos.CustomerDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO createCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomerById(Long id);
    List<CustomerDTO> getAllCustomers();
    void deleteCustomer(Long id);
}
