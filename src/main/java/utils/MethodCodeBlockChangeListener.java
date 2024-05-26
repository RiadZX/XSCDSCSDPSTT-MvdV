package utils;

import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import services.ScanFileService;

public class MethodCodeBlockChangeListener extends PsiTreeChangeAdapter {

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent event) {
        PsiElement element = event.getParent();
        if (element != null) {
            if(element.getNode() == null) return;
            if ("java.FILE".equals(element.getNode().getElementType().toString())) {
                new ScanFileService().scanFile(element.getContainingFile());
            }
        }
    }

}
