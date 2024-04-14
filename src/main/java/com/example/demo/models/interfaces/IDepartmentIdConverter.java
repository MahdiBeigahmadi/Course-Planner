package com.example.demo.models.interfaces;

public interface IDepartmentIdConverter {
    static String checkDepartmentID(long departmentId) {
        return switch ((int) departmentId) {
            case 1 -> "IAT";
            case 2 -> "TECH";
            case 3 -> "MATH";
            case 4 -> "KIN";
            case 5 -> "CMPT";
            case 6 -> "CMNS";
            case 7 -> "ENSC";
            case 8 -> "REM";
            case 9 -> "WKTM";
            case 10 -> "MACM";
            case 11 -> "DDP";
            case 12 -> "IART";
            case 13 -> "CHIN";
            case 14 -> "MSE";
            default -> "Failed";
        };
    }
}
