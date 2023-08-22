package ru.ermolaayyyyyyy.leschats.dataaccesslayer.models;

public enum Color {
    BLACK,
    WHITE,
    GRAY,
    RED,
    BROWN;

    public static Color findColor(String color){
        for (Color c : Color.values()) {
            if (c.name().equals(color)) {
                return c;
            }
        }
        return null;
    }
}
