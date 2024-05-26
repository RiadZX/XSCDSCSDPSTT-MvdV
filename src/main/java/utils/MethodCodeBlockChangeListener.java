package utils;

import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class MethodCodeBlockChangeListener extends PsiTreeChangeAdapter {

    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent event) {
        PsiElement element = event.getChild();
        if ("METHOD".equals(element.getNode().getElementType().toString())) {
            System.out.println("Added method" + element);
            MethodInfo methodInfo = new MethodInfo(element);
            Controller.dependencyTree.addMethod(methodInfo);
        }
    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent event) {
        PsiElement element = event.getChild();
        if ("METHOD".equals(element.getNode().getElementType().toString())) {
            System.out.println("Removed method" + element);
            MethodInfo methodInfo = Controller.dependencyTree.findMethodInfo(element);
            Controller.dependencyTree.removeMethod(methodInfo);
        }
    }
}
