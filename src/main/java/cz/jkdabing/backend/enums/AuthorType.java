package cz.jkdabing.backend.enums;

import lombok.Getter;

@Getter
public enum AuthorType {

    AUTHOR(1),
    COAUTHOR(2),
    TRANSLATOR(3),
    INTERPRET(4),
    DIRECTOR(5),
    SCRIPT(6),
    SOUND(7),
    MUSIC(8),
    STUDIO(9),
    PUBLISHER(10);

    private final int order;

    AuthorType(int order) {
        this.order = order;
    }

}
