package de.oliver.javamm.utils;

public class KeyValue<T, V> {

    private final T key;
    private final V value;

    public KeyValue(T key, V value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
