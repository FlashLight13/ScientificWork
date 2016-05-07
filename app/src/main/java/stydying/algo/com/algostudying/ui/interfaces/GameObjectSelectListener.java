package stydying.algo.com.algostudying.ui.interfaces;

import android.support.annotation.NonNull;

import stydying.algo.com.algostudying.game.objects.GameObject;

/**
 * Created by Anton on 09.02.2016.
 */
public interface GameObjectSelectListener {

    void setObjectToSelectedPosition(@NonNull GameObject object);
}
