package processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.BenchmarkResult;

public class ReportGenerator {

    public static void main(String[] args) {

        var rawDataset = ResultReader.getBenchmarkResultMap();
        var groupedDataset = flattenNestedMap(rawDataset);

        DataVisualisation.displayExecutionTimeArrays(groupedDataset);
        DataVisualisation.displayMinAvgMaxExecutionTime(groupedDataset);
        DataVisualisation.displayAverageMemoryUsed(groupedDataset);
        DataVisualisation.displayAverageTotalCompilationTime(groupedDataset);
    }


    private static Map<String, Map<String, List<BenchmarkResult>>> flattenNestedMap(
        Map<String, Map<String, Map<String, BenchmarkResult>>> originalMap) {
        Map<String, Map<String, List<BenchmarkResult>>> flattenedMap = new HashMap<>();

        for (Map.Entry<String, Map<String, Map<String, BenchmarkResult>>> outerEntry : originalMap.entrySet()) {
            String outerKey = outerEntry.getKey();
            Map<String, Map<String, BenchmarkResult>> middleMap = outerEntry.getValue();

            for (Map.Entry<String, Map<String, BenchmarkResult>> middleEntry : middleMap.entrySet()) {
                String middleKey = middleEntry.getKey();

                for (Map.Entry<String, BenchmarkResult> innerEntry : middleEntry.getValue().entrySet()) {
                    String innerKey = innerEntry.getKey();
                    BenchmarkResult benchmarkResult = innerEntry.getValue();

                    flattenedMap.computeIfAbsent(outerKey, k -> new HashMap<>())
                        .computeIfAbsent(innerKey, k -> new ArrayList<>())
                        .add(benchmarkResult);
                }
            }
        }

        return flattenedMap;
    }


}
