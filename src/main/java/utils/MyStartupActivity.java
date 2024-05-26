package utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.ProjectLifecycleListener;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

public class MyStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        MyDisposableHolder disposable = new MyDisposableHolder();
        PsiManager psiManager = PsiManager.getInstance(project);
        psiManager.addPsiTreeChangeListener(new MethodCodeBlockChangeListener(), disposable);

        // Ensure the disposable is disposed of when the project is closed
        project.getMessageBus().connect(disposable).subscribe(ProjectLifecycleListener.TOPIC, new ProjectLifecycleListener() {
            public void projectClosing(@NotNull Project project) {
                disposable.dispose();
            }
        });
    }
}

