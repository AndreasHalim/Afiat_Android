package com.example.afiat.datastore;

abstract class BasicStore {
    boolean isSynchronized = true;
    abstract void persist();
    abstract void flush();
    abstract void recover();
}
