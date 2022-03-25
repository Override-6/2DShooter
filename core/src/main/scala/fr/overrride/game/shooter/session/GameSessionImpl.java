package fr.overrride.game.shooter.session;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.overrride.game.shooter.api.session.GameSession;
import fr.overrride.game.shooter.api.session.GameSessionObject;
import fr.overrride.game.shooter.api.session.ParticleManager;
import fr.overrride.game.shooter.api.session.character.Character;
import fr.overrride.game.shooter.api.session.character.Controllable;
import fr.overrride.game.shooter.api.session.character.Controller;
import fr.overrride.game.shooter.api.session.levels.Level;

import java.util.HashSet;
import java.util.Set;

public class GameSessionImpl implements GameSession {

    private final Set<Controllable<?>> players = new HashSet<>();

    private final SceneManager sceneManager = new SceneManager();
    private final ParticleManager particleManager;
    private final int maxPlayers;
    private Level level;

    public GameSessionImpl(int maxPlayers, Level level, ParticleManager particleManager) {
        this.maxPlayers = maxPlayers;
        this.particleManager = particleManager;
        if (level != null)
            setCurrentLevel(level);
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public int countPlayers() {
        return players.size();
    }

    @Override
    public void addCharacter(Character character) {
        if (players.size() > maxPlayers)
            throw new IllegalArgumentException("Player limit reached !");

        character.setGameSession(this);
        players.add(character);
        addObject(character);
    }

    @Override
    public void setCurrentLevel(Level level) {
        if (this.level != null)
            removeObject(this.level);
        level.setGameSession(this);
        addObject(level);
        this.level = level;
    }

    @Override
    public void updateInputs() {
        players.forEach(character -> {
            Controller<?> ctrl = character.getController();
            if (ctrl != null)
                ctrl.update();
        });
    }

    @Override
    public void addObject(GameSessionObject object) {
        sceneManager.addObject(object);
    }

    @Override
    public void removeObject(GameSessionObject object) {
        if (object instanceof Controllable) {
            players.remove(object);
        }
        sceneManager.removeObject(object);
    }

    @Override
    public ParticleManager getParticleManager() {
        return particleManager;
    }

    @Override
    public void updateScene(float deltaTime) {
        sceneManager.update(deltaTime);
        particleManager.update(deltaTime);
    }

    @Override
    public void renderScene(SpriteBatch batch) {
        sceneManager.render(batch);
        particleManager.render(batch);
    }

    @Override
    public void disposeScene() {
        sceneManager.dispose();
    }

}