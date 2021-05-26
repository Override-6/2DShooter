package fr.overrride.game.shooter.session;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.overrride.game.shooter.GameConstants;
import fr.overrride.game.shooter.api.other.states.ScreenState;
import fr.overrride.game.shooter.api.session.GameSession;
import fr.overrride.game.shooter.api.session.character.Character;
import fr.overrride.game.shooter.api.session.character.KeyControl;
import fr.overrride.game.shooter.api.session.character.KeyType;
import fr.overrride.game.shooter.session.character.CharacterController;
import fr.overrride.game.shooter.session.character.ShooterCharacter;
import fr.overrride.game.shooter.session.levels.FirstLevel;

import static com.badlogic.gdx.Input.Keys.*;

public class PlayState extends ScreenState {

    private final GameSession session;
    private final Texture background;

    protected PlayState() {
        super();

        session = new GameSessionImpl(2, new FirstLevel());
        background = new Texture("background.png");
        createPlayers();

        camera.setToOrtho(false, GameConstants.VIEWPORT_WIDTH, GameConstants.VIEWPORT_HEIGHT);

    }

    private void createPlayers() {
        Character player1 = new ShooterCharacter(500, 550, Color.GREEN);
        CharacterController controller = new CharacterController(player1);

        controller.addKeyControl(KeyControl.of(KeyType.DASH, A, Character::dash));
        controller.addKeyControl(KeyControl.of(KeyType.JUMP, SPACE, Character::jump));
        controller.addKeyControl(KeyControl.of(KeyType.LEFT, Q, Character::left));
        controller.addKeyControl(KeyControl.of(KeyType.RIGHT, D, Character::right));
        controller.addKeyControl(KeyControl.of(KeyType.SHOOT, E, Character::shoot));
        player1.setController(controller);
        player1.setGameSession(session);

        Character player2 = new ShooterCharacter(1500, 550, Color.PURPLE);
        CharacterController controller2 = new CharacterController(player2);

        controller2.addKeyControl(KeyControl.of(KeyType.DASH, P, Character::dash));
        controller2.addKeyControl(KeyControl.of(KeyType.JUMP, UP, Character::jump));
        controller2.addKeyControl(KeyControl.of(KeyType.LEFT, LEFT, Character::left));
        controller2.addKeyControl(KeyControl.of(KeyType.RIGHT, RIGHT, Character::right));
        controller2.addKeyControl(KeyControl.of(KeyType.SHOOT, R, Character::shoot));
        player2.setController(controller2);
        player2.setGameSession(session);

        session.addCharacter(player1);
        session.addCharacter(player2);
    }

    @Override
    protected void handleInputs() {
        session.updateInputs();
    }

    @Override
    public void update(float deltaTime) {
        handleInputs();
        session.updateScene(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0);
        session.renderScene(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        session.disposeScene();
        background.dispose();
    }

}
