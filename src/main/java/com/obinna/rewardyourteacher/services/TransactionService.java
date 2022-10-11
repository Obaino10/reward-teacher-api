package com.decagon.rewardyourteacher.services;

import com.decagon.rewardyourteacher.entity.Transaction;
import com.decagon.rewardyourteacher.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    List<Transaction> transactionHistory(String email);
    Transaction saveTransaction(Transaction transaction);
    Transaction saveTransaction(User user, BigDecimal amount, String description);
}
