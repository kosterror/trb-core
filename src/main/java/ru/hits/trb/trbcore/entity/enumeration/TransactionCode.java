package ru.hits.trb.trbcore.entity.enumeration;

import lombok.Getter;

@Getter
public enum TransactionCode {
    SUCCESS(0),
    NOT_ENOUGH_MONEY(1);

    private final int dbCode;

    TransactionCode(int dbCode) {
        this.dbCode = dbCode;
    }
}
