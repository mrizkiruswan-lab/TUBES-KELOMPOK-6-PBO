package com.rental.infrastructure.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rental.domain.entity.Kendaraan;
import com.rental.domain.entity.Mobil;
import com.rental.domain.entity.Motor;

public final class GsonFactory {

    private static final Gson INSTANCE = buildGson();

    private GsonFactory() {}

    public static Gson getInstance() {
        return INSTANCE;
    }
    private static Gson buildGson() {
        RuntimeTypeAdapterFactory<Kendaraan> kendaraanFactory =
                RuntimeTypeAdapterFactory.of(Kendaraan.class, "type")
                        .registerSubtype(Mobil.class,  "Mobil")
                        .registerSubtype(Motor.class, "Motor");

        return new GsonBuilder()
                .registerTypeAdapterFactory(kendaraanFactory)
                .setPrettyPrinting()
                .create();
    }
}
