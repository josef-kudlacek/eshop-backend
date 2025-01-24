package cz.jkdabing.backend.enums;

public enum FileType {
    AUDIO,
    COMPRESSED;

    public String toLowerCase() {
        return this.name().toLowerCase();
    }
}
