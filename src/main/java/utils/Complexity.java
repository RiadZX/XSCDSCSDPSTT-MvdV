package utils;

import com.intellij.ui.JBColor;

public class Complexity {

    private final String longComplexity;
    private final String shortComplexity;
    private final JBColor color;

    public static Complexity fromString(String shortComplexity, String longComplexity, String color){
        shortComplexity = shortComplexity.trim();
        if(!isComplexityValid(shortComplexity)){
            throw new RuntimeException("No time complexity found in '"+shortComplexity+"'!");
        }
        longComplexity = longComplexity.trim();
        if(!isComplexityValid(longComplexity)){
            throw new RuntimeException("No time complexity found in '"+longComplexity+"'!");
        }
        color = color.trim();
        return new Complexity(shortComplexity, longComplexity, getColor(color));
    }

    private static JBColor getColor(String color){
        return switch (color) {
            case "red" -> JBColor.RED;
            case "orange" -> JBColor.ORANGE;
            case "yellow" -> JBColor.YELLOW;
            case "green" -> JBColor.GREEN;
            case "blue" -> JBColor.BLUE;
            case "pink" -> JBColor.PINK;
            default -> throw new RuntimeException("Color '" + color + "' not recognized!");
        };
    }

    private static boolean isComplexityValid(String value){
        return value.startsWith("O(") && value.endsWith(")");
    }

    public Complexity(String shortComplexity, String longComplexity, JBColor color){
        this.shortComplexity = shortComplexity;
        this.longComplexity = longComplexity;
        this.color = color;
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
}
