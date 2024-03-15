package ru.otus.atm_emulator.other;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import ru.otus.atm_emulator.exception.CurrencyException;

import java.util.Currency;
import java.util.List;

public record Banknote(Nominal nominal, Currency currency) {

    public static final String RUB = "RUB";
    public static final String USD = "USD";

    @SneakyThrows
    public Banknote {
        val nominalList = Nominal.byCurrency(currency);
        if (!nominalList.contains(nominal)) {
            throw new CurrencyException("Для валюты %s не существует номинала %d."
                    .formatted(currency.getCurrencyCode(), nominal.getNominalValue()));
        }
    }

    @Override
    public String toString() {
        return "{" + nominal + ", " + currency + "}";
    }

    @Getter
    @RequiredArgsConstructor
    public enum Nominal implements Comparable<Nominal> {
        N5000(5000),
        N2000(2000),
        N1000(1000),
        N500(500),
        N100(100),
        N50(50),
        N20(20),
        N10(10),
        N5(5),
        N2(2),
        N1(1),
        ;

        private final Integer nominalValue;

        public static List<Nominal> byCurrency(Currency currency) throws CurrencyException {
            val currencyCode = currency.getCurrencyCode();
            return switch (currencyCode) {
                case RUB -> List.of(N10, N50, N100, N500, N1000, N2000, N5000);
                case USD -> List.of(N1, N2, N5, N10, N20, N50, N100);
                default -> throw new CurrencyException("Валюта %s не поддерживается.".formatted(currencyCode));
            };
        }

    }

}
