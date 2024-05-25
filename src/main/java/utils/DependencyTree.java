package utils;

import java.util.ArrayList;
import java.util.List;

import com.intellij.psi.PsiElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class DependencyTree {

    private List<GroupInfo> groups;
    private List<MethodInfo> methods;

    public DependencyTree() {
        this.groups = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public List<GroupInfo> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupInfo> groups) {
        this.groups = groups;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
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

    public MethodInfo getMethodInfo(PsiElement child) {
        for (MethodInfo methodInfo : methods) {
            if (methodInfo.getPsiElement().equals(child)) {
                return methodInfo;
            }
        }
        throw new RuntimeException("Method not found");
    }

    public void updateComplexities() {
        for (GroupInfo groupInfo : groups) {
            System.out.println("Updating complexities for group: " + groupInfo.hashCode());
            groupInfo.updateComplexities();
        }
    }
}
