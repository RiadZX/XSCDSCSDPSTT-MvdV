package utils;

import com.intellij.psi.PsiElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class MethodInfo {
    private final PsiElement psiElement;
    private Complexity timeComplexity;
    private Complexity spaceComplexity;

    private List<MethodInfo> parents;
    private List<MethodInfo> children;

    public MethodInfo(PsiElement psiElement) {
        this.psiElement = psiElement;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
        Controller.methodInfoMap.put(psiElement, this);
    }

    public Complexity getTimeComplexity() {
        return timeComplexity;
    }

    public void setTimeComplexity(Complexity timeComplexity) {
        this.timeComplexity = timeComplexity;
    }

    public Complexity getSpaceComplexity() {
        return spaceComplexity;
    }

    public void setSpaceComplexity(Complexity spaceComplexity) {
        this.spaceComplexity = spaceComplexity;
    }

    public PsiElement getPsiElement() {
        return psiElement;
    }

    public List<MethodInfo> getChildren() {
        return children;
    }

    public void setChildren(List<MethodInfo> children) {
        this.children = children;
    }

    public void addChild(MethodInfo child) {
        children.add(child);
    }

    public List<MethodInfo> getParents() {
        return parents;
    }

    public void setParents(List<MethodInfo> parents) {
        this.parents = parents;
    }

    public void addParent(MethodInfo parent) {
        parents.add(parent);
    }

    public PsiElement getMethod() {
        return psiElement;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
