package services;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.nio.file.Path;

@Service(Service.Level.PROJECT)
public final class ScanFileService {

    public void scanCurrentFile() {
        PsiFile psiFile = getCurrentFile();
        System.out.println(psiFile);
    }

    private static PsiFile getCurrentFile() {
        Document currentDoc = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        String fileName = currentFile.getPath()

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
            return null;
        }

        Document document = editor.getDocument();

        // Get the virtual file corresponding to the editor
        VirtualFile virtualFile = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance().getFile(document);
        if (virtualFile == null) {
            return null;
        }

        // Get the PsiFile for the virtual file
        return PsiManager.getInstance(project).findFile(virtualFile);
    }

    private static Project getCurrentProject() {
        IdeFrame ideFrame = WindowManager.getInstance().getIdeFrame(null);
        if (ideFrame != null) {
            return ideFrame.getProject();
        }
        return null;
    }
}
