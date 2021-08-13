package com.acceldata.redis.controllers;

import com.acceldata.redis.service.KeyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyValueController {
    @Autowired
    KeyValueService keyValueService;

    @PostMapping("/create")
    public ResponseEntity<?> createKeyValuePair(@RequestParam String key, @RequestParam String value) {
        keyValueService.createKeyValue(key, value);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/read")
    public ResponseEntity<String> getValue(@RequestParam String key) {
        String value = keyValueService.getValueForKey(key);
        if (value == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateKeyValuePair(@RequestParam String key, @RequestParam String value) {
        keyValueService.updateKeyValue(key, value);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteKeyValuePair(@RequestParam String key) {
        keyValueService.deleteKey(key);
        return ResponseEntity.noContent().build();
    }
}
