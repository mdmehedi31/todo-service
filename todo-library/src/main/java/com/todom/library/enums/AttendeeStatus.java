package com.todom.library.enums;

public enum AttendeeStatus {

    Not_Sure("Not sure"),
    Accepted("Accepted"),
    Rjected("Rjected"),
    Needs_Action("Need action"),
    Creator("Creator");

    public final String label;

    AttendeeStatus(String label) {
        this.label = label;
    }

    private String getLabel() {
        return label;
    }
}
