package fr.override.game.shooter.session;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.override.game.shooter.actions.Action;
import fr.override.game.shooter.character.Character;
import fr.override.game.shooter.character.Controllable;
import fr.override.game.shooter.session.levels.Level;

import java.util.HashSet;
import java.util.Set;

public class GameSession {

    private final Set<Controllable> players = new HashSet<>();

    private final SceneManager sceneManager = new SceneManager();
    private final ParticleManager particleManager = new ParticleManager();
    private final int maxPlayers;
    private Level level;

    public GameSession(int maxPlayers, Level level) {
        this.maxPlayers = maxPlayers;
        level.setGameSession(this);
        addObject(level);
        this.level = level;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setCurrentLevel(Level level) {
        removeObject(this.level);
        level.setGameSession(this);
        addObject(level);
        this.level = level;
    }

    public void addPlayer(Character character) {
        if (players.size() > maxPlayers)
            throw new IllegalArgumentException("player limit reached !");

        character.setGameSession(this);
        players.add(character);
        addObject(character);
    }

    public void handleInputs() {
        players.forEach(character -> character.getController().handleInputs());
    }

    public void addObject(GameSessionObject object) {
        sceneManager.addObject(object);
    }

    public ParticleManager getParticleManager() {
        return particleManager;
    }

    public Action removeObject(GameSessionObject object) {
        if (object instanceof Controllable) {
            players.remove(object);
        }
        return sceneManager.removeObject(object);
    }

    public void update(float dt) {
        sceneManager.update(dt);
        particleManager.update(dt);
    }

    public void renderScene(SpriteBatch batch) {
        sceneManager.render(batch);
        particleManager.render(batch);
    }

    public void disposeScene() {
        sceneManager.dispose();
    }

}