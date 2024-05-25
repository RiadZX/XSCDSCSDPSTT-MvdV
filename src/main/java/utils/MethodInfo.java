package utils;

import com.intellij.psi.PsiElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class MethodInfo {
    private final PsiElement psiElement;
    private String timeComplexityLong;
    private String timeComplexityShort;
    private String spaceComplexityLong;
    private String spaceComplexityShort;

    private List<MethodInfo> parents;
    private List<MethodInfo> children;

    public MethodInfo(PsiElement psiElement) {
        this.psiElement = psiElement;
        this.timeComplexityLong = "";
        this.timeComplexityShort = "";
        this.spaceComplexityLong = "";
        this.spaceComplexityShort = "";
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public String getTimeComplexityLong() {
        return timeComplexityLong;
    }

    public void setTimeComplexityLong(String timeComplexityLong) {
        this.timeComplexityLong = timeComplexityLong;
    }

    public String getTimeComplexityShort() {
        return timeComplexityShort;
    }

    public void setTimeComplexityShort(String timeComplexityShort) {
        this.timeComplexityShort = timeComplexityShort;
    }

    public String getSpaceComplexityLong() {
        return spaceComplexityLong;
    }

    public void setSpaceComplexityLong(String spaceComplexityLong) {
        this.spaceComplexityLong = spaceComplexityLong;
    }

    public String getSpaceComplexityShort() {
        return spaceComplexityShort;
    }

    public void setSpaceComplexityShort(String spaceComplexityShort) {
        this.spaceComplexityShort = spaceComplexityShort;
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
