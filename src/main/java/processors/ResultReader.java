package processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import model.BenchmarkResult;
import model.Environment;
import model.MemoryResult;
import model.TimeResult;
import model.enums.BenchmarkTest;
import model.enums.Isolation;

@UtilityClass
public class ResultReader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }


    public Map<String, Map<String, Map<String, BenchmarkResult>>> getBenchmarkResultMap() {
        Map<String, Map<String, Map<String, BenchmarkResult>>> resultMap = new HashMap<>();
        for (Isolation isolation : Isolation.values()) {
            String isolationName = isolation.getName();
            resultMap.put(isolationName, getBenchmarkResultForIsolationAndTestNumber(isolationName));
        }
        return resultMap;
    }

    private Map<String, BenchmarkResult> getBenchmarkResultForIsolation(String isolationName, String testNumber) {
        Map<String, BenchmarkResult> benchmarkResultMap = new HashMap<>();

        for (BenchmarkTest benchmark : BenchmarkTest.values()) {
            String testName = benchmark.getName();
            benchmarkResultMap.put(
                testName,
                getBenchmarkResultForTestNameIsolationTestNumber(testName, isolationName, testNumber)
            );
        }

        return benchmarkResultMap;
    }

    private Map<String, Map<String, BenchmarkResult>> getBenchmarkResultForIsolationAndTestNumber(
        String isolationName) {
        Map<String, Map<String, BenchmarkResult>> resultMap = new HashMap<>();

        for (int i = 1; i < 4; i++) {
            String testNumber = String.valueOf(i);
            resultMap.put(testNumber, getBenchmarkResultForIsolation(isolationName, testNumber));
        }

        return resultMap;
    }

    private BenchmarkResult getBenchmarkResultForTestNameIsolationTestNumber(String test, String isolation,
                                                                             String testNumber) {
        String jsonPath = String.join("/", "results", isolation, testNumber);
        jsonPath += "/" + testNumber + "-" + test;
        InputStream memoryInputStream = ResultReader.class.getClassLoader()
            .getResourceAsStream(jsonPath + "-mem.json");
        InputStream timeInputStream = ResultReader.class.getClassLoader()
            .getResourceAsStream(jsonPath + "-time.json");
        MemoryResult memoryResult = getMemoryResult(test, memoryInputStream);
        TimeResult timeResult = getTimeResult(test, timeInputStream);
        return new BenchmarkResult(memoryResult, timeResult);
    }

    private MemoryResult getMemoryResult(String testName, InputStream inputStream) {
        try {
            Map<String, Object> memoryMap = objectMapper.readValue(inputStream, Map.class);
            Object testDataObject = ((Map<String, Object>) memoryMap.get("data")).get(testName);
            MemoryResult.BenchmarkData benchmarkData = objectMapper.readValue(
                objectMapper.writeValueAsString(testDataObject),
                MemoryResult.BenchmarkData.class);
            Environment environment = objectMapper.readValue(
                objectMapper.writeValueAsString(memoryMap.get("environment")),
                Environment.class);
            return new MemoryResult(benchmarkData, environment);
        } catch (IOException e) {
            return null;
        }
    }

    private TimeResult getTimeResult(String testName, InputStream inputStream) {
        try {
            Map<String, Object> timeMap = objectMapper.readValue(inputStream, Map.class);
            Object testDataObject = ((Map<String, Object>) timeMap.get("data")).get(testName);
            TimeResult.BenchmarkData benchmarkData = objectMapper.readValue(
                objectMapper.writeValueAsString(testDataObject),
                TimeResult.BenchmarkData.class);
            Environment environment = objectMapper.readValue(
                objectMapper.writeValueAsString(timeMap.get("environment")),
                Environment.class);
            return new TimeResult(benchmarkData, environment);
        } catch (IOException e) {
            return null;
        }
    }

}
