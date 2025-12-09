package com.example.workaccounting.domain.enums;

public enum ProjectStatusType {
    VOTING,
    APPROVED,
    ARCHIVED_CANCELED,
    ARCHIVED_COMPLETED,
    IN_PROGRESS;

    public static ProjectStatusType valueOfOrRuntimeException(String value) {
        try {
            return ProjectStatusType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + value);
        }
    }
}
