package fr.override.game.shooter.actions;

import java.util.function.Consumer;

public interface RestAction<T> extends Action {

    T get();

    void then(Consumer<T> consumer);

    static <T> SimpleRestAction<T> build(T t) {
        return new SimpleRestAction<>(t);
    }
}