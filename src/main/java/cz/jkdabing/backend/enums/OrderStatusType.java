package cz.jkdabing.backend.enums;

public enum OrderStatusType {
    CREATED,
    PROCESSING,
    CANCELLED,
    WAITING_FOR_PAYMENT,
    COMPLETED,
    SHIPPED,
    REFUNDED,
    DOWNLOADED,
    EXPIRED
}
