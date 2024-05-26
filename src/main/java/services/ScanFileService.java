package services;

import com.intellij.openapi.components.Service;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import utils.*;

import java.util.List;

import static utils.Controller.dependencyTree;

@Service(Service.Level.PROJECT)
public final class ScanFileService {

    public void scanFile(PsiFile file) {
        System.out.println("Scanning file: " + file.getName());
        if (!"JAVA".equals(file.getLanguage().getID())) {
            throw new RuntimeException("Only Java files are supported");
        }

        List<PsiElement> elements = PsiHelper.findMethods(file);

        dependencyTree.setMethods(elements.stream().map(el -> new MethodInfo(el, false)).toList());
        dependencyTree.updateAll();

        Controller.timeActive = true;
    }
}
