package utils;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomFileUtils {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static File getRandomFile(String folderPath, List<String> allowedExtensions, boolean checkSize) {

        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("Thư mục không tồn tại: " + folderPath);
        }

        File[] files = folder.listFiles(file -> {
            if (file == null || !file.isFile()) return false;

            String fileName = file.getName().toLowerCase();

            boolean matchesExtension = allowedExtensions.stream()
                    .anyMatch(ext -> fileName.endsWith("." + ext));

            if (!matchesExtension) return false;

            if (checkSize) {
                return file.length() <= MAX_FILE_SIZE;
            }

            return true;
        });

        if (files == null || files.length == 0) {
            throw new RuntimeException(
                    "Không tìm thấy file hợp lệ trong thư mục: " + folderPath +
                            " | Extension: " + allowedExtensions +
                            (checkSize ? " | Size <= 15MB" : "")
            );
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(files.length);
        return files[randomIndex];
    }

    public static Path getFileAvatarValid(String folderPath) {
        return getRandomFile(
                folderPath,
                Arrays.asList("jpg", "jpeg", "png"),
                true
        ).toPath();
    }

    public static String getFileAvatarIvl(String folderPath) {
        return getRandomFile(
                folderPath,
                Arrays.asList("docx", "pdf"),
                false
        ).getAbsolutePath();
    }

    public static Path getFileAvatarValidPath(String folderPath) {
        return getFileAvatarValid(folderPath);
    }

    public static Path getFileAvatarIvlPath(String folderPath) {
        return Path.of(getFileAvatarIvl(folderPath));
    }
}