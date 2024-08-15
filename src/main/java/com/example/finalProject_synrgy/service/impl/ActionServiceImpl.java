package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.dto.action.PayQrisReq;
import com.example.finalProject_synrgy.dto.action.TransferReq;
import com.example.finalProject_synrgy.dto.infosaldo.InfoSaldoResponse;
import com.example.finalProject_synrgy.entity.Qris;
import com.example.finalProject_synrgy.entity.Rekening;
import com.example.finalProject_synrgy.entity.Transaction;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;
import com.example.finalProject_synrgy.entity.enums.TransactionReason;
import com.example.finalProject_synrgy.entity.oauth2.User;
import com.example.finalProject_synrgy.repository.*;
import com.example.finalProject_synrgy.service.ActionService;
import com.example.finalProject_synrgy.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ActionServiceImpl implements ActionService {

    @Autowired
    ValidationService validationService;

    @Autowired
    RekeningRepository rekeningRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VendorsRepository vendorsRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    QrisRepository qrisRepository;

    public Object getInfoSaldo(Principal principal) {
        return rekeningRepository.findAllByUser(userRepository.findByUsername(principal.getName()));
    }

    @Transactional
    public Object transfer(Principal principal, TransferReq req) {
        validationService.validate(req);

        Rekening userCard = rekeningRepository.findByRekeningNumber(req.getDebitedRekeningNumber());
        if(userCard == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Debited rekening doesn't exist");

        Rekening targetCard = rekeningRepository.findByRekeningNumber(req.getTargetRekeningNumber());
        if(targetCard == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target rekening doesn't exist");

        User user = userRepository.findByUsername(principal.getName());
        if(!user.getRekenings().contains(userCard)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Debited rekening is not belong to authenticated user");

        if(!Objects.equals(req.getPin(), user.getPin())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong Pin");


        String referenceNumber = UUID.randomUUID().toString();

        int userFinalBalance = userCard.getBalance() - req.getAmount();

        if(userFinalBalance < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough balance on card");

        userCard.setBalance(userFinalBalance);

        List<Transaction> userCardTransactions = userCard.getTransactions();

        if(userCardTransactions == null) userCardTransactions = new ArrayList<>();

        Transaction userTransaction = new Transaction();

        userTransaction.setAmount(req.getAmount());
        userTransaction.setRekening(userCard);
        userTransaction.setIsDebited(true);
        userTransaction.setJenisTransaksi(JenisTransaksi.TRANSAKSI_KELUAR);
        userTransaction.setReason(TransactionReason.TRANSFER);
        userTransaction.setReferenceNumber(referenceNumber);
        userTransaction.setVendors(vendorsRepository.findByVendorName("SATU"));
        userTransaction.setIsInternal(true);
        userTransaction.setBalanceHistory(userFinalBalance);
        userTransaction.setNote(req.getNote());

        userCardTransactions.add(userTransaction);

        rekeningRepository.save(userCard);

        int targetFinalBalance = targetCard.getBalance() + req.getAmount();

        targetCard.setBalance(targetFinalBalance);

        List<Transaction> targetCardTransactions = targetCard.getTransactions();

        if(targetCardTransactions == null) targetCardTransactions = new ArrayList<>();

        Transaction targetTransaction = new Transaction();

        targetTransaction.setAmount(req.getAmount());
        targetTransaction.setRekening(targetCard);
        targetTransaction.setIsDebited(false);
        targetTransaction.setJenisTransaksi(JenisTransaksi.TRANSAKSI_MASUK);
        targetTransaction.setReason(TransactionReason.TRANSFER);
        targetTransaction.setReferenceNumber(referenceNumber);
        targetTransaction.setVendors(vendorsRepository.findByVendorName("SATU"));
        targetTransaction.setIsInternal(true);
        targetTransaction.setBalanceHistory(targetFinalBalance);
        targetTransaction.setNote(req.getNote());

        targetCardTransactions.add(targetTransaction);

        rekeningRepository.save(targetCard);

        return transactionRepository.save(userTransaction);
    }

    @Transactional
    public Object payQris(Principal principal, PayQrisReq req) {
        validationService.validate(req);

        Qris qris = checkQris(req.getTargetQris());

        Rekening userCard = rekeningRepository.findByRekeningNumber(req.getDebitedRekeningNumber());
        if(userCard == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Debited rekening doesn't exist");

        User user = userRepository.findByUsername(principal.getName());
        if(!user.getRekenings().contains(userCard)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Debited rekening is not belong to authenticated user");

        if(!Objects.equals(req.getPin(), user.getPin())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong Pin");

        String referenceNumber = UUID.randomUUID().toString();

        userCard.setBalance(userCard.getBalance() - req.getAmount());

        List<Transaction> userCardTransactions = userCard.getTransactions();

        if(userCardTransactions == null) userCardTransactions = new ArrayList<>();

        Transaction userTransaction = new Transaction();

        userTransaction.setAmount(req.getAmount());
        userTransaction.setRekening(userCard);
        userTransaction.setIsDebited(true);
        userTransaction.setJenisTransaksi(JenisTransaksi.TRANSAKSI_KELUAR);
        userTransaction.setReason(TransactionReason.PEMBAYARAN);
        userTransaction.setReferenceNumber(referenceNumber);
        userTransaction.setVendors(vendorsRepository.findByVendorName("QRIS"));
        userTransaction.setIsInternal(false);
        userTransaction.setNote("-");

        qris.setBalance(qris.getBalance() + req.getAmount());

        userCardTransactions.add(userTransaction);

        rekeningRepository.save(userCard);

        return transactionRepository.save(userTransaction);
    }

    public Qris checkQris(String targetQris) {
        String keyword = "ID.CO.QRIS.WWW";

        if(!targetQris.contains(keyword))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid qris code, make sure qrcode encoded properly");

        Qris qris = qrisRepository.findByEncodedQrCode(targetQris);

        int index = targetQris.indexOf(keyword);

        String result = targetQris.substring(index + 51 + keyword.length());
        result = result.replaceAll("\\d", " ").replaceAll("\\s+", " ");
        String[] words = result.split(" ");
        String[] firstThreeWords = Arrays.copyOfRange(words, 0, Math.min(3, words.length));
        result = String.join(" ", firstThreeWords);

        log.info(targetQris);
        log.info(result);

        if(qris == null) {
            qris = new Qris();
            qris.setName(result);
            qris.setEncodedQrCode(targetQris);
            qris.setBalance(0);
            qris = qrisRepository.save(qris);
        }

        return qris;
    }
}
