package ticseinfo3.samiri.ebankbackend.services;

import ticseinfo3.samiri.ebankbackend.dto.CustomerDTO;
import ticseinfo3.samiri.ebankbackend.entities.BankAccount;
import ticseinfo3.samiri.ebankbackend.entities.CurrentAccount;
import ticseinfo3.samiri.ebankbackend.entities.Customer;
import ticseinfo3.samiri.ebankbackend.entities.SavingAccount;
import ticseinfo3.samiri.ebankbackend.exeptions.BankAccountNotFondException;
import ticseinfo3.samiri.ebankbackend.exeptions.BanlnceNotSufacientException;
import ticseinfo3.samiri.ebankbackend.exeptions.CustomerNotFundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO addCustomer(CustomerDTO customer);

    CustomerDTO updaCustomer(CustomerDTO customerDTO);

    void deletCustomer(long id);


    CurrentAccount addCurrentBankAccount(double initialBalence, double overDraft, Long customerId) throws CustomerNotFundException;
    SavingAccount addSavingBankAccount(double initialBalence, double intrestRate , Long customerId) throws CustomerNotFundException;

    CustomerDTO getCustomer(Long id) throws CustomerNotFundException;

    BankAccount addBankAccount(BankAccount bankAccount, Long customerId);

    List<CustomerDTO> getAllCustomers();

    List<BankAccount> getAllBankAccounts();

    BankAccount getBankAccount(String accountId) throws BankAccountNotFondException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFondException, BanlnceNotSufacientException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFondException;
    void transfer(String fromAccountId, String toAccountId, double amount) throws BankAccountNotFondException, BanlnceNotSufacientException;
    void deleteBankAccount(String accountId) throws BankAccountNotFondException;

}
