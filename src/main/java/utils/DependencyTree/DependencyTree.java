package utils.DependencyTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
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

    public DependencyTree() {
        this.groups = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.methodInfoMap = new HashMap<>();
    }

    public void updateAll() {
        System.out.println("Updating full dependency tree complexities");

        System.out.println("Updating relations");
        RelationAdder relationAdder = new RelationAdder(this);
        relationAdder.run();

        showMethods();

        System.out.println("Updating groups");
        ConnectedComponentsFinder grouper = new ConnectedComponentsFinder(methods);
        this.groups = grouper.run();

        showGroups();

        System.out.println("Updating complexities");
        ComplexityUpdater complexityUpdater = new ComplexityUpdater(this);
        complexityUpdater.run();
    }

    public void showMethods() {
        System.out.println("All methods:");
        for (MethodInfo methodInfo : methods) {
            System.out.println("Method: "+methodInfo.getMethodSignature());
            System.out.println(methodInfo.getPsiElement().getText());
        }
    }

    public void showGroups() {
        System.out.println("All groups:");
        for (GroupInfo groupInfo : groups) {
            System.out.println("Group: "+groupInfo.getMethodSignatures());
        }
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

    public void setMethods(List<MethodInfo> methods) {
        this.methods.clear();
        this.methodInfoMap.clear();
        this.methods.addAll(methods);
        this.methods.forEach(methodInfo -> {
            methodInfoMap.put(methodInfo.getPsiElement(), methodInfo);
            methodInfo.setOutdated(true);
        });
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
