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
import java.util.Objects;

@Service(Service.Level.PROJECT)
public final class ScanFileService {

    public void scanFile(PsiFile file) {
        if (!"JAVA".equals(file.getLanguage().getID())) {
            throw new RuntimeException("Only Java files are supported");
        }


    }

    private void scanElement(PsiElement el) {
        // check if element is of type method
//        if (el.)
        for (PsiElement sub : el.getChildren()) {
            scanElement(sub);
        }
    }

    private void parseMethod(PsiElement meth) {

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
