package utils.DependencyTree;

import com.intellij.openapi.util.Pair;
import utils.GroupInfo.GroupInfo;
import utils.MethodInfo;

import java.util.*;

class ConnectedComponentsFinder {
    private Stack<MethodInfo> stack;
    private Set<MethodInfo> visited;
    private Set<Pair<GroupInfo, GroupInfo>> connected;
    private List<MethodInfo> graph;

    public ConnectedComponentsFinder(List<MethodInfo> graph) {
        stack = new Stack<>();
        visited = new HashSet<>();
        connected = new HashSet<>();
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

        for (MethodInfo node : graph) {
            for (MethodInfo child : node.getProvidesFor()) {
                if (!node.getGroup().equals(child.getGroup()) && !connected.contains(new Pair<>(node.getGroup(), child.getGroup()))) {
                    node.getGroup().addProvidedFor(child.getGroup());
                    child.getGroup().addDependsOn(node.getGroup());
                    connected.add(new Pair<>(node.getGroup(), child.getGroup()));
                }
            }
        }

        return sccList;
    }

    private void dfs1(MethodInfo node) {
        visited.add(node);
        for (MethodInfo child : node.getProvidesFor()) {
            if (!visited.contains(child)) {
                dfs1(child);
            }
        }
        stack.push(node);
    }

    private Map<MethodInfo, List<MethodInfo>> reverseGraph(List<MethodInfo> graph) {
        Map<MethodInfo, List<MethodInfo>> reversedGraph = new HashMap<>();
        for (MethodInfo node : graph) {
            for (MethodInfo child : node.getProvidesFor()) {
                reversedGraph.computeIfAbsent(child, k -> new ArrayList<>()).add(node);
            }
        }
        return reversedGraph;
    }

    private void dfs2(MethodInfo node, Map<MethodInfo, List<MethodInfo>> reversedGraph, GroupInfo scc) {
        visited.add(node);
        scc.getMethods().add(node);
        node.linkGroup(scc);
        for (MethodInfo parent : reversedGraph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(parent)) {
                dfs2(parent, reversedGraph, scc);
            }
        }
    }
}