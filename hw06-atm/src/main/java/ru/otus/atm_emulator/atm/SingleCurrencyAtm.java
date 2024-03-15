package ru.otus.atm_emulator.atm;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.atm_emulator.other.BankAccountProvider;
import ru.otus.atm_emulator.other.Banknote;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.otus.atm_emulator.other.Banknote.Nominal;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SingleCurrencyAtm extends Atm {

    public static final long SINGLE_CURRENCY_ATM_NOMINAL_LIMIT = 100;

    private final Currency currency;
    private final NavigableMap<Nominal, List<Banknote>> vault = new TreeMap<>();

    public SingleCurrencyAtm(BankAccountProvider bankAccountProvider, Currency currency) {
        super(bankAccountProvider);
        initVault(List.of(currency));
        this.currency = currency;
    }

    @Override
    @SneakyThrows
    public List<Banknote> cashOut(Long accountId, BigDecimal toCashOut, Currency currency) {
        val availableFunds = getBankAccountProvider().availableFunds(accountId, currency);
        if (availableFunds.compareTo(toCashOut) < 0) {
            log.info("На счете {} не хватает средств для снятия суммы {}. Доступные средства: {}.",
                    accountId, toCashOut.doubleValue(), availableFunds.doubleValue());
            return List.of();
        }
        if (!currency.equals(this.currency)) {
            log.info("Банкомат не располагает валютой {}.", currency.getCurrencyCode());
            return List.of();
        }

        var cash = toCashOut.longValue();
        val currencyMinNominal = vault.lastKey().getNominalValue();
        val cashToGiveOut = (cash / currencyMinNominal) * currencyMinNominal;

        List<Banknote> res = new ArrayList<>();
        val nominalIterator = vault.navigableKeySet().iterator();
        var value = 0;
        var nominal = nominalIterator.next();
        while (value != cashToGiveOut) {
            val nominalBanknotes = vault.get(nominal);
            if (nominalBanknotes.isEmpty()) {
                if (nominalIterator.hasNext()) {
                    nominal = nominalIterator.next();
                    continue;
                } else {
                    log.info("В банкомате недостаточно средств.");
                    return List.of();
                }
            }
            if (nominal.getNominalValue() > cashToGiveOut - value && nominalIterator.hasNext()) {
                nominal = nominalIterator.next();
                continue;
            }
            res.add(nominalBanknotes.getLast());
            value += nominal.getNominalValue();
            nominalBanknotes.removeLast();
        }

        getBankAccountProvider().withdrawCash(accountId, new BigDecimal(cashToGiveOut), currency);
        return res;
    }

    @Override
    public List<Banknote> depositCash(Long accountId, List<Banknote> incomingBanknotes) {
        val returnedBanknotes = new ArrayList<Banknote>();
        long sum = 0L;
        for (val banknote : incomingBanknotes) {
            if (!canPutInVault(banknote)) {
                returnedBanknotes.add(banknote);
            } else {
                val nominal = banknote.nominal();
                vault.get(nominal).add(banknote);
                sum += banknote.nominal().getNominalValue();
            }
        }
        val income = getBankAccountProvider().convert(sum, currency);
        getBankAccountProvider().depositCash(accountId, income);
        log.info("Возвращены банкноты: {}.", returnedBanknotes);
        return returnedBanknotes;
    }

    private boolean canPutInVault(Banknote banknote) {
        if (!banknote.currency().equals(currency)) {
            log.info("Данный банкомат не принимает валюту {}.", currency.getCurrencyCode());
            return false;
        }
        val nominal = banknote.nominal();
        val newNominalQty = vault.get(nominal).size() + 1;
        return newNominalQty <= getNominalLimit();
    }

    @Override
    public BigDecimal showAccountSize(Long accountId, Currency currency) {
        val accountSize = getBankAccountProvider().availableFunds(accountId, currency);
        log.info("Счет: {}. Размер: {}{}.", accountId, accountSize.doubleValue(), currency.getSymbol());
        return accountSize;
    }

    @Override
    @SneakyThrows
    public void fullCharge() {
        for (val vaultEntry : vault.entrySet()) {
            val nominal = vaultEntry.getKey();
            val nominalBanknotes = vaultEntry.getValue();
            while (nominalBanknotes.size() < SINGLE_CURRENCY_ATM_NOMINAL_LIMIT) {
                nominalBanknotes.add(new Banknote(nominal, currency));
            }
        }
    }

    @Override
    @SneakyThrows
    protected void initVault(List<Currency> currencyList) {
        val singleCurrency = currencyList.get(0);
        val nominalMap = Nominal.byCurrency(singleCurrency).stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        nominal -> new ArrayList<Banknote>(),
                        (nominal1, nominal2) -> nominal1,
                        TreeMap::new
                ));
        vault.putAll(nominalMap);
    }

    @Override
    public Long getNominalLimit() {
        return SINGLE_CURRENCY_ATM_NOMINAL_LIMIT;
    }

    public NavigableMap<Nominal, List<Banknote>> getVault() {
        return new TreeMap<>(vault);
    }

}
