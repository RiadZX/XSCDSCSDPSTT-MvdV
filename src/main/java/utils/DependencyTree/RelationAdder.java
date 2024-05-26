package utils.DependencyTree;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import utils.*;
import utils.GroupInfo.GroupInfo;

import java.util.ArrayList;
import java.util.List;

public class RelationAdder {
    private final DependencyTree dependencyTree;

    public RelationAdder(DependencyTree dependencyTree) {
        this.dependencyTree = dependencyTree;
    }

    public void run() {
        findParentsAndChildren();
    }

    private void findParentsAndChildren() {
        for (MethodInfo methodInfo : dependencyTree.getMethods()) {
            PsiElement psiElement = methodInfo.getPsiElement();
            Pair<List<PsiElement>, List<PsiElement>> references = PsiHelper.findMethodReferences(psiElement);

            // Normal functions
            references.first.forEach(child -> {
                MethodInfo childInfo = dependencyTree.findMethodInfo(child);
                if (Controller.dependencyTree.findMethodInfo(child) != null) {
                    methodInfo.addDependsOn(childInfo);
                    childInfo.addProvidesFor(methodInfo);
                }
            });

            // Library functions
            List<MethodInfo> libraryMethods = new ArrayList<>();
            List<String> methods = new ArrayList<>();
            references.second.forEach(child -> {
                if (child instanceof PsiMethod meth) {
                    String className = meth.getContainingClass() != null ? meth.getContainingClass().getQualifiedName() + "." : "";
                    StringBuilder res = new StringBuilder(meth.getReturnType().getCanonicalText() + " " + className + meth.getName() + "(");
                    for (PsiParameter parameter : meth.getParameterList().getParameters()) {
                        res.append(parameter.getType().getCanonicalText()).append(" ").append(parameter.getName()).append(", ");
                    }
                    res = new StringBuilder(res.substring(0, res.length() - 2) + ")");
                    MethodInfo methInfo;
                    if (Controller.signartureToMethodInfoMap.containsKey(res.toString())) {
                        methInfo = Controller.signartureToMethodInfoMap.get(res.toString());
                    } else {
                        methInfo = new MethodInfo(meth, true);
                        methods.add(res.toString());
                        libraryMethods.add(methInfo);
                        Controller.signartureToMethodInfoMap.put(res.toString(), methInfo);
                    }
                    Controller.libraryMethodInfoMap.put(child, methInfo);
                }
            });


            for (MethodInfo libraryMethod : libraryMethods) {
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.addMethod(libraryMethod);
                groupInfo.updateComplexities();
            }
        }
    }
}
