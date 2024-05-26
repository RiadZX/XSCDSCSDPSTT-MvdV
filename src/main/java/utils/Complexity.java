package utils;

import com.intellij.ui.JBColor;

public class Complexity {

    private String longComplexity;
    private String shortComplexity;
    private JBColor color;

    public Complexity(String shortComplexity, String longComplexity, JBColor color){
        this.shortComplexity = shortComplexity;
        this.longComplexity = longComplexity;
        this.color = color;
    }

    public Complexity(String shortComplexity, String longComplexity, String color) {
        setComplexities(shortComplexity, longComplexity);
        setColor(color);
    }

    public void setComplexities(String shortComplexity, String longComplexity){
        shortComplexity = parseComplexity(shortComplexity);
        longComplexity = parseComplexity(longComplexity);

        this.shortComplexity = shortComplexity;
        this.longComplexity = longComplexity;
    }

    private String parseComplexity(String value){
        value = value.trim();
        if (!value.startsWith("O(") && value.endsWith(")")) {
            throw new RuntimeException("No time complexity found in '"+value+"'!");
        }
        return value;
    }

    public String getLongComplexity() {
        return this.longComplexity;
    }

    public String getShortComplexity() {
        return this.shortComplexity;
    }

    public JBColor getColor() {
        return color;
    }

    public void setColor(String color){
        this.color = parseColor(color);
    }

    private JBColor parseColor(String color) {
        color = color.trim();
        switch (color) {
            case "red" -> {
                return JBColor.RED;
            }
            case "orange" -> {
                return JBColor.ORANGE;
            }
            case "white" -> {
                return JBColor.WHITE;
            }
            case "yellow" -> {
                return JBColor.YELLOW;
            }
            case "green" -> {
                return JBColor.GREEN;
            }
            case "blue" -> {
                return JBColor.BLUE;
            }
            case "pink" -> {
                return JBColor.PINK;
            }
            default -> throw new RuntimeException("Color '" + color + "' not recognized!");
        }
    }

    public boolean isKnown() {
        return shortComplexity != null && longComplexity != null && color != null;
    }
}
