package ticseinfo3.samiri.ebankbackend.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ticseinfo3.samiri.ebankbackend.dto.AccountHistoryDTO;
import ticseinfo3.samiri.ebankbackend.dto.AccountOperationDTO;
import ticseinfo3.samiri.ebankbackend.dto.BankAccountDTO;
import ticseinfo3.samiri.ebankbackend.exeptions.BankAccountNotFondException;
import ticseinfo3.samiri.ebankbackend.exeptions.BanlnceNotSufacientException;
import ticseinfo3.samiri.ebankbackend.services.AccountOperationsService;
import ticseinfo3.samiri.ebankbackend.services.BankAccountService;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/operations")
public class AccountOperationsController {

    private AccountOperationsService accountOperationsService;

    @PostMapping("/debit/{accountId}/{amount}/{description}")
    public void debit(@PathVariable String accountId,
                      @PathVariable double amount,
                      @PathVariable String description) throws BankAccountNotFondException, BanlnceNotSufacientException{

        accountOperationsService.debit(accountId,amount,description);
    }

    @PostMapping("/credit/{accountId}/{amount}/{description}")
    public void credit(@PathVariable String accountId,
                       @PathVariable double amount,
                       @PathVariable String description) throws BankAccountNotFondException{

        accountOperationsService.credit(accountId,amount,description);
    }

    @PostMapping("/transfer/{fromAccountId}/{toAccountId}/{amount}")
    public void transfer(@PathVariable String fromAccountId,
                         @PathVariable String toAccountId,
                         @PathVariable double amount) throws BankAccountNotFondException, BanlnceNotSufacientException{

        accountOperationsService.transfer(fromAccountId,toAccountId,amount);
    }


    @GetMapping("/operations/{accountId}")
    public List<AccountOperationDTO> accountHistory(@PathVariable String accountId){
        return accountOperationsService.accountHistory(accountId);
    }

    @GetMapping("/accountHistory/{accountId}")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                               @RequestParam(name = "page" ,defaultValue = "0") int page,
                                               @RequestParam(name = "size" ,defaultValue = "5") int size) throws BankAccountNotFondException {
        return accountOperationsService.getAccountHistory(accountId,page,size);
    }

}
