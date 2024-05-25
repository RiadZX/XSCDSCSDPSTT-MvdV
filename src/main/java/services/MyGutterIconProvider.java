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
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        System.out.println("I have been called");
        return new LineMarkerInfo<>(
                element,
                element.getTextRange(),
                AllIcons.Gutter.ImplementingMethod,
                Pass.UPDATE_ALL,
                (Function<PsiElement, String>) PsiElement::getText,
                null,
                GutterIconRenderer.Alignment.RIGHT
        );
    }
}
