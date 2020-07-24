package fr.override.game.shooter.api.other.actions;

public class SimpleAction implements Action, ActionCompleter {
    private Runnable then;

    @Override
    public void then(Runnable action) {
        then = action;
    }

    @Override
    public void onActionCompleted() {
        if (then != null)
            then.run();
    }
}