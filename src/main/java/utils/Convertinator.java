package utils;

import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.List;

public class Convertinator {
    public static String toPrompt(GroupInfo group) {
        return "";
    }

//    private static void findVariables(GroupInfo group) {
//        group.clearVariables();
//        for (MethodInfo method : group.getMethods()) {
//            group.addVariables(getVariablesFromMethod(method));
//        }
//    }

//    public static List<String> getVariablesFromMethod(MethodInfo method) {
//        List<String> variables = new ArrayList<>();
//        for (PsiElement el : method.getPsiElement().getChildren()) {
//            if ("PARAMETER_LIST".equals(el.getNode().getElementType().toString())) {
//                for (PsiElement sub : el.getChildren()) {
//                    if ("PARAMETER".equals(sub.getNode().getElementType().toString())) {
//                        variables.add(sub.getText());
//                    }
//                }
//            } else if ("CODE_BLOCK".equals(el.getNode().getElementType().toString())) {
//                findVariables(el, variables);
//            }
//        }
//
//        return variables;
//    }
//
//    private static void findVariables(PsiElement el, List<String> variables) {
//        for (PsiElement sub : el.getChildren()) {
//            if ("IDENTIFIER".equals(sub.getNode().getElementType().toString())) {
//                variables.add(sub.getText());
//            }
//            findVariables(sub, variables);
//        }
//    }
}
