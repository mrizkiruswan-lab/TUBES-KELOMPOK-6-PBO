package com.rental.presentation.util;

import java.util.Scanner;

public final class ConsoleHelper {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleHelper() {}

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Input tidak valid. Harap masukkan angka bulat.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Double.parseDouble(input.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Input tidak valid. Harap masukkan angka.");
            }
        }
    }

    public static String readNonBlank(String prompt) {
        while (true) {
            String val = readLine(prompt);
            if (!val.isBlank()) return val;
            System.out.println("[ERROR] Input tidak boleh kosong.");
        }
    }

    public static void pressEnterToContinue() {
        System.out.print("\nTekan ENTER untuk melanjutkan...");
        SCANNER.nextLine();
    }

    public static String formatRupiah(double amount) {
        return "Rp " + amount ;
    }

    public static void printHeader(String title) {
        String line = "=".repeat(62);
        System.out.println("\n" + line);
        System.out.println("  " + title);
        System.out.println(line);
    }

    public static void printDivider() {
        System.out.println("-".repeat(62));
    }

    public static void printSuccess(String msg) {
        System.out.println("\n[SUKSES] " + msg);
    }

    public static void printError(String msg) {
        System.out.println("\n[GAGAL] " + msg);
    }

    public static void printInfo(String msg) {
        System.out.println("[INFO] " + msg);
    }
}
