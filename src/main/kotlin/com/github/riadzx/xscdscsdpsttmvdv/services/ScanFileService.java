package com.github.riadzx.xscdscsdpsttmvdv.services;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFrame;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.nio.file.Path;

public class ScanFileService {


    public void scanFile(Path path) throws Exception {
        PsiFile psiFile = getPsiFile(path);
        System.out.println(psiFile);
    }



    public static PsiFile getCurrentFile() {
        // Get the currently opened project
        Project project = getCurrentProject();
        if (project == null) {
            return null;
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
