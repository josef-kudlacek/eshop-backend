package cz.jkdabing.backend.util;

import org.springframework.util.StringUtils;

public class FileValidationUtils {

    private static final String[] AUDIO_EXTENSIONS = {"mp3"};

    private static final String[] IMAGE_EXTENSIONS = {"jpg", "jpeg", "png"};

    private FileValidationUtils() {
    }

    public static boolean isAudioFileValid(String fileName) {
        return isFileExtensionValid(fileName, AUDIO_EXTENSIONS);
    }

    public static boolean isImageFileValid(String fileName) {
        return isFileExtensionValid(fileName, IMAGE_EXTENSIONS);
    }

    private static boolean isFileExtensionValid(String fileName, String... allowedExtensions) {
        String fileExtension = getFileExtension(fileName);
        for (String allowedExtension : allowedExtensions) {
            if (fileExtension.equalsIgnoreCase(allowedExtension)) {
                return true;
            }
        }

        return false;
    }

    private static String getFileExtension(String fileName) {
        String fileExtension = "";
        if (StringUtils.hasText(fileName) && fileName.contains(".")) {
            int fileExtensionIndex = getFileExtensionIndex(fileName);
            if (fileExtensionIndex > 0) {
                fileExtension = fileName.substring(fileExtensionIndex);
            }
        }

        return fileExtension;
    }

    private static int getFileExtensionIndex(String fileName) {
        return fileName.lastIndexOf(".") + 1;
    }
}
