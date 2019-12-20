package com.piggymetrics.account.controller;

import com.piggymetrics.account.domain.Account;
import com.piggymetrics.account.domain.User;
import com.piggymetrics.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@RequestMapping(path = "/{name}", method = RequestMethod.GET)
	public Account getAccountByName(@PathVariable String name) {
		return accountService.findByName(name);
	}

	@RequestMapping(path = "/current", method = RequestMethod.GET)
	public Account getCurrentAccount(@RequestParam("username") String username) {
		return accountService.findByName(username);
	}

	@RequestMapping(path = "/current", method = RequestMethod.PUT)
	public void saveCurrentAccount(@RequestParam("username") String username, @Valid @RequestBody Account account) {
		accountService.saveChanges(username, account);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Account createNewAccount(@Valid @RequestBody User user) {

		return accountService.create(user);
	}
}
