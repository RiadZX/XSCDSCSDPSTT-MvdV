package services;

import com.intellij.openapi.components.Service;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import utils.*;

import java.util.List;

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

        dependencyTree.updateComplexities();
        Controller.timeActive = true;
        PsiHelper.resetAnnotationsAndStuff();
    }
}
