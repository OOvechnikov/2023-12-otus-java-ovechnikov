package ru.otus.atm_emulator.atm;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.atm_emulator.other.BankAccountProvider;
import ru.otus.atm_emulator.other.Banknote;
import ru.otus.atm_emulator.other.Banknote.Nominal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static ru.otus.atm_emulator.atm.SingleCurrencyAtm.SINGLE_CURRENCY_ATM_NOMINAL_LIMIT;
import static ru.otus.atm_emulator.other.Banknote.Nominal.*;
import static ru.otus.atm_emulator.other.Banknote.RUB;
import static ru.otus.atm_emulator.other.Banknote.USD;

@ExtendWith(MockitoExtension.class)
class SingleCurrencyAtmTest {

    static final Currency CUR_RUB = Currency.getInstance(RUB);
    static final Currency CUR_USD = Currency.getInstance(USD);

    private Atm atm;
    @Mock
    private BankAccountProvider bankAccountProvider;

    static Arguments[] cashOutArgs() {
        return new Arguments[]{
                arguments(new BigDecimal("100500"), CUR_RUB, new BigDecimal("2000"), List.of()),
                arguments(new BigDecimal("1000"), CUR_USD, new BigDecimal("5000"), List.of()),
                arguments(new BigDecimal("1000000"), CUR_RUB, new BigDecimal("2000000"), List.of()),
                arguments(new BigDecimal("12570"), CUR_RUB, new BigDecimal("2000000"), List.of(new Banknote(N5000, CUR_RUB), new Banknote(N5000, CUR_RUB), new Banknote(N2000, CUR_RUB), new Banknote(N500, CUR_RUB), new Banknote(N50, CUR_RUB), new Banknote(N10, CUR_RUB), new Banknote(N10, CUR_RUB))),
        };
    }

    @ParameterizedTest
    @MethodSource("cashOutArgs")
    void cashOut(BigDecimal toCashOut, Currency currency, BigDecimal accountSize, List<Banknote> expected) {
        Mockito.when(bankAccountProvider.availableFunds(any(), any())).thenReturn(accountSize);

        atm = new SingleCurrencyAtm(bankAccountProvider, CUR_RUB);
        atm.fullCharge();
        val actual = atm.cashOut(1L, toCashOut, currency);

        assertEquals(expected, actual);

    }

    @Test
    void depositAndCashOut() {
        atm = new SingleCurrencyAtm(bankAccountProvider, CUR_RUB);
        val toDeposit = new ArrayList<Banknote>();
        for (int i = 0; i < 7; i++) {
            toDeposit.add(new Banknote(N1000, CUR_RUB));
        }
        for (int i = 0; i < 12; i++) {
            toDeposit.add(new Banknote(N10, CUR_RUB));
        }
        toDeposit.add(new Banknote(N100, CUR_USD));
        val depositActual = atm.depositCash(1L, toDeposit);
        assertEquals(depositActual, List.of(new Banknote(N100, CUR_USD)));

        toDeposit.removeLast();
        Mockito.when(bankAccountProvider.availableFunds(any(), any())).thenReturn(new BigDecimal("2000000"));
        val cashOutActual = atm.cashOut(1L, new BigDecimal("7120"), CUR_RUB);
        assertEquals(cashOutActual, toDeposit);

    }

    @SneakyThrows
    static Arguments[] depositCashArgs() {
        val usd = new Banknote(N10, CUR_USD);
        val rub10List = new ArrayList<Banknote>();
        for (int i = 0; i < SINGLE_CURRENCY_ATM_NOMINAL_LIMIT + 2; i++) {
            rub10List.add(new Banknote(N10, CUR_RUB));
        }
        return new Arguments[]{
                arguments(List.of(usd), List.of(usd)),
                arguments(rub10List, List.of(new Banknote(N10, CUR_RUB), new Banknote(N10, CUR_RUB))),
        };
    }

    @ParameterizedTest
    @MethodSource("depositCashArgs")
    void depositCash(List<Banknote> incoming, List<Banknote> returned) {
        atm = new SingleCurrencyAtm(bankAccountProvider, CUR_RUB);
        val res = atm.depositCash(1L, incoming);

        assertEquals(res, returned);
    }

    @Test
    void showAccountSize() {
        val cashMock = new BigDecimal("1000");
        long accountId = 1L;
        Mockito.when(bankAccountProvider.availableFunds(accountId, CUR_RUB)).thenReturn(cashMock);
        atm = new SingleCurrencyAtm(bankAccountProvider, CUR_RUB);

        val actual = atm.showAccountSize(accountId, CUR_RUB);

        assertEquals(cashMock, actual);
    }

    @Test
    @SneakyThrows
    void singleCurrencyAtmFullCharge() {
        val singleCurrencyAtm = new SingleCurrencyAtm(bankAccountProvider, CUR_RUB);

        singleCurrencyAtm.fullCharge();
        val actual = singleCurrencyAtm.getVault().entrySet().stream()
                .flatMap(vaultEntry -> vaultEntry.getValue().stream())
                .collect(Collectors.toSet());
        val expected = Nominal.byCurrency(CUR_RUB).stream()
                .flatMap(nominal ->
                        Stream.generate(() -> new Banknote(nominal, CUR_RUB))
                                .limit(SINGLE_CURRENCY_ATM_NOMINAL_LIMIT)
                ).collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

}