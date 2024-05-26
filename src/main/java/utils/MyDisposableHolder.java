package utils;
import com.intellij.openapi.Disposable;
import org.jetbrains.annotations.NotNull;

public class MyDisposableHolder implements Disposable {
    private boolean isDisposed = false;

    @Override
    public void dispose() {
        isDisposed = true;
    }

    public boolean isDisposed() {
        return isDisposed;
    }
}

