package cz.jkdabing.backend.util;

import org.springframework.util.StringUtils;

public class FileUtils {

    private static final String[] AUDIO_EXTENSIONS = {"mp3"};

    private static final String[] IMAGE_EXTENSIONS = {"jpg", "jpeg", "png"};

    private static final String[] COMPRESSED_EXTENSIONS = {"zip"};

    private FileUtils() {
    }

    public static boolean isAudioFileValid(String fileName) {
        return isFileExtensionValid(fileName, AUDIO_EXTENSIONS);
    }

    public static boolean isImageFileValid(String fileName) {
        return isFileExtensionValid(fileName, IMAGE_EXTENSIONS);
    }

    public static boolean isCompressedFileValid(String fileName) {
        return isFileExtensionValid(fileName, COMPRESSED_EXTENSIONS);
    }

    public static String getFileExtension(String fileName) {
        String fileExtension = null;
        boolean emptyFileNameOrNoExtension = !StringUtils.hasText(fileName) || !fileName.contains(".");
        if (emptyFileNameOrNoExtension) {
            return null;
        }

        int fileExtensionIndex = getFileExtensionIndex(fileName);
        if (fileExtensionIndex > 0) {
            fileExtension = fileName.substring(fileExtensionIndex);
        }

        return fileExtension;
    }

    private static boolean isFileExtensionValid(String fileName, String... allowedExtensions) {
        String fileExtension = getFileExtension(fileName);
        boolean noFileExtension = !StringUtils.hasText(fileExtension);
        if (noFileExtension) {
            return false;
        }

        for (String allowedExtension : allowedExtensions) {
            if (fileExtension.equalsIgnoreCase(allowedExtension)) {
                return true;
            }
        }

        return false;
    }

    private static int getFileExtensionIndex(String fileName) {
        return fileName.lastIndexOf(".") + 1;
    }
}
