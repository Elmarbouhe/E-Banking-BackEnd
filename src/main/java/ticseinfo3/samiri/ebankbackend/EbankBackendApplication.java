package ticseinfo3.samiri.ebankbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ticseinfo3.samiri.ebankbackend.entities.*;
import ticseinfo3.samiri.ebankbackend.enums.AccountStatus;
import ticseinfo3.samiri.ebankbackend.enums.OperationType;
import ticseinfo3.samiri.ebankbackend.repositoies.AccountOperationRepository;
import ticseinfo3.samiri.ebankbackend.repositoies.BankAccountRepository;
import ticseinfo3.samiri.ebankbackend.repositoies.CustomerRepository;
import ticseinfo3.samiri.ebankbackend.services.BankService;

import java.util.Date;

import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class    EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunne(BankService bankService){
        return args -> {
            bankService.cunsult();
        };
    }


    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository ){
        return args -> {
            Stream.of("Yahia","Mohamed","Salah").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount curAc = new CurrentAccount();
                curAc.setId(UUID.randomUUID().toString());
                curAc.setBalance(Math.random()*90000);
                curAc.setCreatedAt(new Date());
                curAc.setStatus(AccountStatus.CREATED);
                curAc.setCustomer(cust);
                curAc.setOverDraft(9000);
                bankAccountRepository.save(curAc) ;

                SavingAccount savAc = new SavingAccount();
                savAc.setId(UUID.randomUUID().toString());
                savAc.setBalance(Math.random()*90000);
                savAc.setCreatedAt(new Date());
                savAc.setStatus(AccountStatus.CREATED);
                savAc.setCustomer(cust);
                savAc.setIntrestRate(5.5);
                bankAccountRepository.save(savAc) ;
            });

            bankAccountRepository.findAll().forEach(acc->{
                for (int i=0;i<5;i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*150000);
                    accountOperation.setType(Math.random()>0.5? OperationType.CREDIT: OperationType.DEBIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };
    }



}
