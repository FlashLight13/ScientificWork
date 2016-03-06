package stydying.algo.com.algostudying.ui.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import stydying.algo.com.algostudying.game.GameWorld;

/**
 * Created by Anton on 06.06.2015.
 */
public class GameView extends GLSurfaceView {

    private GameRenderer gameRenderer;

    public GameView(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        gameRenderer = new GameRenderer(getContext());
        setRenderer(gameRenderer);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent e) {
        requestRender();
        if (gameRenderer != null) {
            gameRenderer.onTouch(e);
        }
        return true;
    }

    public void init(GameWorld world) {
        gameRenderer.setWorld(world);
        requestRender();
    }
}