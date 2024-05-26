package utils;

import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.List;

public class LibraryMethodInfo {
    private Complexity timeComplexity;
    private Complexity spaceComplexity;

    public LibraryMethodInfo() {
    }

    public Complexity getTimeComplexity() {
        return timeComplexity;
    }

    public Complexity getSpaceComplexity() {
        return spaceComplexity;
    }

    public void setTimeComplexity(Complexity timeComplexity) {
        this.timeComplexity = timeComplexity;
    }

    public void setSpaceComplexity(Complexity spaceComplexity) {
        this.spaceComplexity = spaceComplexity;
    }
}
