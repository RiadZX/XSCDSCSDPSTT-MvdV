package services;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.Complexity;
import utils.MethodInfo;

import javax.swing.*;
import java.awt.*;

public class MyGutterIconProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement el) {

        PsiElement refEl = getReferenceElement(el);
        if (refEl!=null){
            //handling a reference element
            if (MethodInfo.methodInfoMap.get(refEl) == null) return null;
            Complexity timeComplexity = MethodInfo.methodInfoMap.get(refEl).getTimeComplexity();
            if (timeComplexity == null) return null;
            PsiElement identifierElement = getIdentifierElement(el);
            if (identifierElement == null) return null;
            return new MyLineMarkerInfo(identifierElement, timeComplexity.getShortComplexity(), timeComplexity.getColor(), timeComplexity.getLongComplexity());
        } else {
            //handling something else
            if (isValidElement(el)) {
                if (MethodInfo.methodInfoMap.get(el) == null) return null;
                Complexity timeComplexity = MethodInfo.methodInfoMap.get(el).getTimeComplexity();
                if (timeComplexity == null) return null;
                PsiElement identifierElement = getIdentifierElement(el);
                if (identifierElement == null) return null;
                return new MyLineMarkerInfo(identifierElement, timeComplexity.getShortComplexity(), timeComplexity.getColor(), timeComplexity.getLongComplexity());
            }
        }
        return null;
    }

    private PsiElement getIdentifierElement(PsiElement el) {
       for (PsiElement child : el.getChildren()) {
           if(child.getNode()==null) return null;
           if ("IDENTIFIER".equals(child.getNode().getElementType().toString())) {
               return child;
           }
       }
        return null;
    }

    private PsiElement getReferenceElement(PsiElement el) {
        if ("REFERENCE_EXPRESSION".equals(el.getNode().getElementType().toString())) {
            if (el.getReference() != null) {
                return el.getReference().resolve();
            }
        }
        return null;
    }

    private String getComplexity(PsiElement el) {
//        return el.getText();
    return "O(n\u00B2)";
    }

    private boolean isValidElement(PsiElement el) {
        return "METHOD".equals(el.getNode().getElementType().toString());

    }

    private static class MyLineMarkerInfo extends LineMarkerInfo<PsiElement> {
        private final String text;
        private final JBColor color;

        MyLineMarkerInfo(@NotNull PsiElement element, String text, JBColor color, String hoverText) {
            //Loop through the element's children to find the Identifier, this is the element where the code complexity will be written to.


            super(
                    element,
                    element.getTextRange(),
                    AllIcons.General.Information,
                    (Function<PsiElement, String>) psiElement -> hoverText,
                    null,
                    GutterIconRenderer.Alignment.RIGHT,
                    () -> text
            );

            this.text = text;
            this.color = color;
        }

        @Override
        public GutterIconRenderer createGutterRenderer() {
            return new CustomGutterIconRenderer(this, text, color);
        }
    }


    private static class CustomGutterIconRenderer extends LineMarkerInfo.LineMarkerGutterIconRenderer<PsiElement> {
        private final String text;
        private final JBColor color;

        CustomGutterIconRenderer(LineMarkerInfo<PsiElement> info, String text, JBColor color) {
            super(info);
            this.text = text;
            this.color = color;
        }

        @NotNull
        @Override
        public Icon getIcon() {
            return new TextIcon(text, color);
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
        private final JBColor color;

        TextIcon(String text, JBColor color) {
            this.text = text;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
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
