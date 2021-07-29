package fr.overrride.game.shooter.session.character;

import com.badlogic.gdx.math.Vector2;
import fr.linkit.api.connection.cache.repo.description.annotation.MethodControl;
import fr.overrride.game.shooter.api.other.actions.Action;
import fr.overrride.game.shooter.api.other.actions.ActionCompleter;
import fr.overrride.game.shooter.api.other.actions.SimpleAction;
import fr.overrride.game.shooter.api.session.character.AxisController;
import fr.overrride.game.shooter.api.session.character.Controllable;

import java.util.ArrayList;
import java.util.List;

import static fr.linkit.api.connection.cache.repo.description.annotation.InvocationKind.ONLY_LOCAL;

public class AxisControllerImpl implements AxisController {

    private final Controllable<?> controllable;
    private transient final List<ActionCompleter> yActions = new ArrayList<>();
    private transient final List<ActionCompleter> xActions = new ArrayList<>();

    private int xMills = 0, yMillis = 0;
    private float xAxis = 0, yAxis = 0;

    public AxisControllerImpl(Controllable<?> controllable) {
        this.controllable = controllable;
    }

    @Override
    @MethodControl(ONLY_LOCAL)
    public void update(float deltaTime) {
        Vector2 pos = controllable.getLocation();
        Vector2 velocity = controllable.getVelocity();

        if (xMills > 0) {
            pos.x = xAxis;
            velocity.x = 0;
            xMills -= deltaTime * 60 * 60;
        } else if (xActions != null) {
            xActions.forEach(ActionCompleter::onActionCompleted);
            xActions.clear();
        }

        if (yMillis > 0) {
            pos.y = yAxis;
            velocity.y = 0;
            yMillis -= deltaTime * 60 * 60;
        } else if (yActions != null){
            yActions.forEach(ActionCompleter::onActionCompleted);
            yActions.clear();
        }

    }

    @Override
    public void blockXAxis() {
        blockXAxisFor(Integer.MAX_VALUE);
    }

    @Override
    public Action blockXAxisFor(int millis) {
        xAxis = controllable.getLocation().x;
        xMills = millis;
        SimpleAction action = Action.build();
        xActions.add(action);
        return action;
    }

    @Override
    public void unblockXAxis() {
        xMills = 0;
    }

    @Override
    public void blockYAxis() {
        blockYAxisFor(Integer.MAX_VALUE);
    }

    @Override
    public Action blockYAxisFor(int millis) {
        yAxis = controllable.getLocation().y;
        yMillis = millis;
        SimpleAction action = Action.build();
        yActions.add(action);
        return action;
    }

    @Override
    public void unlockYAxis() {
        yAxis = 0;
    }


}
