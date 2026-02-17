package data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class TestGenerateJob {
    private static final Faker faker = new Faker(new Locale("vi"));

    public static String title() {
        return faker.job().title();
    }

    public static String jd() {
        return faker.lorem().paragraph(3);
    }

    public static String requirements() {
        return faker.lorem().paragraph(3);
    }

    public static int category_id() {
        return faker.number().numberBetween(1,100);
    }

    public static int location_id() {
        return faker.number().numberBetween(1,63);
    }

    public static int locationId_Ivl() {
        return faker.number().numberBetween(100,200);
    }

    public static int level_id() {
        return faker.number().numberBetween(1,9);
    }

    public static int levelId_Ivl() {
        return faker.number().numberBetween(20,30);
    }

    public static int salary_min() {
        return faker.number().numberBetween(500,1000);
    }

    public static int salary_max() {
        return faker.number().numberBetween(1100,5000);
    }

    public static String idJob() {
        return String.valueOf(faker.number().numberBetween(120000, 120100));
    }
}
