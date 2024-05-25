package utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import java.util.*;

public class DependencyTreeBuilder {
    private final DependencyTree dependencyTree;

    public DependencyTreeBuilder() {
        dependencyTree = new DependencyTree();
    }

    public DependencyTreeBuilder addPSIElements(List<PsiElement> elements) {
        dependencyTree.setMethods(elements.stream().map(MethodInfo::new).toList());
        return this;
    }

    private void buildGroups() {
        Grouper grouper = new Grouper(dependencyTree.getMethods());
        List<GroupInfo> groups = grouper.run();
        dependencyTree.setGroups(groups);
    }

    private void findParentsAndChildren() {
        for (MethodInfo methodInfo : dependencyTree.getMethods()) {
            PsiElement psiElement = methodInfo.getPsiElement();
            PsiHelper.findMethodReferences(psiElement).forEach(child -> {
                MethodInfo childInfo = dependencyTree.getMethodInfo(child);
                methodInfo.addChild(childInfo);
                childInfo.addParent(methodInfo);
            });
        }
    }

    public DependencyTree build() {
        findParentsAndChildren();
        buildGroups();
        return dependencyTree;
    }


    static class Grouper {
        private Stack<MethodInfo> stack;
        private Set<MethodInfo> visited;
        private List<MethodInfo> graph;

        public Grouper(List<MethodInfo> graph) {
            stack = new Stack<>();
            visited = new HashSet<>();
            this.graph = graph;
        }

        public List<GroupInfo> run() {

            // Step 1: First DFS to fill the stack with finish times
            for (MethodInfo node : graph) {
                if (!visited.contains(node)) {
                    dfs1(node);
                }
            }

            // Step 2: Create a reversed graph
            Map<MethodInfo, List<MethodInfo>> reversedGraph = reverseGraph(graph);

            // Step 3: Second DFS based on finish times in decreasing order
            List<GroupInfo> sccList = new ArrayList<>();
            visited.clear();
            while (!stack.isEmpty()) {
                MethodInfo node = stack.pop();
                if (!visited.contains(node)) {
                    GroupInfo scc = new GroupInfo();
                    dfs2(node, reversedGraph, scc);
                    sccList.add(scc);
                }
            }

            return sccList;
        }

        private void dfs1(MethodInfo node) {
            visited.add(node);
            for (MethodInfo child : node.getChildren()) {
                if (!visited.contains(child)) {
                    dfs1(child);
                }
            }
            stack.push(node);
        }

        private Map<MethodInfo, List<MethodInfo>> reverseGraph(List<MethodInfo> graph) {
            Map<MethodInfo, List<MethodInfo>> reversedGraph = new HashMap<>();
            for (MethodInfo node : graph) {
                for (MethodInfo child : node.getChildren()) {
                    reversedGraph.computeIfAbsent(child, k -> new ArrayList<>()).add(node);
                }
            }
            return reversedGraph;
        }

        private void dfs2(MethodInfo node, Map<MethodInfo, List<MethodInfo>> reversedGraph, GroupInfo scc) {
            visited.add(node);
            scc.getMethods().add(node);
            for (MethodInfo parent : reversedGraph.getOrDefault(node, new ArrayList<>())) {
                if (!visited.contains(parent)) {
                    dfs2(parent, reversedGraph, scc);
                }
            }
        }
    }
}