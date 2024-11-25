package com.estsoft.springproject.tdd;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// TDD
// 1. 계좌 생성
// 2. 잔금 조회
// 3. 입/출금
public class AccountTest {
	Account account;

	@BeforeEach
	public void setUp() {
		account = new Account(10000);
	}

	@Test
	public void testAccount() {
		assertThat(account.getBalance(), is(10000));    // hamcrest로 검증

		account = new Account(20000);
		assertThat(account.getBalance(), is(20000));

		account = new Account(500000);
		assertThat(account.getBalance(), is(500000));
	}

	@Test
	public void testDeposit() {
		account.deposit(100000);
		assertThat(account.getBalance(), is(110000));
	}

	@Test
	public void testWithdrawal() {
		account.withdraw(10000);
		assertThat(account.getBalance(), is(0));
	}
}
