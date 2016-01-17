package com.freelancer.xwatch.database;

import java.util.*;
import java.util.Map.*;

public class Column<K, V> implements Entry<K, V>, java.io.Serializable {

    private static final long serialVersionUID = -6875034139784896861L;
    private final K key;
    private V value;

    public Column(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Column(Entry<? extends K, ? extends V> entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Map.Entry))
            return false;
        @SuppressWarnings("rawtypes")
        Map.Entry e = (Map.Entry) o;
        return eq(key, e.getKey()) && eq(value, e.getValue());
    }

    private static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public int hashCode() {
        return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    }

    public String toString() {
        return key + "=" + value;
    }

}
