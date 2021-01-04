package com.tschanz.v_bro.repo.persistence.jdbc.mock;

import com.tschanz.v_bro.common.types.KeyValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class SpyResult {
    public final List<KeyValue> keyValues;


    public static SpyResult fromStrings(String[]... keyValues) {
        SpyResult result = new SpyResult();
        Arrays.stream(keyValues).forEach(kv -> result.keyValues.add(new KeyValue(kv[0], kv[1])));
        return result;
    }


    public SpyResult(KeyValue... keyValues) {
        this.keyValues = Arrays.stream(keyValues).collect(Collectors.toList());
    }


    public String getValue(String key) {
        KeyValue keyValue = this.findKeyValue(key);
        if (keyValue == null) {
            return null;
        } else {
            return keyValue.value;
        }
    }


    private KeyValue findKeyValue(String key) {
        return this.keyValues
            .stream()
            .filter(kv -> kv.key.equals(key))
            .findFirst()
            .orElse(null);
    }
}
