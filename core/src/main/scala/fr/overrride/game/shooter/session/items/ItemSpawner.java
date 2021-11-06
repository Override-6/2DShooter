package fr.overrride.game.shooter.session.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fr.overrride.game.shooter.api.session.GameSession;
import fr.overrride.game.shooter.api.session.character.GameSessionObject;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ItemSpawner implements GameSessionObject {

    private GameSession gameSession;

    private final Vector2 location;
    private final int minSpawnRate, maxSpawnRate, minDespawnRate, maxDespawnRate, afterSpawnSleep;
    private float currentSpawnRate;

    public ItemSpawner(float x, float y, int minSpawnRate, int maxSpawnRate, int minDespawnRate, int maxDespawnRate, int afterSpawnSleep) {
        this.location = new Vector2(x, y);
        this.minSpawnRate = minSpawnRate;
        this.maxSpawnRate = maxSpawnRate;
        this.minDespawnRate = minDespawnRate;
        this.maxDespawnRate = maxDespawnRate;
        this.afterSpawnSleep = afterSpawnSleep;
        defineNewSpawnRate();
    }

    @Override
    public void update(float deltaTime) {
        if (currentSpawnRate < 0) {
            defineNewSpawnRate();
            spawnItem();
            return;
        }
        currentSpawnRate -= deltaTime * 60 * 60;
    }

    private void spawnItem() {
        ItemType[] types = ItemType.values();
        ThreadLocalRandom threadRandom = ThreadLocalRandom.current();
        ItemType type = types[threadRandom.nextInt(types.length)];
        int life = threadRandom.nextInt(minDespawnRate, maxDespawnRate);
        currentSpawnRate += life + afterSpawnSleep;
        Item item = new Item(location.x, location.y, life, type);
        item.setGameSession(gameSession);
    }

    private void defineNewSpawnRate() {
        currentSpawnRate = ThreadLocalRandom.current().nextInt(minSpawnRate, maxSpawnRate);
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public Optional<GameSession> getCurrentGameSession() {
        return Optional.ofNullable(gameSession);
    }

    @Override
    public void setGameSession(@Nullable GameSession gameSession) {
        this.gameSession = gameSession;
    }

    @Override
    public Vector2 getLocation() {
        return location;
    }

    @Override
    public DrawPriority getDrawPriority() {
        return DrawPriority.BACKGROUND;
    }

}
