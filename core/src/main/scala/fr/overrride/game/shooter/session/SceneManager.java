package fr.overrride.game.shooter.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import fr.overrride.game.shooter.api.session.character.Collidable;
import fr.overrride.game.shooter.api.session.GameSessionObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SceneManager {

    //   private final Set<GameSessionObject> objects = new TreeSet<>();
    //FIXME some objects does not get removed with a TreeSet
    private final Set<GameSessionObject> objects = new HashSet<>();

    private CyclickTask currentTask = CyclickTask.NOTHING;

    public void addObject(GameSessionObject object) {
        objects.add(object);
        if (currentTask == CyclickTask.UPDATING) {
            object.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void removeObject(GameSessionObject object) {
        objects.remove(object);
        object.setGameSession(null);
    }

    public void update(float deltaTime) {
        currentTask = CyclickTask.UPDATING;
        for (GameSessionObject object : new ArrayList<>(objects)) {
            object.update(deltaTime);
        }
        currentTask = CyclickTask.CHECKING_COLLISIONS;
        handleCollisions();
        currentTask = CyclickTask.NOTHING;
    }

    public void render(SpriteBatch batch) {
        currentTask = CyclickTask.RENDERING;
        new ArrayList<>(objects).forEach(object -> object.render(batch));
        currentTask = CyclickTask.NOTHING;
    }

    public void dispose() {
        new ArrayList<>(objects).forEach(GameSessionObject::dispose);
    }

    private void handleCollisions() {
        List<GameSessionObject> notHandeleds = new ArrayList<>(objects);
        new ArrayList<>(notHandeleds).forEach(object -> {
            if (!(object instanceof Collidable))
                return;
            Collidable collidable = (Collidable) object;
            if (!collidable.canCollide())
                return;

            Rectangle hitBox = collidable.getHitBox();

            new ArrayList<>(notHandeleds).forEach(other -> {

                if (other == object || !(other instanceof Collidable))
                    return;

                Collidable otherCollidable = (Collidable) other;
                if (!otherCollidable.canCollide())
                    return;

                Rectangle otherHitBox = otherCollidable.getHitBox();
                if (otherHitBox.overlaps(hitBox)) {
                    collidable.onCollision(otherCollidable);
                    otherCollidable.onCollision(collidable);
                    notHandeleds.remove(object);
                }

            });

        });
    }
    private enum CyclickTask {
        UPDATING,
        CHECKING_COLLISIONS,
        RENDERING,
        NOTHING

    }

}