package fr.override.game.shooter.states;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.override.game.shooter.character.Character;
import fr.override.game.shooter.character.Controller;
import fr.override.game.shooter.character.ShooterCharacter;
import fr.override.game.shooter.session.GameSession;
import fr.override.game.shooter.session.levels.FirstLevel;

import java.util.Optional;

import static com.badlogic.gdx.Input.Keys.*;
import static fr.override.game.shooter.util.GameConstants.VIEWPORT_HEIGHT;
import static fr.override.game.shooter.util.GameConstants.VIEWPORT_WIDTH;

public class PlayState extends GameState {

    private final GameSession session;
    private final Texture background;

    protected PlayState(GameStateManager manager) {
        super(manager);

        session = new GameSession(2, new FirstLevel());
        background = new Texture("background.png");
        createPlayers();

        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

    }

    private void createPlayers() {
        Character player1 = new ShooterCharacter(500, 550, Color.GREEN);
        Controller controller = new Controller(player1);

        controller.setJumpKey(SPACE);
        controller.setLeftKey(Q);
        controller.setRightKey(D);
        controller.setDashKey(A);
        player1.setController(controller);
        player1.setGameSession(session);

        Character player2 = new ShooterCharacter(1500, 550, Color.PURPLE);
        Controller controller2 = new Controller(player2);

        controller2.setJumpKey(UP);
        controller2.setLeftKey(LEFT);
        controller2.setRightKey(RIGHT);
        controller2.setDashKey(P);
        player2.setController(controller2);
        player2.setGameSession(session);

        session.addPlayer(player1);
        session.addPlayer(player2);
    }

    @Override
    protected void handleInput() {
        session.handleInputs();
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
        session.update(deltaTime);
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
