package utils.GroupInfo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import utils.MethodInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class GroupInfo {
    private List<MethodInfo> methods;
    private List<GroupInfo> dependsOn;
    private List<GroupInfo> providesFor;
    private Set<String> variables;

    public GroupInfo() {
        this.methods = new ArrayList<>();
        this.dependsOn = new ArrayList<>();
        this.providesFor = new ArrayList<>();
    }

    public String getMethodSignatures() {
        StringBuilder sb = new StringBuilder();
        for (MethodInfo method : methods){
            sb.append(method.getMethodSignature()).append(", ");
        }
        return sb.toString();
    }

    public boolean isOutdated() {
        for (MethodInfo method : methods) {
            if (method.isOutdated()) {
                return true;
            }
        }
        return false;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }

    public void addMethod(MethodInfo method) {
        this.methods.add(method);
    }

    public List<GroupInfo> getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(List<GroupInfo> dependsOn) {
        this.dependsOn = dependsOn;
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

    public List<GroupInfo> getProvidesFor() {
        return providesFor;
    }

    public List<GroupInfo> getAllSubGroups() {
        List<GroupInfo> allSubGroups = new ArrayList<>();
        for(GroupInfo child : providesFor){
            allSubGroups.add(child);
            allSubGroups.addAll(child.getAllSubGroups());
        }
        return allSubGroups;
    }

    public void setProvidesFor(List<GroupInfo> providesFor) {
        this.providesFor = providesFor;
    }

    public void updateComplexities() {
        TimeComplexityUpdater updater = new TimeComplexityUpdater(this);
        updater.run();
    }

    public MethodInfo findMethodBySignature(String signature) {
        for (MethodInfo method : methods) {
            String text = method.getPsiElement().getText();
            if (text.contains(signature)){
                return method;
            }
        }
        throw new RuntimeException("Method not found with signature '"+signature+"'");
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

    public void addDependsOn(GroupInfo group) {
        this.dependsOn.add(group);
    }

    public void addProvidedFor(GroupInfo group) {
        this.providesFor.add(group);
    }
}
