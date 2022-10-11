package com.decagon.rewardyourteacher.controllers;

import com.decagon.rewardyourteacher.entity.Transaction;
import com.decagon.rewardyourteacher.entity.User;
import com.decagon.rewardyourteacher.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(value="abc", authorities = {"ROLE_STUDENT"})
class TransactionControllerTest {

    @MockBean
    TransactionService  transactionService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getTransactionHistory() throws Exception {

        when(transactionService.transactionHistory(any(String.class))).thenReturn(getTransactionList());

        mockMvc.perform(get("/api/transactions/transaction-history")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()", is(2)))
                .andExpect(jsonPath("$.data[0].message", is("Transaction successful")))
                .andExpect(jsonPath("$.data[0].amount", is(5000)))
                .andExpect(jsonPath("$.data[1].message", is("Transaction was successful")))
                .andExpect(jsonPath("$.data[1].amount", is(7000)));

    }

    private User getUser1(){
        Long id = 1L;

        User user1 = new User();
        user1.setId(id);
        user1.setFirstName("David");
        user1.setLastName("Kingsley");
        user1.setEmail("hello@gmail.com");

        return user1;
    }

    private List<Transaction> getTransactionList(){
        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setUser(getUser1());
        transaction.setAmount(BigDecimal.valueOf(5000));
        transaction.setMessage("Transaction successful");

        Transaction transaction2 = new Transaction();
        transaction2.setUser(getUser1());
        transaction2.setAmount(BigDecimal.valueOf(7000));
        transaction2.setMessage("Transaction was successful");

         transactionList.add(transaction);
         transactionList.add(transaction2);

        return transactionList;

    }
}