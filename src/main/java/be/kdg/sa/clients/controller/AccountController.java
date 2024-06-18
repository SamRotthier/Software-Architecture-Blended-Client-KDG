package be.kdg.sa.clients.controller;

import be.kdg.sa.clients.controller.dto.AccountDto;
import be.kdg.sa.clients.controller.dto.LoyaltyDto;
import be.kdg.sa.clients.domain.Account;
import be.kdg.sa.clients.domain.Enum.OrderStatus;
import be.kdg.sa.clients.domain.Order;
import be.kdg.sa.clients.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/* mport org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult; */
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountDto accountDto){
       accountService.createAccount(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account was successfully created");
    }

    // @PreAuthorize("hasRole")
    @DeleteMapping("/{accountId}/delete")
    public ResponseEntity<?> deleteAccount(@PathVariable ("accountId") UUID accountId) {
        if(accountId == null || !accountService.exists(accountId)){
            return ResponseEntity.badRequest().body("Invalid request body");
        }
        accountService.deleteAccount(accountId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Account was successfully deleted");
    }

    @GetMapping("/{lastName}")
    public ResponseEntity<?> getAccountByLastName(@PathVariable String lastName){
        Optional<Account> accountOptional = accountService.getAccountByLastName(lastName);
        if(accountOptional.isPresent()){
            return ResponseEntity.ok(accountOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{accountId}/history")
    public ResponseEntity<?> getHistoryByAccountId(
            @PathVariable ("accountId") UUID accountId,
            @RequestParam(value = "Status", required = false) OrderStatus status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate){
        if(accountId == null || !accountService.exists(accountId)){
            return ResponseEntity.badRequest().body("Invalid request body");
        }
        List<Order> history = accountService.getAccountHistory(accountId, status, startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }


    @GetMapping("/{accountId}/loyalty")
    public ResponseEntity<?> getLoyaltyByAccountId(
            @PathVariable("accountId") UUID accountId){
        LoyaltyDto loyaltyDto = accountService.getLoyaltyByAccountId(accountId);
        if(loyaltyDto != null){
            return ResponseEntity.status(HttpStatus.OK).body(accountId);
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for ID: " +accountId);
        }
    }


}
