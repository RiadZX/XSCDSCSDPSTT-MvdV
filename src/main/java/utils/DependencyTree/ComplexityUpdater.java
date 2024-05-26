package utils.DependencyTree;

import com.intellij.openapi.application.ReadAction;
import com.intellij.util.concurrency.AppExecutorUtil;
import utils.GroupInfo.GroupInfo;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;

class ComplexityUpdater {
    private final DependencyTree dependencyTree;
    private final Set<GroupInfo> updated;
    private final ExecutorService executor;

    public ComplexityUpdater(DependencyTree dependencyTree) {
        this.dependencyTree = dependencyTree;
        updated = new CopyOnWriteArraySet<>();
        executor = AppExecutorUtil.createBoundedApplicationPoolExecutor("GroupUpdaterPool", 10);
    }

    public void run() {
        updateComplexities();
    }

    private void updateComplexities() {
        for (GroupInfo groupInfo : dependencyTree.getTopLevelGroups()) {
            updateComplexities(groupInfo);
        }
    }

    private void updateComplexities(GroupInfo groupInfo) {
        executor.submit(() -> {
//            try {
                updated.add(groupInfo);
                if (!groupInfo.isOutdated()) {
                    ReadAction.run(() -> {
                        System.out.println("Updating complexities for group: (" + groupInfo.getMethodSignatures() + ")");
//                        Thread.sleep(2000);
                        groupInfo.updateComplexities();
                    });
                }
                for (GroupInfo child : groupInfo.getProvidesFor()) {
                    if (!updated.contains(child)) {
                        updateComplexities(child);
                    }
                }
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        });
    }
}
