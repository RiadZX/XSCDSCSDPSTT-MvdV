package services;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyGutterIconProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement el) {
        if (isValidElement(el)) {
            String complexity = getComplexity(el);
            return new LineMarkerInfo<>(
                    el,
                    el.getTextRange(),
                    AllIcons.Gutter.ImplementingMethod,
                    Pass.UPDATE_ALL,
                    this::getComplexity,
                    null,
                    GutterIconRenderer.Alignment.RIGHT
            );
        }
        return null;
    }
    private String getComplexity(PsiElement el) {
        return el.getText();
//        return "O(n^2)";
    }
    private boolean isValidElement(PsiElement el) {
        return "METHOD".equals(el.getNode().getElementType().toString());
    }
}
