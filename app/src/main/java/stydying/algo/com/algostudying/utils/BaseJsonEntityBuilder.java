package stydying.algo.com.algostudying.utils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anton on 16.02.2016.
 */
public class BaseJsonEntityBuilder {

    private Map<String, Object> fields = new HashMap<>();

    public BaseJsonEntityBuilder add(String field, String value) {
        fields.put(field, value);
        return this;
    }

    public BaseJsonEntityBuilder add(String field, Object value) {
        fields.put(field, value);
        return this;
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder("{");
        for (String field : fields.keySet()) {
            stringBuilder
                    .append("\"").append(field).append("\"")
                    .append(":")
                    .append(new Gson().toJson(fields.get(field)))
                    .append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}