package ru.otus.atm_emulator.other;

import java.math.BigDecimal;
import java.util.Currency;

public interface BankAccountProvider {

    BigDecimal availableFunds(Long accountId, Currency currency);

    BigDecimal convert(Long sum, Currency currency);

    BigDecimal depositCash(Long accountId, BigDecimal income);

    BigDecimal withdrawCash(Long accountId, BigDecimal outcome, Currency currency);

}
