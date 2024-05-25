package utils;

public class Complexity {

    private String value;

    public static Complexity fromString(String thing){
        thing = thing.trim();
        if(thing.startsWith("O(") && thing.endsWith(")")){
            return new Complexity(thing);
        }
        throw new RuntimeException("No time complexity found in '"+thing+"'!");
    }

    public Complexity(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
