package fr.overrride.game.shooter.session.levels;

import com.badlogic.gdx.graphics.Color;
import fr.linkit.api.gnom.network.ExecutorEngine;
import fr.linkit.api.internal.concurrency.Procrastinator;
import fr.linkit.engine.internal.concurrency.pool.HiringBusyWorkerPool;
import fr.overrride.game.shooter.api.session.GameSession;
import fr.overrride.game.shooter.api.session.GameSessionObject;
import fr.overrride.game.shooter.api.session.comps.RectangleComponent;
import fr.overrride.game.shooter.api.session.levels.Level;
import fr.overrride.game.shooter.session.PlayState;
import fr.overrride.game.shooter.session.components.DangerousComponent;
import fr.overrride.game.shooter.session.components.HealingComponent;
import fr.overrride.game.shooter.session.items.Item;
import fr.overrride.game.shooter.session.items.ItemType;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultLevel extends Level {

    private static final int ITEM_SPAWN_MAX = 10 * 1000;
    private static final int ITEM_SPAWN_MIN = 4 * 1000;
    private static final int ITEM_LIFETIME_MIN = 1 * 1000;
    private static final int ITEM_LIFETIME_MAX = 3 * 1000;

    @Override
    protected Set<GameSessionObject> buildLevel() {
        Set<GameSessionObject> components = new HashSet<>();
        components.add(new RectangleComponent(75, 800, 400, 50, Color.WHITE));
        components.add(new RectangleComponent(1440, 800, 400, 50, Color.WHITE));
        components.add(new HealingComponent(900, 700, 100, 100, 12));
        components.add(new RectangleComponent(750, 450, 400, 50, Color.WHITE));
        components.add(new DangerousComponent(1600, 350, 50, 30, 25));
        components.add(new DangerousComponent(250, 350, 50, 30, 25));
        components.add(new RectangleComponent(250, 76, 50, 275, Color.WHITE));
        components.add(new RectangleComponent(1600, 76, 50, 275, Color.WHITE));
        if (false && ExecutorEngine.currentEngine().isServer())
            startItemSpawning();
        return components;
    }

    private void startItemSpawning() {
        new Thread(() -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            for (; ; ) {
                try {
                    int spawnSleep = random.nextInt(ITEM_SPAWN_MIN, ITEM_SPAWN_MAX);
                    sleep(spawnSleep);
                    Item item = spawnItem();
                    if (item == null)
                        continue;
                    int lifeTime = random.nextInt(ITEM_LIFETIME_MIN, ITEM_LIFETIME_MAX);
                    sleep(lifeTime);
                    item.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Item Spawn").start();
    }

    private Item spawnItem() {
        Optional<GameSession> opt = getCurrentGameSession();
        if (!opt.isPresent())
            return null;
        GameSession session = opt.get();
        ItemType[] types = ItemType.values();
        ThreadLocalRandom threadRandom = ThreadLocalRandom.current();
        ItemType type = types[threadRandom.nextInt(types.length)];
        Item item = new Item(800, 525, 3, type);
        item.setGameSession(session);
        return item;
    }


    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onUpdate(float deltaTime) {
    }
}
