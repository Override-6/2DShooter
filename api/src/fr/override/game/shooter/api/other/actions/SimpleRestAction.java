package fr.override.game.shooter.api.other.actions;

import java.util.function.Consumer;

public class SimpleRestAction<T> extends SimpleAction implements RestAction<T>, RestActionCompleter<T> {

    private final T obj;
    private Consumer<T> then;

    public SimpleRestAction(T obj) {
        this.obj = obj;
    }

    @Override
    public T get() {
        return obj;
    }

    @Override
    public void then(Consumer<T> action) {
        then = action;
    }

    @Override
    public void onActionCompleted(T obj) {
        super.onActionCompleted();
        if (then != null)
            then.accept(obj);
    }
}