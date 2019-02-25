package com.example.afiat.datastore;

import java.util.ArrayList;

public interface FetchStoreListener {
    void onFinish(boolean success, BasicStore []stores);
}
