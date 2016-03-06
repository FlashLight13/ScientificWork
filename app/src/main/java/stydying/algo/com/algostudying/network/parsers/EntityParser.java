package stydying.algo.com.algostudying.network.parsers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * Created by Anton on 04.02.2016.
 */
public class EntityParser<T> implements Parser<T> {

    private final TypeToken<T> typeToken;

    public EntityParser(Class<T> entityClass) {
        this.typeToken = TypeToken.get(entityClass);
    }

    public EntityParser(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    @Override
    public T parse(ResponseBody responseBody) throws IOException {
        return new Gson().fromJson(responseBody.string(), typeToken.getType());
    }
}
