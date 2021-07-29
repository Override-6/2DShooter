package fr.overrride.game.shooter.api.session;

import fr.overrride.game.shooter.api.session.character.Character;
import fr.overrride.game.shooter.api.session.character.GameSessionObject;
import fr.overrride.game.shooter.api.session.levels.Level;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameSession {

    int getMaxPlayers();

    int countPlayers();

    void addCharacter(Character character);

    void setCurrentLevel(Level level);

    void updateInputs();

    void addObject(GameSessionObject object);

    void removeObject(GameSessionObject object);

    ParticleManager getParticleManager();

    void updateScene(float deltaTime);

    void renderScene(SpriteBatch batch);

    void disposeScene();

}
