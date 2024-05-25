package utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import java.util.List;

public class DependencyTreeBuilder {
    private DependencyTree dependencyTree;

    public DependencyTreeBuilder() {
        dependencyTree = new DependencyTree();
    }

    public DependencyTreeBuilder addPSIElements(List<PsiElement> elements) {
        dependencyTree.setMethods(elements.stream().map(MethodInfo::new).toList());
        return this;
    }

    private void buildGroups() {
        Grouper grouper = new Grouper(dependencyTree.getMethods());
        List<GroupInfo> groups = grouper.run();
        dependencyTree.setGroups(groups);
    }

    private void findParentsAndChildren() {
        for (MethodInfo methodInfo : dependencyTree.getMethods()) {
            PsiElement psiElement = methodInfo.getPsiElement();
            PsiHelper.findMethodReferences(psiElement).forEach(child -> {
                // Get the method that is called by child
                PsiReference reference = child.getReference();
                if (reference == null) {
                    throw new RuntimeException("No reference found");
                }
                PsiElement called = reference.resolve();
                System.out.println(called);
            });
        }
        System.out.println("hi");
    }

    public DependencyTree build() {
        buildGroups();
        findParentsAndChildren();
        return dependencyTree;
    }
}
