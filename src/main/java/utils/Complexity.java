package utils;

import com.intellij.ui.JBColor;

public class Complexity {

    private String value;
    private String shortComplexity;
    private JBColor color;

    public static Complexity fromString(String thing){
        thing = thing.trim();
        if(thing.startsWith("O(") && thing.endsWith(")")){
            return new Complexity(thing, "O(n)", JBColor.GREEN);
        }
        throw new RuntimeException("No time complexity found in '"+thing+"'!");
    }

    public Complexity(String value, String shortComplexity, JBColor color){
        this.value = value;
        this.shortComplexity = shortComplexity;
        this.color = color;
    }

    public String getValue() {
        return this.value;
    }

    public String getShortComplexity() {
        return this.shortComplexity;
    }

    public JBColor getColor() {
        return color;
    }
}
