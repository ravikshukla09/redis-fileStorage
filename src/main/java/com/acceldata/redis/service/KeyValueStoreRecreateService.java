package com.acceldata.redis.service;

import com.acceldata.redis.constants.KeyValueConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class KeyValueStoreRecreateService implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private KeyValueService keyValueService;

    @Value("${fileName}")
    private String fileName;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            this.recreateKeyValueStore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recreateKeyValueStore() throws IOException {
        BufferedReader dataStoreReader = this.getBufferedReader();
        Map<String, String> map = new HashMap<>();
        String keyValue;
        while ((keyValue = dataStoreReader.readLine()) != null) {
            String[] pair = keyValueService.getKeyValueFromRecord(keyValue);
            String key = pair[0];
            String value = pair[1];

            if (value.equals(KeyValueConstants.DELETED)) {
                map.remove(key);
            } else {
                map.put(key, value);
            }
        }
        keyValueService.setKeyValueStore(map);
    }

    private BufferedReader getBufferedReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(fileName));
    }
}
