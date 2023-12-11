package ticseinfo3.samiri.ebankbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticseinfo3.samiri.ebankbackend.dto.*;
import ticseinfo3.samiri.ebankbackend.entities.*;
import ticseinfo3.samiri.ebankbackend.enums.OperationType;
import ticseinfo3.samiri.ebankbackend.exeptions.BankAccountNotFondException;
import ticseinfo3.samiri.ebankbackend.exeptions.BanlnceNotSufacientException;
import ticseinfo3.samiri.ebankbackend.exeptions.CustomerNotFundException;
import ticseinfo3.samiri.ebankbackend.mappers.BankAccountMapperImpl;
import ticseinfo3.samiri.ebankbackend.mappers.CustomerMapperImpl;
import ticseinfo3.samiri.ebankbackend.repositoies.AccountOperationRepository;
import ticseinfo3.samiri.ebankbackend.repositoies.BankAccountRepository;
import ticseinfo3.samiri.ebankbackend.repositoies.CustomerRepository;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private CustomerRepository customerRepo;
    private BankAccountRepository bankAccountRepo ;
    private AccountOperationRepository accountOperationRepo ;
    private CustomerMapperImpl dtoMapper;
    private BankAccountMapperImpl bankAccountMapper;
    private CustomerService customer ;


    private BankAccount findBankAccount(String accountId) throws BankAccountNotFondException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElse(null);
        if (bankAccount == null) {
            throw new BankAccountNotFondException("Bank Account is not fond");
        }
        return bankAccount;
    }

    @Override
    public CurrentBankAccountDTO addCurrentBankAccount(double initialBalence, double overDraft, Long customerId) throws CustomerNotFundException {

        Customer customer =  customerRepo.findById(customerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalence);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount saveCurrentAccount = bankAccountRepo.save(currentAccount);

        return bankAccountMapper.fromCurrentAccToCurrentAccDTO(saveCurrentAccount);
    }

    @Override
    public SavingBankAccountDTO addSavingBankAccount(double initialBalence, double intrestRate, Long customerId) throws CustomerNotFundException {
        CustomerDTO customerDTO = customer.getCustomer(customerId);
        Customer customer = dtoMapper.fromCustomerDtoToCustomer(customerDTO);
        if (customer== null)
            throw new CustomerNotFundException("custumor not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(intrestRate);
        savingAccount.setIntrestRate(intrestRate);
        savingAccount.setCustomer(customer);
        SavingAccount saveSavingAccount = bankAccountRepo.save(savingAccount);
        return bankAccountMapper.fromSavingAccToSavingDTO(saveSavingAccount);
    }

    @Override
    public List<BankAccountDTO> getAllBankAccounts() {
        List<BankAccount> bankAccountList = bankAccountRepo.findAll();
       List<BankAccountDTO> bankAccountDTOS = bankAccountList.stream().map(bankAccount -> {
            if(bankAccount instanceof SavingAccount){
                SavingAccount savingAccount = (SavingAccount) bankAccount ;
                return bankAccountMapper.fromSavingAccToSavingDTO(savingAccount);
            }else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentAccToCurrentAccDTO(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFondException {
        BankAccount bankAccount = this.findBankAccount(accountId);
        if (bankAccount instanceof SavingAccount ){
            SavingAccount savingAccount = (SavingAccount) bankAccount ;
            return bankAccountMapper.fromSavingAccToSavingDTO(savingAccount);
        }else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount ;
            return bankAccountMapper.fromCurrentAccToCurrentAccDTO(currentAccount);
        }
    }

      @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFondException, BanlnceNotSufacientException {
        BankAccount bankAccount = this.findBankAccount(accountId);
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
        BankAccount bankAccount = this.findBankAccount(accountId);
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
        BankAccount bankAccount = this.findBankAccount(accountId);
        bankAccountRepo.delete(bankAccount);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
       List<AccountOperation> accountOperations =accountOperationRepo.findByBankAccountId(accountId);
      return   accountOperations.stream().map(op->
               bankAccountMapper.fromAccountOperationToAccountOperationDTO(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFondException {
        BankAccount account = this.findBankAccount(accountId);
        Page<AccountOperation> accountOperations = accountOperationRepo.findByBankAccountId(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op-> bankAccountMapper.fromAccountOperationToAccountOperationDTO(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(account.getId());
        accountHistoryDTO.setBalance(account.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return  accountHistoryDTO;
    }
}
