package fr.overrride.game.shooter.api.other.actions;

public interface Action {

    void then(Runnable runnable);

    static SimpleAction build() {
        return new SimpleAction();
    }
}
