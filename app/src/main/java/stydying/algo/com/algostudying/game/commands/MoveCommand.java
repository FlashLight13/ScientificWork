package stydying.algo.com.algostudying.game.commands;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.game.objects.EmptyObject;
import stydying.algo.com.algostudying.game.objects.Player;
import stydying.algo.com.algostudying.game.objects.Sphere;
import stydying.algo.com.algostudying.utils.vectors.Vector3i;

/**
 * Created by anton on 27.06.15.
 */
public class MoveCommand extends Command {

    public MoveCommand() {
        super(R.string.move_command, R.drawable.ic_arrow_upward_black_24dp, R.string.move_command_desc);
    }

    public MoveCommand(Command command) {
        super(command);
    }

    @Override
    public boolean perform(GameWorld gameWorld) throws InterruptedException {
        final Player player = gameWorld.getPlayer();
        Vector3i coordinates = player.getWorldCoordinates();
        int newX = coordinates.x;
        int newY = coordinates.y;
        switch (player.getLookDirection()) {
            case BACK:
                newY = newY - 1;
                break;
            case LEFT:
                newX = newX - 1;
                break;
            case RIGHT:
                newX = newX + 1;
                break;
            case STRAIT:
                newY = newY + 1;
                break;
            default:
                throw new IllegalStateException();
        }
        if (gameWorld.isInWorldBounds(newX, newY)
                && (gameWorld.get(newX, newY, coordinates.z) instanceof EmptyObject
                || gameWorld.get(newX, newY, coordinates.z) instanceof Sphere)) {
            player.setWorldCoordinates(newX, newY);
            gameWorld.tryConsumeSphere(newX, newY, coordinates.z);
            return true;
        }
        return false;
    }

    @Override
    public Command cloneCommand() {
        return new MoveCommand(this);
    }
}
