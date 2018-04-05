package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.NegativeAmountException;
import com.db.awmd.challenge.exception.NegativeBalanceException;
import com.db.awmd.challenge.exception.NonexistentAccountException;
import com.db.awmd.challenge.exception.SameAccountException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  @Autowired
  private NotificationService notificationService;

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  public void  transferAmountBetweenAccounts(String fromAccountId, String toAccountId, BigDecimal amount)
          throws NonexistentAccountException, NegativeAmountException, SameAccountException, NegativeBalanceException{
    Account fromAccount = this.accountsRepository.getAccount(fromAccountId);
    Account toAccount =  this.accountsRepository.getAccount(toAccountId);

    //Checks if accounts exist. Else, an exception will be returned
    if ((fromAccount == null) || (toAccount == null)) {
      throw new NonexistentAccountException();
      //Checks if amount specified is greater than zero. Else, an exception will be returned
    } else if (amount.signum() < 0) {
      throw new NegativeAmountException();
      //Checks if accounts specified are equal. If so, an exception will be returned
    } else if (fromAccount.equals(toAccount)) {
      throw new SameAccountException();
    }


    List<Account> accountsList = new ArrayList<>();
    accountsList.add(fromAccount);
    accountsList.add(toAccount);

    accountsList.sort((a, b) -> a.getAccountId().compareTo(b.getAccountId()));

    synchronized (accountsList.get(0)) {
      synchronized (accountsList.get(1)) {
        //Checks the balance which source account will have after transaction. amount can't be negative as overdraft is not allowed
        if (fromAccount.getBalance().subtract(amount).signum() < 0) {
          throw new NegativeBalanceException(fromAccount.getAccountId());
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        //Send notifications to accounts owners
        notificationService.notifyAboutTransfer(fromAccount, "$ " + amount.toString() + " were transferred from your account to account "
                + toAccount.getAccountId() + ". Now you have a balance of $ " + fromAccount.getBalance().toString());
        notificationService.notifyAboutTransfer(toAccount, "$ " + amount.toString() + " were transferred to your account from account "
                + fromAccount.getAccountId() + ". Now you have a balance of $ " + toAccount.getBalance().toString());
      }
    }
  }

}
