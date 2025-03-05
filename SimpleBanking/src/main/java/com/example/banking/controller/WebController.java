package com.example.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;

@Controller
public class WebController {

    @Autowired
    private AccountRepository accountRepository;

    // Home page
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("account", new Account()); // For form binding
        return "index";
    }

    // Show account details and operations
    @GetMapping("/account")
    public String getAccount(@RequestParam Long accountNumber, Model model) {
        accountRepository.findById(accountNumber)
                .ifPresentOrElse(
                        account -> model.addAttribute("account", account),
                        () -> model.addAttribute("error", "Account not found"));
        return "account";
    }

    // Create account form submission
    @PostMapping("/account/create")
    public String createAccount(@ModelAttribute Account account, Model model) {
        if (accountRepository.existsById(account.getAccountNumber())) {
            model.addAttribute("error", "Account number already exists");
            return "index";
        }
        accountRepository.save(account);
        model.addAttribute("account", account);
        return "account";
    }

    // Deposit form submission
    @PostMapping("/account/deposit")
    public String deposit(@RequestParam Long accountNumber, @RequestParam double amount, Model model) {
        if (amount <= 0) {
            model.addAttribute("error", "Amount must be positive");
            accountRepository.findById(accountNumber).ifPresent(account -> model.addAttribute("account", account));
            return "account";
        }
        accountRepository.findById(accountNumber)
                .ifPresent(account -> {
                    account.setBalance(account.getBalance() + amount);
                    accountRepository.save(account);
                    model.addAttribute("account", account);
                });
        return "account";
    }

    // Withdraw form submission
    @PostMapping("/account/withdraw")
    public String withdraw(@RequestParam Long accountNumber, @RequestParam double amount, Model model) {
        if (amount <= 0) {
            model.addAttribute("error", "Amount must be positive");
            accountRepository.findById(accountNumber).ifPresent(account -> model.addAttribute("account", account));
            return "account";
        }
        accountRepository.findById(accountNumber)
                .ifPresent(account -> {
                    if (account.getBalance() >= amount) {
                        account.setBalance(account.getBalance() - amount);
                        accountRepository.save(account);
                        model.addAttribute("account", account);
                    } else {
                        model.addAttribute("error", "Insufficient balance");
                        model.addAttribute("account", account);
                    }
                });
        return "account";
    }
}