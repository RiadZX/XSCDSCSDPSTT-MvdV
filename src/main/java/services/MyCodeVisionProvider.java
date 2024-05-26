package services;


import com.intellij.codeInsight.codeVision.CodeVisionAnchorKind;
import com.intellij.codeInsight.codeVision.CodeVisionEntry;
import com.intellij.codeInsight.codeVision.CodeVisionRelativeOrdering;
import com.intellij.codeInsight.codeVision.ui.model.ClickableTextCodeVisionEntry;
import com.intellij.codeInsight.hints.codeVision.DaemonBoundCodeVisionProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import kotlin.Unit;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import utils.Controller;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class MyCodeVisionProvider implements DaemonBoundCodeVisionProvider {

    private boolean isLoading;

    @NotNull
    @Override
    public CodeVisionAnchorKind getDefaultAnchor() {
        return CodeVisionAnchorKind.EmptySpace;
    }

    @NotNull
    @Override
    public String getId() {
        return "method.complexity";
    }

    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Method complexity";
    }

    @NotNull
    @Override
    public List<CodeVisionRelativeOrdering> getRelativeOrderings() {
        return List.of(CodeVisionRelativeOrdering.CodeVisionRelativeOrderingFirst.INSTANCE);
    }

    @Override
    public List<kotlin.Pair<TextRange, CodeVisionEntry>> computeForEditor(@NotNull Editor editor, @NotNull PsiFile file) {
        List<kotlin.Pair<TextRange, CodeVisionEntry>> lenses = new ArrayList<>();
        String languageId = file.getLanguage().getID();
        if (!"JAVA".equalsIgnoreCase(languageId) || !Controller.timeActive || !Controller.methodTime) {
            return lenses;
        }
        for (PsiElement el : file.getChildren()) addMethodsToLenses(el, lenses);
        return lenses;
    }

    private void addMethodsToLenses(PsiElement el, List<kotlin.Pair<TextRange, CodeVisionEntry>> lenses) {
        if ("METHOD".equals(el.getNode().getElementType().toString())) {
            PsiElement identifierElement = getIdentifierElement(el);
            if (Controller.methodInfoMap.containsKey(el) &&
                Controller.methodInfoMap.get(el) != null &&
                Controller.methodInfoMap.get(el).getTimeComplexity() != null &&
                !Controller.methodInfoMap.get(el).getTimeComplexity().getLongComplexity().isEmpty())
            {
                String hint = Controller.methodInfoMap.get(el).getTimeComplexity().getLongComplexity();
                TextRange range = identifierElement.getTextRange();
                lenses.add(new kotlin.Pair<>(range, new ClickableTextCodeVisionEntry(hint, "method.complexity", (MouseEvent event, Editor editor) -> {onClick(); return Unit.INSTANCE;}, null, hint, hint, List.of())));
            }
        }
        for (PsiElement sub : el.getChildren()) {
            addMethodsToLenses(sub, lenses);
        }
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

    private void onClick() {
        // Do something when the lens is clicked
    }

    public void setLoading(boolean val){
        this.isLoading = val;
    }
}