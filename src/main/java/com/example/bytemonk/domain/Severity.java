package com.example.bytemonk.domain;

public enum Severity {

    Low, Medium, High;

    public static Severity getValue(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        for( Severity severity : Severity.values() ) {
            if ( str.equalsIgnoreCase(severity.name()) ) {
                return severity;
            }
        }
        return null;
    }


}
