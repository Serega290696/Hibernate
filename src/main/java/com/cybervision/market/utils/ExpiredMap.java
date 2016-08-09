package com.cybervision.market.utils;

import java.util.Map;

public interface ExpiredMap<K, V> extends Map<K, V> {

    void clearExpired();

    V put(K key, V value, Long timeExpired);
}
