package stydying.algo.com.algostudying.game.commands;

import stydying.algo.com.algostudying.game.GameWorld;

/**
 * Created by anton on 27.06.15.
 */
public class CycleCommandBlock extends CommandBlock {

    private int count;

    public CycleCommandBlock() {
        super();
    }

    public CycleCommandBlock(int count) {
        this.count = count;
    }

    public CycleCommandBlock(Command command) {
        super(command);
        if (command instanceof CycleCommandBlock) {
            this.count = ((CycleCommandBlock) command).count;
        }
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    @Override
    public boolean perform(GameWorld gameWorld) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            if (!super.perform(gameWorld)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Command cloneCommand() {
        return new CycleCommandBlock(this);
    }
}
