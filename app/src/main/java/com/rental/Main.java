package com.rental;

import com.rental.application.*;
import com.rental.domain.repository.*;
import com.rental.infrastructure.persistence.*;
import com.rental.presentation.menu.*;

public class Main {

    public static void main(String[] args) {

        UserRepository userRepo = new JsonUserRepository();
        KendaraanRepository kendaraanRepo = new JsonKendaraanRepository();
        PelangganRepository pelangganRepo = new JsonPelangganRepository();
        TransaksiRepository transaksiRepo = new JsonTransaksiRepository();

        AuthService authService = new AuthService(userRepo);
        KendaraanService kendaraanService = new KendaraanService(kendaraanRepo);
        PelangganService pelangganService = new PelangganService(pelangganRepo);
        TransaksiService transaksiService = new TransaksiService(
                transaksiRepo, kendaraanRepo, pelangganRepo);

        AdminMenu  adminMenu  = new AdminMenu(kendaraanService);
        StaffMenu  staffMenu  = new StaffMenu(pelangganService, kendaraanService, transaksiService);
        OwnerMenu  ownerMenu  = new OwnerMenu(transaksiService);
        LoginMenu  loginMenu  = new LoginMenu(authService, adminMenu, staffMenu, ownerMenu);

        loginMenu.show();

        System.out.println("\nTerima kasih telah menggunakan Sistem Rental Kendaraan. Sampai jumpa!");
    }
}
