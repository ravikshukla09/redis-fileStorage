package com.acceldata.redis.service;


import com.acceldata.redis.constants.KeyValueConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeyValueService {
    private Map<String, String> keyValueStore = new ConcurrentHashMap<>();

    PrintWriter printWriter;

    public KeyValueService(@Value("${fileName}") String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName, true);
        printWriter = new PrintWriter(fileWriter);
    }

    public String getValueForKey(String key) {
        return keyValueStore.get(key);
    }

    public String createKeyValue(String key, String value) {
        keyValueStore.put(key, value);
        // Append to FS
        printWriter.println(this.getKeyValuePairForFile(key, value, false));  //New line
        printWriter.flush();
        return "CREATED";
    }

    public String updateKeyValue(String key, String value) {
        keyValueStore.put(key, value);
        // Append to FS
        printWriter.println(this.getKeyValuePairForFile(key, value, false));  //New line
        printWriter.flush();
        return "UPDATED";
    }

    public boolean deleteKey(String key) {
        keyValueStore.remove(key);
        // Append to FS
        printWriter.println(this.getKeyValuePairForFile(key, null, true));  //New line
        printWriter.flush();
        return true;
    }

    public void setKeyValueStore(Map<String, String> keyValueStore) {
        this.keyValueStore = keyValueStore;
    }

    private String getKeyValuePairForFile(String key, String value, boolean isDelete) {
        if (isDelete) {
            return key + "=" + KeyValueConstants.DELETED;
        } else {
            return key + "=\"" + value + "\"";
        }
    }

    public String[] getKeyValueFromRecord(String record) {
        String[] keyValue = record.split("=");
        String key = keyValue[0];
        String value = keyValue[1];
        if (value.charAt(0) == '"') {
            value = value.substring(1, value.length() - 1);
        }
        return new String[]{key, value};
    }
}
