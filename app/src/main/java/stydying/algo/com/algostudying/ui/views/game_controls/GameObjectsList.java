package stydying.algo.com.algostudying.ui.views.game_controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
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
public class GameObjectsList extends ListView {

    private GameObjectSelectListener listener;

    public GameObjectsList(Context context) {
        super(context);
        init(context);
    }

    public GameObjectsList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameObjectsList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameObjectsList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setAdapter(new ObjectsAdapter(context));
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    ObjectsAdapter adapter = (ObjectsAdapter) getAdapter();
                    listener.setObjectToSelectedPosition(createNewObject(adapter.getItem(position)));
                }
            }
        });
    }

    @Nullable
    private GameObject createNewObject(@Nullable Class<? extends GameObject> objectClass) {
        try {
            if (objectClass != null) {
                return objectClass.newInstance();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void setPossibleObjects(Class<? extends GameObject>[] gameObjectsClasses) {
        ((ObjectsAdapter) getAdapter()).setGameObjects(gameObjectsClasses);
    }

    public GameObjectsList setControlListener(GameObjectSelectListener listener) {
        this.listener = listener;
        return this;
    }

    private static final class ObjectsAdapter extends BaseAdapter {
        private Context context;
        private Class<? extends GameObject>[] gameObjects = null;

        public ObjectsAdapter(Context context) {
            this.context = context;
        }

        public ObjectsAdapter setGameObjects(Class<? extends GameObject>[] gameObjects) {
            this.gameObjects = gameObjects;
            notifyDataSetChanged();
            return this;
        }

        @Override
        public int getCount() {
            return gameObjects == null ? 0 : gameObjects.length;
        }

        @Nullable
        @Override
        public Class<? extends GameObject> getItem(int position) {
            return gameObjects == null ? null : gameObjects[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) convertView;
            if (view == null) {
                view = new TextView(context);
                view.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
            Class gameObject = getItem(position);
            String title = gameObject != null ? gameObject.toString() : "";
            view.setText(title);
            return view;
        }
    }
}
