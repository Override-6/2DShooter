package fr.overrride.game.shooter.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.linkit.api.application.connection.ExternalConnection;
import fr.overrride.game.shooter.api.other.states.ScreenState;

public class LobbyState extends ScreenState {
    private SpriteBatch batch;
    private final ExternalConnection server;

    public LobbyState(ExternalConnection serverConnection) {
        super();
        this.server = serverConnection;
    }

    @Override
    protected void handleInputs() {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render(SpriteBatch batch) {

    }

    @Override
    public void dispose() {

    }
}
