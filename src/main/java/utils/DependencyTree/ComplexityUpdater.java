package utils.DependencyTree;

import com.intellij.openapi.application.ReadAction;
import com.intellij.util.concurrency.AppExecutorUtil;
import utils.GroupInfo.GroupInfo;
import utils.PsiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class ComplexityUpdater {
    private final DependencyTree dependencyTree;
    private final Set<GroupInfo> updated;
    private final ExecutorService executor;
    private final Queue<Future<?>> futures;

    public ComplexityUpdater(DependencyTree dependencyTree) {
        this.dependencyTree = dependencyTree;
        updated = new CopyOnWriteArraySet<>();
        this.futures = new ConcurrentLinkedQueue<Future<?>>();
        executor = AppExecutorUtil.createBoundedApplicationPoolExecutor("GroupUpdaterPool", 10);
    }

    public void run() {
        futures.clear();
        updateComplexities();
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PsiHelper.resetIdeAnnotations();
    }

    private void updateComplexities() {
        for (GroupInfo groupInfo : dependencyTree.getTopLevelGroups()) {
            updateComplexities(groupInfo);
        }
    }

    private void updateComplexities(GroupInfo groupInfo) {
        Future<?> f = executor.submit(() -> {
                updated.add(groupInfo);
                if (groupInfo.isOutdated()) {
                    ReadAction.run(() -> {
                        System.out.println("Updating complexities for group: (" + groupInfo.getMethodSignatures() + ")");
                        groupInfo.updateComplexities();
                    });
                }
                for (GroupInfo child : groupInfo.getProvidesFor()) {
                    if (!updated.contains(child)) {
                        updateComplexities(child);
                    }
                }
        });
        futures.add(f);
    }
}
