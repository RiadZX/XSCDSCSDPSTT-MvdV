package utils;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

    public static Pair<List<PsiElement>, List<PsiElement>> findMethodReferences(PsiElement element) {
        List<PsiElement> res = new ArrayList<>();
        List<PsiElement> compiled = new ArrayList<>();
        findMethodReferences(element, res, compiled);
        return new Pair<>(res, compiled);
    }

    public static void resetIdeAnnotations() {
        System.out.println("Updating UI");
        Project project = PsiHelper.getCurrentProject();
        PsiManager.getInstance(project).dropPsiCaches();
        DaemonCodeAnalyzer.getInstance(project).restart();
    }

    private static void findMethodReferences(PsiElement el, List<PsiElement> methods, List<PsiElement> compiled) {
        if ("REFERENCE_EXPRESSION".equals(el.getNode().getElementType().toString())) {
            if (el.getReference() != null) {
                var ref = el.getReference().resolve();
                if (ref != null) {
                    var node = ref.getNode();
                    if (node != null) {
                        if (node.getElementType().toString().equals("METHOD")) {
                            methods.add(ref);
                        }
                    } else  {
                        compiled.add(ref);
                    }
                }
            }
        }
        for (PsiElement sub : el.getChildren()) {
            findMethodReferences(sub, methods, compiled);
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

    //    private static void findVariables(GroupInfo group) {
//        group.clearVariables();
//        for (MethodInfo method : group.getMethods()) {
//            group.addVariables(getVariablesFromMethod(method));
//        }
//    }

//    public static List<String> getVariablesFromMethod(MethodInfo method) {
//        List<String> variables = new ArrayList<>();
//        for (PsiElement el : method.getPsiElement().getChildren()) {
//            if ("PARAMETER_LIST".equals(el.getNode().getElementType().toString())) {
//                for (PsiElement sub : el.getChildren()) {
//                    if ("PARAMETER".equals(sub.getNode().getElementType().toString())) {
//                        variables.add(sub.getText());
//                    }
//                }
//            } else if ("CODE_BLOCK".equals(el.getNode().getElementType().toString())) {
//                findVariables(el, variables);
//            }
//        }
//
//        return variables;
//    }
//
//    private static void findVariables(PsiElement el, List<String> variables) {
//        for (PsiElement sub : el.getChildren()) {
//            if ("IDENTIFIER".equals(sub.getNode().getElementType().toString())) {
//                variables.add(sub.getText());
//            }
//            findVariables(sub, variables);
//        }
//    }
}
