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

import javax.swing.*;
import java.awt.*;

public class MyGutterIconProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement el) {
        if (isValidElement(el)) {
            String complexity = getComplexity(el);
            return new MyLineMarkerInfo(el, complexity);
        }
        return null;
    }

    private String getComplexity(PsiElement el) {
//        return el.getText();
    return "O(n^2)";
    }

    private boolean isValidElement(PsiElement el) {
        return "METHOD".equals(el.getNode().getElementType().toString());
    }

    private static class MyLineMarkerInfo extends LineMarkerInfo<PsiElement> {
        private final String text;

        MyLineMarkerInfo(@NotNull PsiElement element, String text) {
            //LineMarkerInfo(T, TextRange, Icon, Function, GutterIconNavigationHandler, Alignment, Supplier)

            //@NotNull T element,
            //                        @NotNull TextRange range,
            //                        Icon icon,
            //                        int updatePass,
            //                        @Nullable Function<? super T, String> tooltipProvider,
            //                        @Nullable GutterIconNavigationHandler<T> navHandler,
            //                        @NotNull GutterIconRenderer.Alignment alignment) {
            super(
                    element,
                    element.getTextRange(),
                    AllIcons.General.Information,
                    Pass.UPDATE_ALL,
                    null,
                    null,
                    GutterIconRenderer.Alignment.RIGHT
            );
            this.text = text;
        }

        @Override
        public GutterIconRenderer createGutterRenderer() {
            return new CustomGutterIconRenderer(this, text);
        }
    }


    private static class CustomGutterIconRenderer extends LineMarkerInfo.LineMarkerGutterIconRenderer<PsiElement> {
        private final String text;

        CustomGutterIconRenderer(LineMarkerInfo<PsiElement> info, String text) {
            super(info);
            this.text = text;
        }

        @NotNull
        @Override
        public Icon getIcon() {
            return new TextIcon(text);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof CustomGutterIconRenderer && ((CustomGutterIconRenderer) obj).text.equals(this.text);
        }

        @Override
        public int hashCode() {
            return text.hashCode();
        }
    }

    private static class TextIcon implements Icon {
        private final String text;

        TextIcon(String text) {
            this.text = text;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.GREEN);
            g.drawString(text, x, y + 12);
        }

        @Override
        public int getIconWidth() {
            return text.length() * 7; // Approximate width of the text
        }

        @Override
        public int getIconHeight() {
            return 16;
        }
    }
}
