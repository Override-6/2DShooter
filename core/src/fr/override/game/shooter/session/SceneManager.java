package fr.override.game.shooter.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import fr.override.game.shooter.actions.Action;
import fr.override.game.shooter.actions.ActionCompleter;
import fr.override.game.shooter.actions.SimpleAction;

import java.util.*;

public class SceneManager {

    private final SortedSet<GameSessionObject> objects = new TreeSet<>();
    private boolean updating = false;
    private final List<ObjectRemovalTicket> tickets = new ArrayList<ObjectRemovalTicket>(){
        @Override
        public boolean remove(Object o) {
            super.remove(o);
            System.out.println("object removed !");
            return false;
        }

        @Override
        public void clear() {
            super.clear();
            System.out.println("cleared !");
        }

        @Override
        public boolean add(ObjectRemovalTicket ticket) {
            super.add(ticket);
            System.out.println("added object !");
            return false;
        }
    };

    public void addObject(GameSessionObject object) {
        objects.add(object);
        if (updating) {
            object.update(Gdx.graphics.getDeltaTime());
        }
    }

    public Action removeObject(GameSessionObject object) {
        SimpleAction action = Action.build();
        ObjectRemovalTicket ticket = new ObjectRemovalTicket(action, object);
        tickets.add(ticket);
        return action;
    }

    public void update(float deltaTime) {
        updating = true;
        for (GameSessionObject object : new ArrayList<>(objects)) {
            if (checkTickets(object))
                continue;
            object.update(deltaTime);
        }
        handleCollisions();
        updating = false;
    }

    private boolean checkTickets(GameSessionObject object) {
        boolean removed = false;
        for (ObjectRemovalTicket ticket : new ArrayList<>(tickets)) {
            if (ticket.object == object) {
                completeTicket(ticket, !removed);
                tickets.remove(ticket);
                removed = true;
            }
        }
        return removed;
    }

    private void completeTicket(ObjectRemovalTicket ticket, boolean removeGameSession) {
        System.out.println("a");
        objects.remove(ticket.object);
        System.out.println("b");
        if (removeGameSession)
            ticket.object.setGameSession(null);
        System.out.println("c");
        ticket.actionCompleter.onActionCompleted();
        System.out.println("ZD");
    }

    public void render(SpriteBatch batch) {
        new ArrayList<>(objects).forEach(object -> object.render(batch));
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

                if (otherCollidable.getHitBox().overlaps(hitBox)) {
                    collidable.onCollision(otherCollidable);
                    otherCollidable.onCollision(collidable);
                    notHandeleds.remove(object);
                }

            });

        });
    }

    private static class ObjectRemovalTicket {
        private final ActionCompleter actionCompleter;
        private final GameSessionObject object;

        public ObjectRemovalTicket(ActionCompleter actionCompleter, GameSessionObject object) {
            this.actionCompleter = actionCompleter;
            this.object = object;
        }
    }

}