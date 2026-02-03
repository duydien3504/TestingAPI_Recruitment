package data;

public class TestGenerateAccount {
    public static String fullName() {
        return "Test User";
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
}
