package ru.hits.trb.trbcore.util;

import lombok.experimental.UtilityClass;
import ru.hits.trb.trbcore.entity.AccountEntity;
import ru.hits.trb.trbcore.exception.NotEnoughMoneyException;

import java.math.BigDecimal;

@UtilityClass
public class BalanceValidator {

    public static void validateBalanceForTransaction(AccountEntity payerAccount,
                                                     BigDecimal amount) {
        if (payerAccount.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughMoneyException(
                    STR."Account \{payerAccount.getId()} has only \{payerAccount.getBalance()} but it needs \{amount}, currency - \{payerAccount.getCurrency()}"
            );
        }
    }

}
