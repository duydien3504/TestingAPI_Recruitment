package data;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

public class TestGenerateAccount {

    private static final Faker faker = new Faker(new Locale("vi"));

    public static String fullName() {
        return faker.name().fullName();
    }

    public static String email() {
        return faker.internet().emailAddress();
    }

    public static String emailInv() {
        return "@gmail.com";
    }

    public static String emailInvDomain() {
        return faker.name().username() + "@gm.com";
    }

    public static String password() {
        return faker.internet().password(8, 20, true, true, true);
    }

    public static String passwordIvl() {
        return faker.internet().password(4, 5);
    }

    public static String passwordWithoutUppercase() {
        return faker.lorem().characters(8, 12, false, true);
    }

    public static String fullNamewithaCharacter() {
        return "a";
    }

    public static String phone() {
        return "09" + faker.number().digits(8);
    }

    public static String phoneIvl() {
        return "09" + faker.number().digits(7);
    }

    public static String address() {
        return faker.address().fullAddress();
    }

    public static String bio() {
        return faker.lorem().sentence(10);
    }

    public static String dateOfBirth() {
        LocalDate dob = LocalDate.now()
                .minusYears(faker.number().numberBetween(18, 45));
        return dob.toString(); // yyyy-MM-dd
    }

    public static String gender() {
        return faker.bool().bool() ? "MALE" : "FEMALE";
    }

    public static String fullNameIvl() {
        return faker.lorem().characters(120, 150, true, false);
    }

    public static String userId() {
        return UUID.randomUUID().toString();
    }
}
