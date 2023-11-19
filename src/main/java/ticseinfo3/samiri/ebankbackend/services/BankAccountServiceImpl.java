package ticseinfo3.samiri.ebankbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticseinfo3.samiri.ebankbackend.entities.*;
import ticseinfo3.samiri.ebankbackend.enums.OperationType;
import ticseinfo3.samiri.ebankbackend.exeptions.BankAccountNotFondException;
import ticseinfo3.samiri.ebankbackend.exeptions.BanlnceNotSufacientException;
import ticseinfo3.samiri.ebankbackend.exeptions.CustomerNotFundException;
import ticseinfo3.samiri.ebankbackend.repositoies.AccountOperationRepository;
import ticseinfo3.samiri.ebankbackend.repositoies.BankAccountRepository;
import ticseinfo3.samiri.ebankbackend.repositoies.CustomerRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private CustomerRepository customerRepo;
    private BankAccountRepository bankAccountRepo ;
    private AccountOperationRepository accountOperationRepo ;




    @Override
    public Customer addCustomer(Customer customer) {
        log.info("Adding new customer : {}",customer);
        Customer addedCustomer = customerRepo.save(customer);
        return addedCustomer;
    }

    @Override
    public Customer getCustomer(Long id) throws CustomerNotFundException {
        Customer getcustomer = customerRepo.findById(id).orElse(null);
        if (getcustomer==null)
            throw new CustomerNotFundException("Customer not fund");
        return getcustomer;
    }

    @Override
    public CurrentAccount addCurrentBankAccount(double initialBalence, double overDraft, Long customerId) throws CustomerNotFundException {
        Customer customer = getCustomer(customerId);
        if(customer ==null)
            throw new CustomerNotFundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalence);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);

        return bankAccountRepo.save(currentAccount);
    }

    @Override
    public SavingAccount addSavingBankAccount(double initialBalence, double intrestRate, Long customerId) throws CustomerNotFundException {
        Customer customer = getCustomer(customerId);
        if (customer== null)
            throw new CustomerNotFundException("custumor not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(intrestRate);
        savingAccount.setIntrestRate(intrestRate);
        savingAccount.setCustomer(customer);

        return bankAccountRepo.save(savingAccount);
    }







    @Override
    public BankAccount addBankAccount(BankAccount bankAccount, Long customerId) {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return null;
    }

    @Override
    public List<BankAccount> getAllBankAccounts() {
        return null;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFondException {
        BankAccount getbankAccount = bankAccountRepo.findById(accountId).orElse(null);
        if (getbankAccount==null)
            throw new BankAccountNotFondException("Bank Account is not fond");
        return getbankAccount;
    }

      @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFondException, BanlnceNotSufacientException {
        BankAccount bankAccount = getBankAccount(accountId);
        if (bankAccount.getBalance()<amount)
            throw new BanlnceNotSufacientException("this account is not Exist");
        else if (bankAccount.getBalance() >= amount) {
           AccountOperation accountOperation = new AccountOperation();
           accountOperation.setType(OperationType.DEBIT);
           accountOperation.setAmount(amount);
           accountOperation.setDescription(description);
           accountOperation.setOperationDate(new Date());
           accountOperation.setBankAccount(bankAccount);
           accountOperationRepo.save(accountOperation);
           bankAccount.setBalance(bankAccount.getBalance()-amount);
           bankAccountRepo.save(bankAccount);


        }
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFondException {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepo.save(bankAccount);

    }

    @Override
    public void transfer(String fromAccountId, String toAccountId, double amount) throws BankAccountNotFondException, BanlnceNotSufacientException {
        debit(fromAccountId,amount,"Transfer to"+toAccountId);
        credit(fromAccountId,amount,"transfer from"+fromAccountId);
    }

    @Override
    public void deleteBankAccount(String accountId) throws BankAccountNotFondException {
        BankAccount bankAccount = getBankAccount(accountId);
        bankAccountRepo.delete(bankAccount);
    }
}
