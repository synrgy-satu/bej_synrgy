package com.example.finalProject_synrgy.service.impl;

import com.example.finalProject_synrgy.dto.action.AddCardReq;
import com.example.finalProject_synrgy.dto.action.BalanceTotalRes;
import com.example.finalProject_synrgy.dto.action.PayQrisReq;
import com.example.finalProject_synrgy.dto.action.TransferReq;
import com.example.finalProject_synrgy.entity.Qris;
import com.example.finalProject_synrgy.entity.Rekening;
import com.example.finalProject_synrgy.entity.Transaction;
import com.example.finalProject_synrgy.entity.enums.JenisTransaksi;
import com.example.finalProject_synrgy.entity.enums.TransactionReason;
import com.example.finalProject_synrgy.entity.oauth2.User;
import com.example.finalProject_synrgy.repository.*;
import com.example.finalProject_synrgy.service.ActionService;
import com.example.finalProject_synrgy.service.NotificationService;
import com.example.finalProject_synrgy.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.security.Principal;
import java.util.*;

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

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    QrisRepository qrisRepository;

    @Autowired
    NotificationService notificationService;

    public Object getInfoSaldo(Principal principal) {
        return rekeningRepository.findAllByUser(userRepository.findByUsername(principal.getName()));
    }

    public Object getInfoSaldoTotal(Principal principal) {
        List<Rekening> rekenings = rekeningRepository.findAllByUser(userRepository.findByUsername(principal.getName()));
        BalanceTotalRes res = new BalanceTotalRes();
        rekenings.forEach((rekening -> {
            res.setBalanceTotal(res.getBalanceTotal() + rekening.getBalance());
        }));
        res.setCardTotal(rekenings.size());
        res.setName(rekenings.get(0).getName());

        return res;
    }

    @Transactional
    public Object transfer(Principal principal, TransferReq req) {
        validationService.validate(req);

        Rekening userCard = rekeningRepository.findByRekeningNumber(req.getDebitedRekeningNumber());
        if (userCard == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Debited rekening doesn't exist");

        Rekening targetCard = rekeningRepository.findByRekeningNumber(req.getTargetRekeningNumber());
        if (targetCard == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target rekening doesn't exist");
        if (targetCard.getUser() == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target is not Satu user");

        User user = userRepository.findByUsername(principal.getName());
        if (!user.getRekenings().contains(userCard))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Debited rekening is not belong to authenticated user");

        if (!Objects.equals(req.getPin(), user.getPin()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong Pin");

        String referenceNumber = UUID.randomUUID().toString();

        int userFinalBalance = userCard.getBalance() - req.getAmount();
        if (userFinalBalance < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough balance on card");

        userCard.setBalance(userFinalBalance);

        int targetFinalBalance = targetCard.getBalance() + req.getAmount();
        targetCard.setBalance(targetFinalBalance);

        // Transaksi untuk rekening pengirim (userCard)
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

        // Transaksi untuk rekening penerima (targetCard)
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

        // Simpan kedua transaksi sekaligus
        transactionRepository.save(userTransaction);
        transactionRepository.save(targetTransaction);

        // Simpan rekening setelah perubahan balance dan transaksi
        rekeningRepository.save(userCard);
        rekeningRepository.save(targetCard);

        // memastikan semua perubahan tersimpan ke database
        entityManager.flush();

        notificationService.saveNotification("Transfer Berhasil",
                "Transfer kepada " + targetCard.getUser().getFullName() + " senilai " + req.getAmount() + " berhasil",
                user.getUsername());

        notificationService.saveNotification("Transfer Berhasil",
                "Transfer dari " + user.getFullName() + " senilai " + req.getAmount() + " berhasil",
                targetCard.getUser().getUsername());

        return userTransaction;
    }


    @Transactional
    public Object addCard(Principal principal, AddCardReq req) {
        validationService.validate(req);

        Rekening rekening = rekeningRepository.findByCardNumber(req.getCardNumber());

        if(rekening == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found");

        if(rekening.getExpiredDateYear() != req.getYear() || rekening.getExpiredDateMonth() != req.getMonth())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Month and year don't match");

        User user = userRepository.findByUsername(principal.getName());

        rekening.setUser(user);

        user.getRekenings().add(rekening);

        return userRepository.save(user);
    }

    @Transactional
    public Object payQris(Principal principal, PayQrisReq req) {
        validationService.validate(req);

        Rekening userCard = rekeningRepository.findByRekeningNumber(req.getDebitedRekeningNumber());
        if (userCard == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Debited rekening doesn't exist");

        User user = userRepository.findByUsername(principal.getName());
        if (!user.getRekenings().contains(userCard))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Debited rekening is not belong to authenticated user");

        if (!Objects.equals(req.getPin(), user.getPin()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong Pin");

        Qris qris = qrisRepository.findByEncodedQrCode(req.getTargetQris());
        if (qris == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Target qris doesn't exist");

        String referenceNumber = UUID.randomUUID().toString();

        int userFinalBalance = userCard.getBalance() - req.getAmount();
        if (userFinalBalance < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough balance on card");

        userCard.setBalance(userFinalBalance);

        int targetFinalBalance = qris.getRekening().getBalance() + req.getAmount();
        qris.getRekening().setBalance(targetFinalBalance);

        // Transaksi untuk rekening pengirim (userCard)
        Transaction userTransaction = new Transaction();
        userTransaction.setAmount(req.getAmount());
        userTransaction.setRekening(userCard);
        userTransaction.setIsDebited(true);
        userTransaction.setJenisTransaksi(JenisTransaksi.TRANSAKSI_KELUAR);
        userTransaction.setReason(TransactionReason.TRANSFER);
        userTransaction.setReferenceNumber(referenceNumber);
        userTransaction.setVendors(vendorsRepository.findByVendorName("QRIS"));
        userTransaction.setIsInternal(true);
        userTransaction.setBalanceHistory(userFinalBalance);
        userTransaction.setNote("-");

        // Transaksi untuk rekening penerima (targetCard)
        Transaction targetTransaction = new Transaction();
        targetTransaction.setAmount(req.getAmount());
        targetTransaction.setRekening(qris.getRekening());
        targetTransaction.setIsDebited(false);
        targetTransaction.setJenisTransaksi(JenisTransaksi.TRANSAKSI_MASUK);
        targetTransaction.setReason(TransactionReason.TRANSFER);
        targetTransaction.setReferenceNumber(referenceNumber);
        targetTransaction.setVendors(vendorsRepository.findByVendorName("QRIS"));
        targetTransaction.setIsInternal(true);
        targetTransaction.setBalanceHistory(targetFinalBalance);
        targetTransaction.setNote("-");

        // Simpan kedua transaksi sekaligus
        transactionRepository.save(userTransaction);
        transactionRepository.save(targetTransaction);

        // Simpan rekening setelah perubahan balance dan transaksi
        rekeningRepository.save(userCard);
        qrisRepository.save(qris);

        // memastikan semua perubahan tersimpan ke database
        entityManager.flush();

        notificationService.saveNotification("Pembayaran QRIS Berhasil",
                "Pembayaran QRIS kepada " + qris.getName() + " senilai " + req.getAmount() + " berhasil",
                user.getUsername());

        notificationService.saveNotification("Transfer QRIS Berhasil",
                "Pembayaran QRIS dari " + user.getFullName() + " senilai " + req.getAmount() + " berhasil",
                qris.getRekening().getUser().getUsername());

        return userTransaction;
    }
}
