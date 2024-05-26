package utils;

import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.List;

public class LibraryMethodInfo {
    private final Complexity timeComplexity;
    private final Complexity spaceComplexity;

    public LibraryMethodInfo(Complexity timeComplexity, Complexity spaceComplexity) {
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
    }

    public Complexity getTimeComplexity() {
        return timeComplexity;
    }

    public Complexity getSpaceComplexity() {
        return spaceComplexity;
    }
}
