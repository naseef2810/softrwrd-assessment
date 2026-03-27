package com.softwrdassessment.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonReader {

    public static List<Map<String, String>> readJsonData(String filePath) {
        try (Reader reader = new InputStreamReader(
                JsonReader.class.getClassLoader().getResourceAsStream(filePath))) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    public static Object[][] getTestData(String filePath) {
        List<Map<String, String>> dataList = readJsonData(filePath);
        Object[][] testData = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            testData[i][0] = dataList.get(i);
        }
        return testData;
    }
    
    public static Map<String, String> getUserByType(String userType) {
        List<Map<String, String>> users = readJsonData("testdata/users.json");
        return users.stream()
                .filter(u -> u.get("userType").equals(userType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User type not found: " + userType));
    }
}
