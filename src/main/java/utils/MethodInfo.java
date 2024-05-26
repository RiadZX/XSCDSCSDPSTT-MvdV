package utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import utils.GroupInfo.GroupInfo;

import java.util.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class MethodInfo {
    private static int idCounter = 0;
    private int id;
    private final PsiMethod psiElement;
    private Complexity timeComplexity;
    private Complexity spaceComplexity;

    private List<MethodInfo> dependsOn;
    private List<MethodInfo> providesFor;
    private final boolean isLibraryMethod;

    public boolean isLibraryMethod() {
        return isLibraryMethod;
    }

    private GroupInfo group;

    private boolean isUpdating;
    private boolean isOutdated;

    public MethodInfo(PsiElement psiElement, boolean isLibraryMethod) {
        this.isLibraryMethod = isLibraryMethod;
        this.psiElement = (PsiMethod) psiElement;
        this.dependsOn = new ArrayList<>();
        this.providesFor = new ArrayList<>();
        this.id = idCounter++;
    }

    public GroupInfo getGroup() {
        return this.group;
    }

    public void linkGroup(GroupInfo group) {
        this.group = group;
    }

    public boolean isOutdated() {
        return isOutdated;
    }

    public void setOutdated(boolean outdated) {
        isOutdated = outdated;
    }

    public String getMethodSignature() {
        PsiMethod meth = this.psiElement;
        String className = meth.getContainingClass() != null ? meth.getContainingClass().getQualifiedName() + "." : "";
        StringBuilder res = new StringBuilder(meth.getReturnType().getCanonicalText() + " " + className + meth.getName() + "(");
        for (PsiParameter parameter : meth.getParameterList().getParameters()) {
            res.append(parameter.getType().getCanonicalText()).append(" ").append(parameter.getName()).append(", ");
        }
        if (meth.getParameterList().getParameters().length > 0) {
            res = new StringBuilder(res.substring(0, res.length() - 2));
        }
        res.append(")");
        return res.toString();
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

    public List<MethodInfo> getProvidesFor() {
        return providesFor;
    }

    public void setProvidesFor(List<MethodInfo> providesFor) {
        this.providesFor = providesFor;
    }

    public void addProvidesFor(MethodInfo parent) {
        providesFor.add(parent);
    }

    public void addDependsOn(MethodInfo child) {
        dependsOn.add(child);
    }

    public List<MethodInfo> getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(List<MethodInfo> dependsOn) {
        this.dependsOn = dependsOn;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof MethodInfo meth) {
            return Objects.equals(this.id, meth.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this.id);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    public void setUpdating(boolean val){
        this.isUpdating = val;
    }

    public boolean isUpdating(){
        return this.isUpdating;
    }
}
