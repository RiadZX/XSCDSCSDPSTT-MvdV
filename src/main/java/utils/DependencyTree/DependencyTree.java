package utils.DependencyTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intellij.psi.PsiElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import utils.GroupInfo.GroupInfo;
import utils.MethodInfo;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class DependencyTree {

    private List<GroupInfo> groups;
    private final List<MethodInfo> methods;
    private final Map<PsiElement, MethodInfo> methodInfoMap;
    private final RelationAdder relationAdder;
    private final ConnectedComponentsFinder grouper;
    private final ComplexityUpdater complexityUpdater;

    public DependencyTree() {
        this.groups = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.methodInfoMap = new HashMap<>();
        this.relationAdder = new RelationAdder(this);
        this.grouper = new ConnectedComponentsFinder(methods);
        this.complexityUpdater = new ComplexityUpdater(this);
    }

    public void updateAll() {
        relationAdder.run();
        this.groups = grouper.run();
        complexityUpdater.run();
    }

    public List<GroupInfo> getGroups() {
        return groups;
    }

    public List<GroupInfo> getTopLevelGroups() {
        List<GroupInfo> topLevelGroups = new ArrayList<>();
        for (GroupInfo groupInfo : groups) {
            if (groupInfo.getDependsOn().isEmpty()) {
                topLevelGroups.add(groupInfo);
            }
        }
        return topLevelGroups;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void addMethods(List<MethodInfo> methods) {
        this.methods.addAll(methods);
        this.methods.forEach(methodInfo -> methodInfoMap.put(methodInfo.getPsiElement(), methodInfo));
    }

    public void addMethod(MethodInfo methodInfo) {
        this.methods.add(methodInfo);
        this.methodInfoMap.put(methodInfo.getPsiElement(), methodInfo);
        relationAdder.run();
        this.groups = grouper.run();
        methodInfo.setOutdated(true);
    }

    public void removeMethod(@NotNull MethodInfo methodInfo) {
        this.methods.remove(methodInfo);
        this.methodInfoMap.remove(methodInfo.getPsiElement());
        relationAdder.run();
        this.groups = grouper.run();
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

    public MethodInfo findMethodInfo(PsiElement child) {
        for (MethodInfo methodInfo : methods) {
            if (methodInfo.getPsiElement().equals(child)) {
                return methodInfo;
            }
        }
        return null;
    }
}
