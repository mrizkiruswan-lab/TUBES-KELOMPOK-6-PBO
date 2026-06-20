package com.rental.presentation.menu;

import com.rental.application.AuthService;
import com.rental.domain.entity.User;
import com.rental.presentation.util.ConsoleHelper;

public class LoginMenu {

    private static final String NAMA_KELOMPOK = "SISTEM RENTAL KENDARAAN - KELOMPOK 6";

    private final AuthService   authService;
    private final AdminMenu     adminMenu;
    private final StaffMenu     staffMenu;
    private final OwnerMenu     ownerMenu;

    public LoginMenu(AuthService authService,
    AdminMenu adminMenu,
    StaffMenu staffMenu,
    OwnerMenu ownerMenu) {
        this.authService = authService;
        this.adminMenu   = adminMenu;
        this.staffMenu   = staffMenu;
        this.ownerMenu   = ownerMenu;
    }

    public void show() {
        while (true) {
            printWelcomeBanner();

            if (authService.isLocked()) {
                System.out.println("[SISTEM DIKUNCI] Terlalu banyak percobaan login gagal.");
                System.out.println("Restart aplikasi untuk mencoba kembali.");
                break;
            }

            String username = ConsoleHelper.readLine("Username : > ");
            String password = ConsoleHelper.readLine("Password : > ");

            try {
                User user = authService.login(username, password);
                System.out.println("\n[SUKSES] Login berhasil sebagai " + user.getRole() + ".");
                ConsoleHelper.pressEnterToContinue();

                routeToDashboard(user);

            } catch (SecurityException e) {
                ConsoleHelper.printError(e.getMessage());
                break;
            } catch (IllegalArgumentException e) {
                ConsoleHelper.printError(e.getMessage());
            }
        }
    }

    private void routeToDashboard(User user) {
        switch (user.getRole()) {
            case ADMIN  -> adminMenu.show(user);
            case STAFF  -> staffMenu.show(user);
            case OWNER  -> ownerMenu.show(user);
        }
    }

    private void printWelcomeBanner() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  SELAMAT DATANG DI " + NAMA_KELOMPOK);
        System.out.println("=".repeat(60));
    }
}
