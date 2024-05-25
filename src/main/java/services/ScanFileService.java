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
import utils.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service(Service.Level.PROJECT)
public final class ScanFileService {

    public void scanFile(PsiFile file) {
        if (!"JAVA".equals(file.getLanguage().getID())) {
            throw new RuntimeException("Only Java files are supported");
        }

        List<PsiElement> elements = PsiHelper.findMethods(file);

        DependencyTree dependencyTree = new DependencyTreeBuilder()
                .addPSIElements(elements)
                .build();

        GroupInfo groupInfo = dependencyTree.getGroups().get(0);
        String prompt = Convertinator.toPrompt(groupInfo);
        ChatGPTAI chatGPTAI = new ChatGPTAI();
        String response = chatGPTAI.getResponse(prompt);
        System.out.println(response);
    }
}
