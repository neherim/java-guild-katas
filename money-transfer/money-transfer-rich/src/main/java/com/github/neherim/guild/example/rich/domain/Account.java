package com.github.neherim.guild.example.rich.domain;

import com.github.neherim.guild.example.rich.domain.exceptions.MoneyAlreadyReservedException;
import com.github.neherim.guild.example.rich.domain.exceptions.MoneyNotReservedException;
import com.github.neherim.guild.example.rich.domain.exceptions.NotEnoughMoneyException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    @EmbeddedId
    private AccountId id;

    private LocalDateTime dateOpen;
    private LocalDateTime dateClose;
    private Integer balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MoneyReservation> moneyReservations;

    /**
     * Open new account
     */
    public static Account openAccount(LocalDateTime openDateTime) {
        return new Account(new AccountId(), openDateTime, null, 0, new ArrayList<>());
    }

    /**
     * Remove reservation and withdraw money for the operation
     */
    public Integer withdrawReserved(Long operationId) {
        var reservation = removeReservation(operationId).orElseThrow(() -> new MoneyNotReservedException(operationId));
        balance = balance - reservation.getAmount();
        return reservation.getAmount();
    }

    /**
     * Deposit money into account
     */
    public void deposit(Integer amount) {
        balance = balance + amount;
    }

    /**
     * Reserve money on the account
     */
    public void reserveMoney(Long operationId, Integer amount) {
        if (findMoneyReservationByOperationId(operationId).isPresent()) {
            throw new MoneyAlreadyReservedException(operationId);
        }
        if (amount > getAvailableBalance()) {
            throw new NotEnoughMoneyException(getId());
        }
        moneyReservations.add(new MoneyReservation(operationId, amount, this));
    }

    public AccountId getId() {
        return id;
    }

    /**
     * Remove money reservation for the operation if it exists
     *
     * @return removed reservation or Optional.empty() if it doesn't exist
     */
    public Optional<MoneyReservation> removeReservation(Long operationId) {
        var reservationOpt = findMoneyReservationByOperationId(operationId);
        reservationOpt.ifPresent(moneyReservations::remove);
        return reservationOpt;
    }

    /**
     * Get not reserved balance, available for operations
     */
    public Integer getAvailableBalance() {
        return balance - getReservedMoneyAmount();
    }

    /**
     * Get actual total balance, including reserved money
     */
    public Integer getTotalBalance() {
        return balance;
    }

    private Integer getReservedMoneyAmount() {
        return moneyReservations.stream().mapToInt(MoneyReservation::getAmount).sum();
    }

    private Optional<MoneyReservation> findMoneyReservationByOperationId(Long operationId) {
        return moneyReservations.stream().filter(mr -> mr.getOperationId().equals(operationId)).findAny();
    }
}
