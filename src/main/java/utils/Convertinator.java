package utils;

import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Convertinator {
    public static String toPrompt(GroupInfo group) {
        String prompt = "Give the time complexity(ies) of the following method(s):\n";
        int counter = 0;
        for(MethodInfo method : group.getMethods()){
            prompt += "Method " + counter++ + ": \n";
            prompt += method.getPsiElement().getText() + "\n";
        }

        Map<String, String> knownComplexities = associateComplexityToMethods(group);

        prompt += "The time complexities of the following methods are already known. Please use them: \n";
        for(String key : knownComplexities.keySet()){
            prompt += "Method: " + key + " Complexity: " + knownComplexities.get(key) + "\n";
        }

        return prompt;
    }

    public static Map<String, String> associateComplexityToMethods(GroupInfo group){
        Map<String, String> res = new HashMap<>();
        List<GroupInfo> children = group.getChildren();
        for(GroupInfo child : children){
                List<MethodInfo> methodInfo = child.getMethods();
                for(MethodInfo method : methodInfo){
                    String name = method.getMethod().getText().substring(0, method.getMethod().getText().indexOf(')')+1);
                    String compl = method.getTimeComplexityLong();
                    res.put(name, compl);
                }
        }
        return res;
    }

    public static String addComplexityToMethods(Map<String, String> known){
        return null;
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
