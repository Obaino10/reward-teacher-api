package com.decagon.rewardyourteacher.services.servicesImpl;

import com.decagon.rewardyourteacher.dto.PaymentResponse;
import com.decagon.rewardyourteacher.dto.SenderTransferDto;
import com.decagon.rewardyourteacher.dto.WalletRequest;
import com.decagon.rewardyourteacher.entity.Notification;
import com.decagon.rewardyourteacher.entity.User;
import com.decagon.rewardyourteacher.entity.Wallet;
import com.decagon.rewardyourteacher.enums.TransactionType;
import com.decagon.rewardyourteacher.exceptions.CustomException;
import com.decagon.rewardyourteacher.exceptions.UserNotFoundException;
import com.decagon.rewardyourteacher.exceptions.WalletNotFoundException;
import com.decagon.rewardyourteacher.repository.NotificationRepository;
import com.decagon.rewardyourteacher.repository.UserRepository;
import com.decagon.rewardyourteacher.repository.WalletRepository;
import com.decagon.rewardyourteacher.services.RewardService;
import com.decagon.rewardyourteacher.services.WalletService;
import com.decagon.rewardyourteacher.utils.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RewardServiceImpl implements RewardService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final NotificationRepository notificationRepository;
    private final AuthDetails authDetails;
    private final WalletService walletService;


    @Override
    public PaymentResponse rewardTeacher(Long receiverId, SenderTransferDto senderTransferDto) throws  WalletNotFoundException {
        User sender = authDetails.getAuthorizedUser();
        User receiver = userRepository.findById(receiverId).orElseThrow(UserNotFoundException::new);
        Wallet senderWallet = sender.getWallet();
        Wallet receiverWallet = receiver.getWallet();
        if (receiverWallet == null) {
            receiverWallet =  walletService.createWallet(receiver, "New wallet", "Wallet created due to payment received");
            receiver.setWallet(receiverWallet);
        }
        BigDecimal senderWalletBal = senderWallet.getCurrentBalance();
        BigDecimal receiverWalletBal = receiverWallet.getCurrentBalance();

        PaymentResponse response = new PaymentResponse();
        if (senderTransferDto.getAmountToSend().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException("Amount is not valid");
        } else if (senderWalletBal.compareTo(senderTransferDto.getAmountToSend()) >= 0) {
            senderWallet.setCurrentBalance(senderWalletBal.subtract(senderTransferDto.getAmountToSend()));
            receiverWallet.setCurrentBalance(receiverWalletBal.add(senderTransferDto.getAmountToSend()));
            walletRepository.save(senderWallet);
            walletRepository.save(receiverWallet);

            String message = "You sent money to " + receiver.getFirstName();
            String message2 = sender.getFirstName() + " sent you " + senderTransferDto.getAmountToSend();

            notificationRepository.save(notification(sender, receiverWallet, message, TransactionType.CREDIT));
            notificationRepository.save(notification(receiver, senderWallet, message2, TransactionType.DEBIT));

            response.setMessage("A Reward of " + senderTransferDto.getAmountToSend()
                    + " was sent successfully to " + receiver.getFirstName());
            response.setCurrentBalance(senderWallet.getCurrentBalance());
            return response;
        } else {
            throw new CustomException("Insufficient fund, amount in wallet is " + senderWallet.getCurrentBalance());
        }
    }

    private Notification notification(User user, Wallet receiverWallet, String message, TransactionType transactionType) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUser(user);
        notification.setNotificationBody(receiverWallet.getDescription());
        notification.setTransactionType(transactionType);
        notification.setCreatedAt(LocalDateTime.now());
        return notification;
    }
}








