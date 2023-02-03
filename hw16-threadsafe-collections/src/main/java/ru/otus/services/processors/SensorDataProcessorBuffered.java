package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

class MeasurementTimeComparator implements Comparator<SensorData> {
    public int compare(SensorData left, SensorData right)
    {
        return left.getMeasurementTime().compareTo(right.getMeasurementTime());
    }
}
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int defaultInitialCapacity = 11;
    private PriorityBlockingQueue<SensorData> dataBuffer = new PriorityBlockingQueue<>(defaultInitialCapacity,
            new MeasurementTimeComparator());
    private final int bufferSize;
    private final SensorDataBufferedWriter writer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        dataBuffer.put(data);
        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    public void flush() {
        try {
            var outputBuffer = new ArrayList<SensorData>();
            if (dataBuffer.drainTo(outputBuffer, bufferSize) > 0) {
                writer.writeBufferedData(outputBuffer);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
