package fr.override.game.shooter.character;

import com.badlogic.gdx.math.Vector2;
import fr.override.game.shooter.actions.Action;
import fr.override.game.shooter.actions.ActionCompleter;
import fr.override.game.shooter.actions.SimpleAction;

import java.util.ArrayList;
import java.util.List;

public class AxisController {

    private final Controllable controllable;
    private final List<ActionCompleter> yActions = new ArrayList<>();
    private final List<ActionCompleter> xActions = new ArrayList<>();

    private float xMills = 0, yMillis = 0;
    private float xAxis = 0, yAxis = 0;

    public AxisController(Controllable controllable) {
        this.controllable = controllable;
    }

    public void update(float deltaTime) {
        Vector2 pos = controllable.getLocation();
        Vector2 velocity = controllable.getVelocity();

        if (xMills > 0) {
            pos.x = xAxis;
            velocity.x = 0;
            xMills -= deltaTime * 60 * 60;
        } else {
            xActions.forEach(ActionCompleter::onActionCompleted);
            xActions.clear();
        }

        if (yMillis > 0) {
            pos.y = yAxis;
            velocity.y = 0;
            yMillis -= deltaTime * 60 * 60;
        } else {
            yActions.forEach(ActionCompleter::onActionCompleted);
            yActions.clear();
        }

    }

    public void blockXAxis() {
        blockXAxisFor(Float.MAX_VALUE);
    }

    public Action blockXAxisFor(float millis) {
        xAxis = controllable.getLocation().x;
        xMills = millis;
        SimpleAction action = Action.build();
        xActions.add(action);
        return action;
    }

    public void unblockXAxis() {
        xMills = 0;
    }

    public void blockYAxis() {
        blockYAxisFor(Float.MAX_VALUE);
    }

    public Action blockYAxisFor(float millis) {
        yAxis = controllable.getLocation().y;
        yMillis = millis;
        SimpleAction action = Action.build();
        yActions.add(action);
        return action;
    }

    public void unlockYAxis() {
        yAxis = 0;
    }


}
