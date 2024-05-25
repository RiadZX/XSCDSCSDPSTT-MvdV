package services;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service(Service.Level.PROJECT)
public final class ScanFileService {

    public List<PsiElement> scanFile(PsiFile file) {
        if (!"JAVA".equals(file.getLanguage().getID())) {
            throw new RuntimeException("Only Java files are supported");
        }
        List<PsiElement> res = new ArrayList<>();
        scanElement(file, res);
        return res;
    }

    private void scanElement(PsiElement el, List<PsiElement> methods) {
        if ("METHOD".equals(el.getNode().getElementType().toString())) {
            methods.add(el);
        }
        for (PsiElement sub : el.getChildren()) {
            scanElement(sub, methods);
        }
    }

    private void parseMethods(List<PsiElement> methods) {
        for (PsiElement method : methods) {
            method.getChildren();
        }
    }

    @NotNull
    public PsiFile getCurrentFile() {
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

    private Project getCurrentProject() {
        IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(null);
        if (ideFrame != null) {
            return ideFrame.getProject();
        }
        return null;
    }
}
