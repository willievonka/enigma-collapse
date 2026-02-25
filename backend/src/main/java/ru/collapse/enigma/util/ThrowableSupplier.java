package ru.collapse.enigma.util;

public interface ThrowableSupplier<T> {
    T get() throws Exception;
}
