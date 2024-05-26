package utils;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import utils.DependencyTree.DependencyTree;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    public static final DependencyTree dependencyTree = new DependencyTree();
    public static final Map<PsiElement, MethodInfo> libraryMethodInfoMap = new HashMap<>();
    public static final Map<String, MethodInfo> signartureToMethodInfoMap = new HashMap<>();
    public static boolean methodTime = true;
    public static boolean inlineTime = true;
    public static boolean timeActive = false;
}
