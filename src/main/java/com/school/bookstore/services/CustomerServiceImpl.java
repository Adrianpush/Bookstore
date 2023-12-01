package com.school.bookstore.services;

import com.school.bookstore.exceptions.CustomerCreateException;
import com.school.bookstore.exceptions.CustomerNotFoundException;
import com.school.bookstore.models.dtos.CustomerDTO;
import com.school.bookstore.models.entities.Customer;
import com.school.bookstore.models.entities.Order;
import com.school.bookstore.models.entities.OrderItem;
import com.school.bookstore.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        checkForDuplicate(customerDTO.getEmail());
        Customer customer = customerRepository.save(convertToCustomerEntity(customerDTO));

        return convertToCustomerDTO(customer);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));

        return convertToCustomerDTO(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        customerRepository.findAll().forEach(customer -> customerDTOs.add(convertToCustomerDTO(customer)));

        return customerDTOs;
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
        customerRepository.delete(customer);
    }

    private Customer convertToCustomerEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFullName(customerDTO.getFullName());
        customer.setPassword(customerDTO.getPassword());
        customer.setAddress(customerDTO.getAddress());
        customer.setEmail(customerDTO.getEmail());
        List<OrderItem> shoppingCart = new ArrayList<>();
        customer.setShoppingCart(shoppingCart);
        List<Order> orderHistory = new ArrayList<>();
        customer.setOrderHistory(orderHistory);

        return customer;
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFullName(customer.getFullName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPassword(customer.getPassword());
        customerDTO.setAddress(customer.getAddress());

        return customerDTO;
    }

    private void checkForDuplicate(String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new CustomerCreateException("E-mail already in use");
        }
    }
}

