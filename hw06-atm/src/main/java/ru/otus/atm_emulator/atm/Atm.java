package ru.otus.atm_emulator.atm;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import ru.otus.atm_emulator.other.BankAccountProvider;
import ru.otus.atm_emulator.other.Banknote;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Slf4j
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public abstract class Atm {

    private final BankAccountProvider bankAccountProvider;

    public abstract List<Banknote> cashOut(Long accountId, BigDecimal toCashOut, Currency currency);

    public abstract List<Banknote> depositCash(Long accountId, List<Banknote> incomingBanknotes);

    public abstract BigDecimal showAccountSize(Long accountId, Currency currency);

    public abstract void fullCharge();

    public abstract Long getNominalLimit();

    protected abstract void initVault(List<Currency> currencyList);

    protected BankAccountProvider getBankAccountProvider() {
        return bankAccountProvider;
    }

}
