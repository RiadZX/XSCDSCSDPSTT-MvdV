package com.github.riadzx.xscdscsdpsttmvdv.services;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.nio.file.Path;

public class ScanFileService {


    public void scanFile(Path path) throws Exception {
        PsiFile psiFile = getPsiFile(path);
        System.out.println(psiFile);
    }

    public PsiFile getPsiFile(Path path) throws Exception {
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(path.toFile());
        if (virtualFile != null) {
            return PsiManager.getInstance(project).findFile(virtualFile);
        }
        throw new Exception("File not found");
    }
}
