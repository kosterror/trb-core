package ru.hits.trb.trbcore.client.exchangerate.impl;

import lombok.experimental.UtilityClass;

@UtilityClass
class Paths {
    static final String GET_EXCHANGE_RATE = "/api/v2/rates/merchant/{from}/{to}";
}
