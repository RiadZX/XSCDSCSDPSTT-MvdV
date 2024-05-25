package utils;

public class Complexity {

    private String value;

    public Complexity(String value){
        this.value = value;
    }

    public static Complexity getComplexity(String thing){
        if(thing.startsWith("O(") && thing.endsWith(")")){
            return new Complexity(thing);
        }
        throw new RuntimeException("L bozo (404: No time complexity in prompt found)");
    }

    public String getValue(){
        return this.value;
    }
}
