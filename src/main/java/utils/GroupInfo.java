package utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class GroupInfo {
    private List<MethodInfo> methods;
    private List<GroupInfo> parents;
    private List<GroupInfo> children;
    private Set<String> variables;

    public GroupInfo() {
        this.methods = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }

    public List<GroupInfo> getParents() {
        return parents;
    }

    public void setParents(List<GroupInfo> parents) {
        this.parents = parents;
    }

    public Set<String> getVariables() {
        return variables;
    }

    public void addVariables(Set<String> variables) {
        this.variables.addAll(variables);
    }

    public void addVariables(List<String> variables) {
        this.variables.addAll(variables);
    }

    public void clearVariables() {
        this.variables.clear();
    }

    public List<GroupInfo> getChildren() {
        return children;
    }

    public void setChildren(List<GroupInfo> children) {
        this.children = children;
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
