package stydying.algo.com.algostudying.ui.views.game_controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.objects.GameObject;
import stydying.algo.com.algostudying.ui.interfaces.GameObjectSelectListener;

/**
 * Created by Anton on 09.02.2016.
 */
public class GameObjectsListView extends ListView {

    private GameObjectSelectListener listener;

    public GameObjectsListView(Context context) {
        super(context);
        init();
    }

    public GameObjectsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameObjectsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameObjectsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setAdapter(new ObjectsAdapter());
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    ObjectsAdapter adapter = (ObjectsAdapter) getAdapter();
                    listener.setObjectToSelectedPosition(adapter.getItem(position).cloneObject());
                }
            }
        });
    }

    public GameObjectsListView setPossibleObjects(GameObject... gameObjectsClasses) {
        ((ObjectsAdapter) getAdapter()).setGameObjects(gameObjectsClasses);
        return this;
    }

    public GameObjectsListView setControlListener(GameObjectSelectListener listener) {
        this.listener = listener;
        return this;
    }

    private static final class ObjectsAdapter extends BaseAdapter {
        private GameObject[] gameObjects;

        public ObjectsAdapter setGameObjects(GameObject... gameObjects) {
            this.gameObjects = gameObjects;
            notifyDataSetChanged();
            return this;
        }

        @Override
        public int getCount() {
            return gameObjects == null ? 0 : gameObjects.length;
        }

        @NonNull
        @Override
        public GameObject getItem(int position) {
            return gameObjects[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) convertView;
            if (view == null) {
                view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.v_game_objects_list_item, parent, false);
            }
            view.setText(getItem(position).getDescription());
            return view;
        }
    }
}
