package stydying.algo.com.algostudying.game;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.game.commands.Command;
import stydying.algo.com.algostudying.game.objects.CubeBlock;
import stydying.algo.com.algostudying.game.objects.EmptyObject;
import stydying.algo.com.algostudying.game.objects.GameObject;
import stydying.algo.com.algostudying.game.objects.ObjectSerializator;
import stydying.algo.com.algostudying.game.objects.Player;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;
import stydying.algo.com.algostudying.ui.interfaces.ControlListener;
import stydying.algo.com.algostudying.ui.interfaces.GameObjectSelectListener;
import stydying.algo.com.algostudying.ui.interfaces.HeightControlListener;
import stydying.algo.com.algostudying.utils.vectors.Vector2i;
import stydying.algo.com.algostudying.utils.vectors.Vector3i;

/**
 * Created by anton on 27.06.15.
 */
public class GameWorld {

    public static final int GAME_CELL_MULTIPLIER = 2;

    private int worldX;
    private int worldY;
    private int worldZ;

    private transient Player player;
    private transient GameObject[][][] map;

    private transient GameWorldEditor gameWorldEditor;

    public GameWorld(Context context, Task task, GameFieldActivity.Mode mode) {
        task.initMap(context);
        worldX = task.getGameField().length;
        worldY = task.getGameField()[0].length;
        worldZ = task.getGameField()[0][0].length;
        map = new GameObject[worldX][worldY][worldZ];
        for (int x = 0; x < worldX; x++) {
            for (int y = 0; y < worldY; y++) {
                for (int z = 0; z < worldZ; z++) {
                    map[x][y][z] = ObjectSerializator.gameObjectFromJsonString(task.getGameField()[x][y][z])
                            .setCoordinates((x - worldX / 2) * GAME_CELL_MULTIPLIER,
                                    (y - worldY / 2) * GAME_CELL_MULTIPLIER,
                                    z * GAME_CELL_MULTIPLIER);
                    if (map[x][y][z] instanceof Player) {
                        player = (Player) map[x][y][z];
                    }
                }
            }
        }
        if (mode != GameFieldActivity.Mode.EDIT && player == null) {
            throw new IllegalStateException("No player");
        }

        gameWorldEditor = new GameWorldEditor(map, worldX, worldY, worldZ);
    }

    public void executeCommands(List<Command> commands) {
        for (Command command : commands) {
            command.perform(this, player);
        }
    }

    public void initDrawing(Context context) throws IOException {
        Log.d("DebugLogs", "Start init drawing for world " + this.toString());
        for (GameObject[][] current : map) {
            for (GameObject[] currentLine : current) {
                for (GameObject currentCell : currentLine) {
                    currentCell.initDrawing(context);
                }
            }
        }
        Log.d("DebugLogs", "Inited drawing for world " + this.toString());
    }

    public Iterator<GameObject> getObjectsIterator() {
        return new Iterator<GameObject>() {
            private int indexX = 0;
            private int indexY = 0;
            private int indexZ = 0;


            @Override
            public boolean hasNext() {
                return indexY < worldY - 1 || indexX < worldX - 1 || indexZ < worldZ - 1;
            }

            @Nullable
            @Override
            public GameObject next() {
                GameObject object = map[indexX][indexY][indexZ];
                if (indexX < worldX - 1) {
                    indexX++;
                } else {
                    indexX = 0;
                    if (indexY < worldY - 1) {
                        indexY++;
                    } else {
                        indexY = 0;
                        if (indexZ < worldZ - 1) {
                            indexZ++;
                        } else {
                            throw new IndexOutOfBoundsException("Index out of bounds: indexX=" + indexX
                                    + "indexY=" + indexY
                                    + "indexZ=" + indexZ);
                        }
                    }
                }
                if (object instanceof CubeBlock) {
                    return (object).isSelected() ? null : object;
                } else {
                    return object;
                }
            }

            @Override
            public void remove() {
                throw new RuntimeException("Removing is not allowed there");
            }
        };
    }

    public GameWorldEditor getGameWorldEditor() {
        return gameWorldEditor;
    }

    public static final class GameWorldEditor implements
            ControlListener, HeightControlListener, GameObjectSelectListener {

        private GameObject[][][] map;
        private Vector2i selectedPosition = new Vector2i(0, 0);

        private int worldX;
        private int worldY;
        private int worldZ;

        public GameWorldEditor(GameObject[][][] map, int worldX, int worldY, int worldZ) {
            this.map = map;
            this.worldX = worldX;
            this.worldY = worldY;
            this.worldZ = worldZ;
        }

        public void setSelected(int x, int y, boolean isSelected) {
            if (x >= 0 && x < worldX && y >= 0 && y < worldY) {
                if (isSelected) {
                    selectedPosition.x = x;
                    selectedPosition.y = y;
                }
                (map[x][y][Math.max(0, topPosition(x, y))]).setSelected(isSelected);
            }
        }

        /**
         * @return index of the top cube of the (x,y) position, or -1 of there is a hole
         */
        private int topPosition(int x, int y) {
            int i = 0;
            while (i < worldZ && map[x][y][i] instanceof CubeBlock) {
                map[x][y][i].setSelected(false);
                i++;
            }
            return Math.max(-1, i - 1);
        }

        public void moveSelectionLeft() {
            setSelected(selectedPosition.x, selectedPosition.y, false);
            setSelected(selectedPosition.x - 1, selectedPosition.y, true);
        }

        public void moveSelectionTop() {
            setSelected(selectedPosition.x, selectedPosition.y, false);
            setSelected(selectedPosition.x, selectedPosition.y + 1, true);
        }

        public void moveSelectionRight() {
            setSelected(selectedPosition.x, selectedPosition.y, false);
            setSelected(selectedPosition.x + 1, selectedPosition.y, true);
        }

        public void moveSelectionBottom() {
            setSelected(selectedPosition.x, selectedPosition.y, false);
            setSelected(selectedPosition.x, selectedPosition.y - 1, true);
        }

        public void increaseHeightOfTheSelectedPosition() {
            int topPosition = Math.min(worldZ - 1, topPosition(selectedPosition.x, selectedPosition.y) + 1);
            Vector3i coordinates = map[selectedPosition.x][selectedPosition.y][topPosition].getCoordinates();
            CubeBlock top = new CubeBlock(coordinates.x, coordinates.y, topPosition * GAME_CELL_MULTIPLIER);
            top.setSelected(true);
            map[selectedPosition.x][selectedPosition.y][topPosition] = top;
        }

        public void decreaseHeightOfTheSelectedPosition() {
            int topPosition = Math.max(1, topPosition(selectedPosition.x, selectedPosition.y));
            Vector3i coordinates = map[selectedPosition.x][selectedPosition.y][topPosition].getCoordinates();
            EmptyObject top = new EmptyObject(coordinates.x, coordinates.y, topPosition * GAME_CELL_MULTIPLIER);
            top.setSelected(true);
            map[selectedPosition.x][selectedPosition.y][topPosition] = top;
        }

        public void setObjectToSelectedPosition(@Nullable GameObject object) {
            if (object != null) {
                int top = topPosition(selectedPosition.x, selectedPosition.y) + 1;
                Vector3i coordinates = map[selectedPosition.x][selectedPosition.y][top].getCoordinates();
                object.setCoordinates(coordinates.x, coordinates.y, top * GAME_CELL_MULTIPLIER);
                map[selectedPosition.x][selectedPosition.y][top + 1] = object;
            }
        }
    }

    public String[][][] createGameWorld() {
        String[][][] result = new String[worldX][worldY][worldZ];
        for (int i = 0; i < worldX; i++) {
            for (int j = 0; j < worldY; j++) {
                for (int k = 0; k < worldZ; k++) {
                    result[i][j][k] = ObjectSerializator.toJsonString(map[i][j][k]);
                }
            }
        }
        return result;
    }
}