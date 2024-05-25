package utils;

import java.util.ArrayList;
import java.util.List;

public class Convertinator {
    public static String toPrompt(GroupInfo group) {
        return "";
    }

    private static void findVariables(GroupInfo group) {
        group.clearVariables();
        for (MethodInfo method : group.getMethods()) {
            group.addVariables(getVariablesFromMethod(method));
        }
    }

    private static List<String> getVariablesFromMethod(MethodInfo method) {
        List<String> variables = new ArrayList<>();
        return variables;
    }
}
