package services;

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
import utils.Controller;

import javax.swing.*;
import java.awt.*;

public class MyGutterIconProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement el) {
        if (!Controller.timeActive || !Controller.inlineTime) return null;

        PsiElement refEl = null;
        if ("REFERENCE_EXPRESSION".equals(el.getNode().getElementType().toString())) {
            if (el.getReference() != null) {
                refEl = el.getReference().resolve();
            }
        }
        if (refEl!=null){
            //handling a reference element
            return addMarker(el, refEl);
        } else {
            //handling something else
            if (isValidElement(el)) {
                return addMarker(el, el);
            }
        }
        return null;
    }

    @Nullable
    private LineMarkerInfo<?> addMarker(@NotNull PsiElement el, PsiElement refEl) {
        if (Controller.dependencyTree.findMethodInfo(refEl) == null &&
            Controller.libraryMethodInfoMap.get(refEl) == null) return null;
        Complexity timeComplexity = Controller.dependencyTree.findMethodInfo(refEl) != null ?
                Controller.dependencyTree.findMethodInfo(refEl).getTimeComplexity() :
                Controller.libraryMethodInfoMap.get(refEl).getTimeComplexity();
        if (timeComplexity == null || !timeComplexity.isKnown()) return null;
        PsiElement identifierElement = getIdentifierElement(el);
        if (identifierElement == null) return null;
        String gutterText = timeComplexity.getShortComplexity();
        if(Controller.dependencyTree.findMethodInfo(refEl) != null && Controller.dependencyTree.findMethodInfo(refEl).isOutdated()){
            gutterText += " \u27F3";
            timeComplexity.setColor("pink");
        }
        String hoverText = identifierElement.getText();
        hoverText += ": " + timeComplexity.getLongComplexity();
        gutterText = formatPower(gutterText);
        hoverText = formatPower(hoverText);
        return new MyLineMarkerInfo(identifierElement, gutterText, timeComplexity.getColor(), hoverText);
    }

    private String formatPower(@NotNull String complexity){
        //replace ^1,^2, ^3 with superscript
        return complexity.
                replace("^1", "\u00B9")
                .replace("^2", "\u00B2")
                .replace("^3", "\u00B3")
                .replace("^4", "\u2074")
                .replace("^5", "\u2075")
                .replace("^6", "\u2076")
                .replace("^7", "\u2077")
                .replace("^8", "\u2078")
                .replace("^n", "\u207F");
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
