package utils.DependencyTree;

import com.intellij.openapi.application.ReadAction;
import com.intellij.util.concurrency.AppExecutorUtil;
import utils.GroupInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class ComplexityUpdater {
    private final DependencyTree dependencyTree;

    public ComplexityUpdater(DependencyTree dependencyTree) {
        this.dependencyTree = dependencyTree;
    }

    public void run() {
        updateComplexities();
    }

    private void updateComplexities() {
//        try (
//                ExecutorService executor = AppExecutorUtil.createBoundedApplicationPoolExecutor("GroupUpdaterPool", 10);
//                ) {
            for (GroupInfo groupInfo : dependencyTree.getGroups()) {
                //executor.submit(() -> {
                      //  ReadAction.run(() -> {
                            System.out.println("Updating complexities for group: " + groupInfo.hashCode());
                            groupInfo.updateComplexities();
                  //      });
                //});
            //}
        }
    }
}
