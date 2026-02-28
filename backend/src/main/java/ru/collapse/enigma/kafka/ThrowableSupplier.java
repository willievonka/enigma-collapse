package ru.collapse.enigma.kafka;

public interface ThrowableSupplier<T> {
    T get() throws Exception;
}
