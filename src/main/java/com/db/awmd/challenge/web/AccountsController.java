package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.web.dto.AccountTransferReceiptDTO;
import com.db.awmd.challenge.exception.*;
import com.db.awmd.challenge.service.AccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;

  @Autowired
  public AccountsController(AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }

  @PostMapping(path = "/accountsTransfer")
  public ResponseEntity<Object> transferAmountBetweenAccounts(@RequestBody @Valid AccountTransferReceiptDTO accountTransferDTO) {
    log.info("Transferring amount from account {} to account {}", accountTransferDTO.getFromAccountId(), accountTransferDTO.getToAcountId());

    try {
      this.accountsService.transferAmountBetweenAccounts(accountTransferDTO.getFromAccountId(), accountTransferDTO.getToAcountId(), accountTransferDTO.getAmount());
    } catch (NonexistentAccountException | NegativeAmountException | SameAccountException | NegativeBalanceException e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

}
