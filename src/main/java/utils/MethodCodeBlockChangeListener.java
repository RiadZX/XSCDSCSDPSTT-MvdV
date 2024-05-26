package utils;

import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;

public class MethodCodeBlockChangeListener extends PsiTreeChangeAdapter {
    @Override
    public void childrenChanged(PsiTreeChangeEvent event) {
        PsiElement element = event.getParent();
        if (element != null) {
            onMethodCodeBlockChanged(element);
        }
    }

    private void onMethodCodeBlockChanged(PsiElement element) {
        // Implement your logic here
        System.out.println("File has been changed oh no.");
    }
}
