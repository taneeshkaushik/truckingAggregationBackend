package com.trucking.starter.utilities;
import java.util.*;
import io.vertx.core.json.*;
import io.vertx.sqlclient.*;
import java.time.*;

public class Utils {

    public List<JsonObject> RowSet_To_List(RowSet<Row> rows){
        List<JsonObject> list = new ArrayList<>();
        for (Row row : rows) {
            int size = row.size();
            JsonObject jsonObject = new JsonObject();
            for (int i = 0; i < size; i++) {
                String columnName = row.getColumnName(i);
                Object value = row.getValue(i);
                if(value != null){
                    if(value instanceof LocalDateTime){
                        jsonObject.put(columnName, ((LocalDateTime)value).atZone(
                                ZoneId.systemDefault()).toInstant());
                    } else {
                        jsonObject.put(columnName, row.getValue(i));
                    }
                }
            }
            list.add(jsonObject);
        }
        return list;
    }
}
