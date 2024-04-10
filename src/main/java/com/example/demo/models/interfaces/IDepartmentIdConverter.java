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
    static long convertDepartmentStringToId(String departmentId) {
        return switch (departmentId) {
            case "IAT" -> 1;
            case "TECH" -> 2;
            case "MATH" -> 3;
            case "KIN" -> 4;
            case "CMPT" -> 5;
            case "CMNS" -> 6;
            case "ENSC" -> 7;
            case "REM" -> 8;
            case "WKTM" -> 9;
            case "MACM" -> 10;
            case "DDP" -> 11;
            case "IART" -> 12;
            case "CHIN" -> 13;
            case "MSE" -> 14;
            default -> 0;
        };
    }
}
