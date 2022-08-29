package com.Emile2250.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class JSONEditor {

    public static JSONObject getJSONFile(String path) {
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(path));
            return obj;
        } catch (Error | IOException | ParseException e) {
            return new JSONObject();
        }
    }

    public static void writeToJSONFile(String path, JSONObject jsonObject) {

        try {

            File data = new File(path.substring(0, path.lastIndexOf('/')));
            if (!(data.exists())) {
                data.mkdirs();
            }

            FileWriter writer = new FileWriter(path, false);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write(gson.toJson(jsonObject));
            writer.close();
        } catch (Error | IOException e) {
            System.out.println("Error when trying to save a JSON object to " + path + " with the object of " + jsonObject.toJSONString());
        }

    }

}
