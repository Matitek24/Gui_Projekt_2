package Interface;

import java.util.List;

public abstract class AbstractCounterService<T> {
    protected abstract List<T> getItems();
    protected abstract void setCounter(int value);
    protected abstract int extractId(T item);

    public void initializeCounter() {
        int max = getItems().stream()
                .mapToInt(this::extractId)
                .max()
                .orElse(0);
        setCounter(max);
    }
}
