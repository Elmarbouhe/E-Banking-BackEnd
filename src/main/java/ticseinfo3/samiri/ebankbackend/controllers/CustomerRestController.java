package ticseinfo3.samiri.ebankbackend.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ticseinfo3.samiri.ebankbackend.dto.CustomerDTO;
import ticseinfo3.samiri.ebankbackend.exeptions.CustomerNotFundException;
import ticseinfo3.samiri.ebankbackend.services.CustomerService;

import java.util.List;

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

    @GetMapping("/getAll")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers();
    }



    @PutMapping("/update/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable long customerId, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return customerService.updaCustomer(customerDTO);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable long id){
        customerService.deletCustomer(id);
        return "Customer is deleted";
    }

}
