package stydying.algo.com.algostudying.game.commands;

import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.game.objects.Player;

/**
 * Created by anton on 27.06.15.
 */
public abstract class Command implements Cloneable {

    private int titleId;
    private int iconId;
    private int descriptionId;

    public Command(int titleId, int iconId, int descriptionId) {
        this.titleId = titleId;
        this.iconId = iconId;
        this.descriptionId = descriptionId;
    }

    public Command(Command command) {
        this.titleId = command.titleId;
        this.iconId = command.iconId;
        this.descriptionId = command.descriptionId;
    }

    public abstract boolean perform(GameWorld gameWorld);

    public int getTitleId() {
        return titleId;
    }

    public int getIconId() {
        return iconId;
    }

    public int getDescriptionId() {
        return descriptionId;
    }

    public abstract Command cloneCommand();
}
