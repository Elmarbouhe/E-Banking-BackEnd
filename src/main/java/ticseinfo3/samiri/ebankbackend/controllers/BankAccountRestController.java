package ticseinfo3.samiri.ebankbackend.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ticseinfo3.samiri.ebankbackend.dto.AccountHistoryDTO;
import ticseinfo3.samiri.ebankbackend.dto.AccountOperationDTO;
import ticseinfo3.samiri.ebankbackend.dto.BankAccountDTO;
import ticseinfo3.samiri.ebankbackend.exeptions.BankAccountNotFondException;
import ticseinfo3.samiri.ebankbackend.services.BankAccountService;

import java.security.PrivateKey;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/accounts")
public class BankAccountRestController {

    private BankAccountService bankAccountService;



    @GetMapping("/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFondException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/Liste")
    public List<BankAccountDTO> getBankAccounts(){

        return bankAccountService.getAllBankAccounts();
    }

    @GetMapping("/operations/{accountId}")
    public List<AccountOperationDTO> accountHistory(@PathVariable String accountId){
      return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accountHistory/{accountId}")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                               @RequestParam(name = "page" ,defaultValue = "0") int page,
                                               @RequestParam(name = "size" ,defaultValue = "5") int size) throws BankAccountNotFondException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }

}
