package ru.hits.trb.trbcore.entity.enumeration;

import lombok.Getter;

@Getter
public enum TransactionCode {
    SUCCESS(0),
    INVALID_AMOUNT(1),
    ACCOUNT_PAYER_NOT_FOUND(2),
    ACCOUNT_PAYEE_NOT_FOUND(3),
    ACCOUNT_PAYER_CLOSED(4),
    ACCOUNT_PAYEE_CLOSED(5),
    NOT_ENOUGH_MONEY(6);

    private final int dbCode;

    TransactionCode(int dbCode) {
        this.dbCode = dbCode;
    }
}
