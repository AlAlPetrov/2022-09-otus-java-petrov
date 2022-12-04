package ru.otus.dataprocessor;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class ResourcesFileLoader implements Loader {
    private final String fileName;
    private final Gson gson = new Gson();
    Type listType = new TypeToken<List<Measurement>>(){}.getType();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        var url = Resources.getResource(fileName);
        try {
            var measurements = Resources.toString(url, StandardCharsets.UTF_8);
            return gson.fromJson(measurements, listType);
        } catch (IOException ex) {
            throw new FileProcessException(ex);
        }
    }
}
