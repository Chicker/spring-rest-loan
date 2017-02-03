package ru.chicker.utils;

public class CollectionUtils {
    public static <T> int sizeOfIterable(Iterable<T> iter) {
        int size = 0;
        for (T ignored : iter) {
            size ++;
        }
        return size;
    }
}
