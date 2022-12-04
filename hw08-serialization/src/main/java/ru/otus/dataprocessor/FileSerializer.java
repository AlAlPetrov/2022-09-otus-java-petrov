package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;

    private final Gson gson = new Gson();

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try (var file = new FileWriter(fileName)){
            var serializedData = gson.toJson(data);
            file.write(serializedData);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
