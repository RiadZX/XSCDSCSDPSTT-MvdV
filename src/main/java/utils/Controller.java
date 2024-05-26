package utils;

import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    public static final Map<PsiElement, MethodInfo> methodInfoMap = new HashMap<>();
    public static final Map<PsiElement, LibraryMethodInfo> libraryMethodInfoMap = new HashMap<>();
    public static final Map<String, LibraryMethodInfo> signartureToMethodInfoMap = new HashMap<>();
    public static boolean methodTime = true;
    public static boolean inlineTime = true;
    public static boolean timeActive = false;
}
