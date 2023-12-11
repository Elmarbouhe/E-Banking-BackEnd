package ticseinfo3.samiri.ebankbackend.services;

import ticseinfo3.samiri.ebankbackend.dto.*;
import ticseinfo3.samiri.ebankbackend.entities.BankAccount;
import ticseinfo3.samiri.ebankbackend.entities.SavingAccount;
import ticseinfo3.samiri.ebankbackend.exeptions.BankAccountNotFondException;
import ticseinfo3.samiri.ebankbackend.exeptions.BanlnceNotSufacientException;
import ticseinfo3.samiri.ebankbackend.exeptions.CustomerNotFundException;

import java.util.List;

public interface BankAccountService {

    CurrentBankAccountDTO addCurrentBankAccount(double initialBalence, double overDraft, Long customerId) throws CustomerNotFundException;
    SavingBankAccountDTO addSavingBankAccount(double initialBalence, double intrestRate , Long customerId) throws CustomerNotFundException;


    List<BankAccountDTO> getAllBankAccounts();

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFondException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFondException, BanlnceNotSufacientException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFondException;
    void transfer(String fromAccountId, String toAccountId, double amount) throws BankAccountNotFondException, BanlnceNotSufacientException;
    void deleteBankAccount(String accountId) throws BankAccountNotFondException;

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFondException;
}
