package com.javasupremacy.hardmode.utils;

import com.badlogic.gdx.Gdx;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JsonReader {
    private JSONObject obj;
    public JsonReader(int level) {
        try {
            switch (level) {
                case 1:
                    obj = (JSONObject) new JSONParser().parse(Gdx.files.internal("config-json/level1.json").readString());
                    break;
                case 2:
                    obj = (JSONObject) new JSONParser().parse(Gdx.files.internal("config-json/level2.json").readString());
                    break;
                case 3:
                    obj = (JSONObject) new JSONParser().parse(Gdx.files.internal("config-json/level3.json").readString());
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getPlayerAttribute() {
        return (JSONObject) obj.get("player");
    }

    public JSONArray getEnemies() {
        return (JSONArray) obj.get("enemy");
    }
}
