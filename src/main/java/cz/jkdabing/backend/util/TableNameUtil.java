package cz.jkdabing.backend.util;

import jakarta.persistence.Table;

public class TableNameUtil {

    private TableNameUtil() {
    }

    public static String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            return table.name();
        }

        return clazz.getSimpleName();
    }
}
