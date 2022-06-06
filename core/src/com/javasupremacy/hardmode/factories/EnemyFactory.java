package com.javasupremacy.hardmode.factories;

import com.javasupremacy.hardmode.objects.Enemy;
import org.json.simple.JSONObject;

import java.util.List;

public interface EnemyFactory {

    public Enemy produce(JSONObject object);
}
