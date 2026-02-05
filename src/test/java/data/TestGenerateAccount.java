package data;

import com.github.javafaker.Faker;

import java.util.UUID;

public class TestGenerateAccount {
    public static String fullName() {
        return "Test User" + System.currentTimeMillis();
    }

    public static String email() {
        return "test" + System.currentTimeMillis() + "@gmail.com";
    }

    public static String emailInv() {
        return "@gmail.com";
    }

    public static String emailInvDomain() {
        return "test" + System.currentTimeMillis() + "@gm.com";
    }

    public static String password() {
        return "Abcd" + (int)(Math.random() * 9000 + 1000) + "@";
    }

    public static String passwordIvl() {
        return "Ab" + (int)(Math.random() * 9000 + 100) + "@";
    }

    public static String passwordWithoutUppercase() {
        return "abcd" + (int)(Math.random() * 9000 + 1000) + "@";
    }

    public static String fullNamewithaCharacter() {
        return "a";
    }

    public static String phone() {
        return "093" + (System.currentTimeMillis() % 10_000_000);
    }

    public static String phoneIvl() {
        return "09" + (System.currentTimeMillis() % 10_000_000);
    }

    public static String address() {
        return "So " + (int)(Math.random() * 999) +
                ", Duong " + (int)(Math.random() * 99) +
                ", Phuong " + (int)(Math.random() * 20) +
                ", TP HCM";
    }

    public static String bio() {
        return "Bio test " + (int)(Math.random() * 100000);
    }

    public static String dateOfBirth() {
        int year = 1980 + (int)(Math.random() * 25);
        int month = 1 + (int)(Math.random() * 12);
        int day = 1 + (int)(Math.random() * 28);

        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public static String gender() {
        return Math.random() < 0.5 ? "MALE" : "FEMALE";
    }

    public static String fullNameIvl() {
        Faker faker = new Faker();
        return faker.lorem().characters(120, 150, true, true);
    }

    public static String userId() {
        return UUID.randomUUID().toString();
    }
}
