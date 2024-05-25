package utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PsiHelper {

    public static List<PsiElement> findMethods(PsiElement element) {
        List<PsiElement> res = new ArrayList<>();
        findMethods(element, res);
        return res;
    }

    private static void findMethods(PsiElement el, List<PsiElement> methods) {
        if ("METHOD".equals(el.getNode().getElementType().toString())) {
            methods.add(el);
        }
        for (PsiElement sub : el.getChildren()) {
            findMethods(sub, methods);
        }
    }

    public static List<PsiElement> findMethodReferences(PsiElement element) {
        List<PsiElement> res = new ArrayList<>();
        findMethodReferences(element, res);
        return res;
    }

    private static void findMethodReferences(PsiElement el, List<PsiElement> methods) {
        if ("REFERENCE_EXPRESSION".equals(el.getNode().getElementType().toString())) {
            if (el.getReference() != null) {
                var ref = el.getReference().resolve();
                if (ref != null) {
                    var node = ref.getNode();
                    if (node != null) {
                        if (node.getElementType().toString().equals("METHOD")) {
                            methods.add(ref);
                        }
                    }
                }
            }
        }
        for (PsiElement sub : el.getChildren()) {
            findMethodReferences(sub, methods);
        }
    }

    @NotNull
    public static PsiFile getCurrentFile() {
        // Get the currently opened project
        Project project = getCurrentProject();
        if (project == null) {
            throw new RuntimeException("No project found");
        }

        // Get the FileEditorManager instance for the project
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);

        // Get the selected editor
        Editor editor = fileEditorManager.getSelectedTextEditor();
        if (editor == null) {
            throw new RuntimeException("No editor found");
        }

        Document document = editor.getDocument();

        // Get the virtual file corresponding to the editor
        VirtualFile virtualFile = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance().getFile(document);
        if (virtualFile == null) {
            throw new RuntimeException("No virtual file found");
        }

        // Get the PsiFile for the virtual file
        return Objects.requireNonNull(PsiManager.getInstance(project).findFile(virtualFile));
    }

    public static Project getCurrentProject() {
        IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(null);
        if (ideFrame != null) {
            return ideFrame.getProject();
        }
        return null;
    }
}
