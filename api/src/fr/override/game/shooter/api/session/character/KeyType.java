package fr.override.game.shooter.api.session.character;

public enum KeyType {

    RIGHT(true),
    LEFT(true),
    JUMP(false),
    DASH(false),
    SHOOT(true);

    private final boolean canBeHold;

    <T extends Controller> KeyType(boolean canBeHold) {
        this.canBeHold = canBeHold;
    }

    public boolean canBeHold() {
        return canBeHold;
    }

}
