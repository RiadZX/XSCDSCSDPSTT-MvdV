package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.util.concurrency.AppExecutorUtil;
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
        return null;
    }

    public void updateComplexities() {
        // Define the number of threads in the pool, you can adjust this based on your needs
        ExecutorService executor = AppExecutorUtil.createBoundedApplicationPoolExecutor("GroupUpdaterPool", 10);
        Semaphore semaphore = new Semaphore(10);  // Limit to 10 concurrent tasks

        try {
            for (GroupInfo groupInfo : groups) {
                semaphore.acquire();  // Acquire a permit before submitting the task
                executor.submit(() -> {
                    try {
                        ReadAction.run(() -> {
                            System.out.println("Updating complexities for group: " + groupInfo.hashCode());
                            groupInfo.updateComplexities();
                        });
                    } finally {
                        semaphore.release();  // Release the permit after task completion
                    }
                });
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Preserve interrupt status
            System.err.println("Task submission interrupted");
        } finally {
            // Initiate an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted
            executor.shutdown();
            try {
                // Wait a while for existing tasks to terminate
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                        System.err.println("Executor did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                executor.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
    }
}
