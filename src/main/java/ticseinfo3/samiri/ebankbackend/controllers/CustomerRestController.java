package ticseinfo3.samiri.ebankbackend.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ticseinfo3.samiri.ebankbackend.dto.CustomerDTO;
import ticseinfo3.samiri.ebankbackend.exeptions.CustomerNotFundException;
import ticseinfo3.samiri.ebankbackend.services.CustomerService;

import java.util.List;
@CrossOrigin("*")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/customers")
public class CustomerRestController {

    private CustomerService customerService;

    @PostMapping("/create")
    public  CustomerDTO saveCustomers(@RequestBody CustomerDTO customerDTO){
        return customerService.addCustomer(customerDTO);
    }

    @GetMapping("/get/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") long id) throws CustomerNotFundException {
        return customerService.getCustomer(id);
    }

    @GetMapping("/list")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/searchByName")
    public List<CustomerDTO> searchCustomersByName(@RequestParam(name = "name") String name){
        return customerService.searchCustomersByName(name);
    }

    @GetMapping("/searchByEmail")
    public List<CustomerDTO> searchCustomersByEmail(@RequestParam(name = "email") String email){
        return customerService.searchCustomersByEmail(email);
    }

    @PutMapping("/update/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable long customerId, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return customerService.updaCustomer(customerDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCustomer(@PathVariable long id){
        customerService.deletCustomer(id);
    }

}
