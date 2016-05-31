package stydying.algo.com.algostudying.game;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.errors.VerifyException;
import stydying.algo.com.algostudying.game.commands.Command;
import stydying.algo.com.algostudying.game.conditions.BaseWinCondition;
import stydying.algo.com.algostudying.game.objects.CubeBlock;
import stydying.algo.com.algostudying.game.objects.EmptyObject;
import stydying.algo.com.algostudying.game.objects.GameObject;
import stydying.algo.com.algostudying.game.objects.ObjectSerializator;
import stydying.algo.com.algostudying.game.objects.Player;
import stydying.algo.com.algostudying.game.objects.Sphere;
import stydying.algo.com.algostudying.logic.managers.ModelsManager;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;
import stydying.algo.com.algostudying.ui.interfaces.ControlListener;
import stydying.algo.com.algostudying.ui.interfaces.GameObjectSelectListener;
import stydying.algo.com.algostudying.ui.interfaces.HeightControlListener;
import stydying.algo.com.algostudying.utils.vectors.Vector2i;
import stydying.algo.com.algostudying.utils.vectors.Vector3f;
import stydying.algo.com.algostudying.utils.vectors.Vector3i;

/**
 * Created by anton on 27.06.15.
 */
public class GameWorld {

    public static final int GAME_CELL_MULTIPLIER = 2;

    private int worldX;
    private int worldY;
    private int worldZ;
    private int spheresCount = 0;

    private transient Player player;
    private transient Vector3f worldCenter;
    private transient GameObject[][][] map;
    private List<Sphere> collectedSpheres = new ArrayList<>();
    private Vector3f initialPlayerPosition;

    private transient GameWorldEditor gameWorldEditor;
    private transient CommandsExecutionThread commandsExecutionThread;

    public GameWorld(Context context, Task task, GameFieldActivity.Mode mode) throws IOException {
        ModelsManager.getInstance().init(context);
        task.initMap(context);
        worldX = task.getGameField().length;
        worldY = task.getGameField()[0].length;
        worldZ = task.getGameField()[0][0].length;
        map = new GameObject[worldX][worldY][worldZ];
        for (int x = 0; x < worldX; x++) {
            for (int y = 0; y < worldY; y++) {
                for (int z = 0; z < worldZ; z++) {
                    map[x][y][z] = ObjectSerializator.gameObjectFromJsonString(task.getGameField()[x][y][z])
                            .setCoordinates(x * GAME_CELL_MULTIPLIER - worldY / GAME_CELL_MULTIPLIER,
                                    y * GAME_CELL_MULTIPLIER - worldY / GAME_CELL_MULTIPLIER,
                                    z * GAME_CELL_MULTIPLIER);
                    if (map[x][y][z] instanceof Player) {
                        player = (Player) map[x][y][z];
                        initialPlayerPosition = new Vector3f(player.getCoordinates());
                    }
                    if (map[x][y][z] instanceof Sphere) {
                        spheresCount++;
                    }
                }
            }
        }
        if (mode != GameFieldActivity.Mode.EDIT && player == null) {
            throw new IllegalStateException("No player");
        }

        this.gameWorldEditor = new GameWorldEditor(map, worldZ);
        this.worldCenter = new Vector3f((worldX / 2) * GAME_CELL_MULTIPLIER,
                (worldY / 2) * GAME_CELL_MULTIPLIER, (worldZ / 2) * GAME_CELL_MULTIPLIER);
    }

    public void executeCommands(@NonNull Context context,
                                @NonNull List<Command> commands,
                                @Nullable CommandsExecutionThread.ExecutionListener executionListener) {
        if (commandsExecutionThread == null) {
            commandsExecutionThread = new CommandsExecutionThread(context, new BaseWinCondition(), this);
            commandsExecutionThread.setCommands(commands).setExecutionListener(executionListener).start();
        }
    }

    public boolean isExecuting() {
        return commandsExecutionThread != null;
    }

    public void stopExecuting() {
        if (commandsExecutionThread != null) {
            commandsExecutionThread.interrupt();
            commandsExecutionThread = null;
        }
    }

    public float getWorldTop() {
        return worldZ * GAME_CELL_MULTIPLIER;
    }

    public Vector3f getWorldCenter() {
        return this.worldCenter;
    }

    public Player getPlayer() {
        return this.player;
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
                return object;
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

    public int getSpheresCount() {
        return spheresCount;
    }

    public List<Sphere> getCollectedSpheres() {
        return collectedSpheres;
    }

    public final class GameWorldEditor implements
            ControlListener, HeightControlListener, GameObjectSelectListener {

        private GameObject[][][] map;
        private Vector2i selectedPosition = new Vector2i(0, 0);

        private final int worldZ;

        public GameWorldEditor(GameObject[][][] map, int worldZ) {
            this.map = map;
            this.worldZ = worldZ;
        }

        public void setSelected(int x, int y, boolean isSelected) {
            if (isInWorldBounds(x, y)) {
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
            if (isInWorldBounds(selectedPosition.x - 1, selectedPosition.y)) {
                setSelected(selectedPosition.x, selectedPosition.y, false);
            }
            setSelected(selectedPosition.x - 1, selectedPosition.y, true);
        }

        public void moveSelectionTop() {
            if (isInWorldBounds(selectedPosition.x, selectedPosition.y + 1)) {
                setSelected(selectedPosition.x, selectedPosition.y, false);
            }
            setSelected(selectedPosition.x, selectedPosition.y + 1, true);
        }

        public void moveSelectionRight() {
            if (isInWorldBounds(selectedPosition.x + 1, selectedPosition.y)) {
                setSelected(selectedPosition.x, selectedPosition.y, false);
            }
            setSelected(selectedPosition.x + 1, selectedPosition.y, true);
        }

        public void moveSelectionBottom() {
            if (isInWorldBounds(selectedPosition.x, selectedPosition.y - 1)) {
                setSelected(selectedPosition.x, selectedPosition.y, false);
            }
            setSelected(selectedPosition.x, selectedPosition.y - 1, true);
        }

        public void increaseHeightOfTheSelectedPosition() {
            int topPosition = Math.min(worldZ - 1, topPosition(selectedPosition.x, selectedPosition.y) + 1);
            Vector3f coordinates = map[selectedPosition.x][selectedPosition.y][topPosition].getCoordinates();
            CubeBlock top = new CubeBlock(coordinates.x, coordinates.y, topPosition * GAME_CELL_MULTIPLIER);
            top.setSelected(true);
            map[selectedPosition.x][selectedPosition.y][topPosition] = top;
        }

        public void decreaseHeightOfTheSelectedPosition() {
            int topPosition = Math.max(1, topPosition(selectedPosition.x, selectedPosition.y));
            Vector3f coordinates = map[selectedPosition.x][selectedPosition.y][topPosition].getCoordinates();
            EmptyObject top = new EmptyObject(coordinates.x, coordinates.y, topPosition * GAME_CELL_MULTIPLIER);
            map[selectedPosition.x][selectedPosition.y][topPosition] = top;
            map[selectedPosition.x][selectedPosition.y][Math.max(0, topPosition(selectedPosition.x, selectedPosition.y))]
                    .setSelected(true);
        }

        public void setObjectToSelectedPosition(@NonNull GameObject object) {
            if (object instanceof Player) {
                if (player != null) {
                    return;
                }
                player = (Player) object;
            }
            final int top = topPosition(selectedPosition.x, selectedPosition.y) + 1;
            if (top > 0 && top < worldZ) {
                Vector3f coordinates = map[selectedPosition.x][selectedPosition.y][top].getCoordinates();
                object.setCoordinates(coordinates.x, coordinates.y, top * GAME_CELL_MULTIPLIER);
                if (map[selectedPosition.x][selectedPosition.y][top] instanceof Player) {
                    player = null;
                }
                map[selectedPosition.x][selectedPosition.y][top] = object;
            }
        }
    }

    public void tryConsumeSphere(int x, int y, int z) {
        if (map[x][y][z] instanceof Sphere) {
            collectedSpheres.add((Sphere) map[x][y][z]);
            map[x][y][z] = new EmptyObject();
        }
    }

    public GameObject get(int x, int y, int z) {
        return map[x][y][z];
    }

    public void reset() {
        player.setCoordinates(initialPlayerPosition.x, initialPlayerPosition.y, initialPlayerPosition.z);
        for (Sphere sphere : collectedSpheres) {
            Vector3i worldCoordinates = sphere.getWorldCoordinates();
            map[worldCoordinates.x][worldCoordinates.y][worldCoordinates.z] = sphere;
        }
        collectedSpheres.clear();
    }

    public boolean isInWorldBounds(int x, int y) {
        return x >= worldX / GAME_CELL_MULTIPLIER && x < -worldX / GAME_CELL_MULTIPLIER && y >= worldY / GAME_CELL_MULTIPLIER && y < -worldY / GAME_CELL_MULTIPLIER;
    }

    public String[][][] createGameWorld() throws VerifyException {
        if (player == null) {
            throw new VerifyException(VerifyException.NO_PLAYER);
        }
        String[][][] result = new String[worldX][worldY][worldZ];
        for (int i = 0; i < worldX; i++) {
            for (int j = 0; j < worldY; j++) {
                for (int k = 0; k < worldZ; k++) {
                    if (map[i][j][k] instanceof Sphere) {
                        spheresCount++;
                    }
                    result[i][j][k] = ObjectSerializator.toJsonString(map[i][j][k]);
                }
            }
        }
        if (spheresCount == 0) {
            throw new VerifyException(VerifyException.NO_SPHERES);
        }
        return result;
    }
}