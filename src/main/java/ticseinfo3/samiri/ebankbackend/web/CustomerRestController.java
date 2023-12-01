package ticseinfo3.samiri.ebankbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ticseinfo3.samiri.ebankbackend.dto.CustomerDTO;
import ticseinfo3.samiri.ebankbackend.exeptions.CustomerNotFundException;
import ticseinfo3.samiri.ebankbackend.services.BankAccountService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> getAllCustomers(){
        return bankAccountService.getAllCustomers();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") long id) throws CustomerNotFundException {
        return bankAccountService.getCustomer(id);
    }

    @PostMapping("/customers")
    public  CustomerDTO saveCustomers(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.addCustomer(customerDTO);
    }

    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable long customerId, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return bankAccountService.updaCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    public String deletCuStomer(@PathVariable long id){
        bankAccountService.deletCustomer(id);
        return "Customer is deleted";
    }

}
