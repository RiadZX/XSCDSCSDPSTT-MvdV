package utils.DependencyTree;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import utils.Controller;
import utils.MethodInfo;
import utils.PsiHelper;

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
                MethodInfo childInfo = dependencyTree.getMethodInfoByPsiElement(child);
                if (Controller.methodInfoMap.containsKey(child)) {
                    methodInfo.addChild(childInfo);
                    childInfo.addParent(methodInfo);
                }
            });

            // Library functions
            references.second.forEach(child -> {
                String name = child.getText();
            });
        }
    }
}
