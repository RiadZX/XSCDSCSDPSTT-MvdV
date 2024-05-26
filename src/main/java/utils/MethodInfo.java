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

    private List<MethodInfo> dependsOn;
    private List<MethodInfo> providesFor;

    private boolean isUpdating;
    private boolean isOutdated;

    public MethodInfo(PsiElement psiElement) {
        this.psiElement = psiElement;
        this.dependsOn = new ArrayList<>();
        this.providesFor = new ArrayList<>();
    }

    public boolean isOutdated() {
        return isOutdated;
    }

    public void setOutdated(boolean outdated) {
        isOutdated = outdated;
    }

    public String getMethodSignature() {
        return psiElement.getText().substring(0, psiElement.getText().indexOf(')') + 1);
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

    public void setUpdating(boolean val){
        this.isUpdating = val;
    }

    public boolean isUpdating(){
        return this.isUpdating;
    }
}
