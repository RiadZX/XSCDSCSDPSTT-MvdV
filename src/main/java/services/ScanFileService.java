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
        if (!"JAVA".equals(file.getLanguage().getID())) {
            throw new RuntimeException("Only Java files are supported");
        }

        List<PsiElement> elements = PsiHelper.findMethods(file);

        dependencyTree.addMethods(elements.stream().map(MethodInfo::new).toList());
        dependencyTree.updateAll();

        Controller.timeActive = true;
        PsiHelper.resetAnnotationsAndStuff();
    }
}
