package fr.override.game.shooter.actions;

public interface Action {

    void then(Runnable runnable);

    static SimpleAction build() {
        return new SimpleAction();
    }
}
