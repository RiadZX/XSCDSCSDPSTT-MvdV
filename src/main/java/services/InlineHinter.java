package services;

import com.intellij.codeInsight.hints.HintInfo;
import com.intellij.codeInsight.hints.InlayInfo;
import com.intellij.codeInsight.hints.InlayParameterHintsProvider;
import com.intellij.codeInsight.hints.Option;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntaxTraverser;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class InlineHinter implements InlayParameterHintsProvider {
    @Override
    public @NotNull List<InlayInfo> getParameterHints(@NotNull PsiElement element) {
        return InlayParameterHintsProvider.super.getParameterHints(element);
    }

    @Override
    public @NotNull List<InlayInfo> getParameterHints(@NotNull PsiElement element, @NotNull PsiFile file) {
        return InlayParameterHintsProvider.super.getParameterHints(element, file);
    }

    @Override
    public @Nullable HintInfo getHintInfo(@NotNull PsiElement element) {
        return InlayParameterHintsProvider.super.getHintInfo(element);
    }

    @Override
    public @Nullable HintInfo getHintInfo(@NotNull PsiElement element, @NotNull PsiFile file) {
        return InlayParameterHintsProvider.super.getHintInfo(element, file);
    }

    @Override
    public @NotNull Set<String> getDefaultBlackList() {
        return null;
    }

    @Override
    public @Nullable Language getBlackListDependencyLanguage() {
        return InlayParameterHintsProvider.super.getBlackListDependencyLanguage();
    }

    @Override
    public @NotNull List<Option> getSupportedOptions() {
        return InlayParameterHintsProvider.super.getSupportedOptions();
    }

    @Override
    public boolean isBlackListSupported() {
        return InlayParameterHintsProvider.super.isBlackListSupported();
    }

    @Override
    public @Nls String getBlacklistExplanationHTML() {
        return InlayParameterHintsProvider.super.getBlacklistExplanationHTML();
    }

    @Override
    public @NotNull String getInlayPresentation(@NotNull String inlayText) {
        return "O(LOG(N))";
    }

    @Override
    public boolean canShowHintsWhenDisabled() {
        return InlayParameterHintsProvider.super.canShowHintsWhenDisabled();
    }

    @Override
    public String getSettingsPreview() {
        return InlayParameterHintsProvider.super.getSettingsPreview();
    }

    @Override
    public boolean isExhaustive() {
        return InlayParameterHintsProvider.super.isExhaustive();
    }

    @Override
    public String getMainCheckboxText() {
        return InlayParameterHintsProvider.super.getMainCheckboxText();
    }

    @Override
    public @NotNull SyntaxTraverser<PsiElement> createTraversal(@NotNull PsiElement root) {
        return InlayParameterHintsProvider.super.createTraversal(root);
    }

    @Override
    public @Nls String getDescription() {
        return InlayParameterHintsProvider.super.getDescription();
    }

    @Override
    public @Nls @Nullable String getProperty(String key) {
        return InlayParameterHintsProvider.super.getProperty(key);
    }
}
