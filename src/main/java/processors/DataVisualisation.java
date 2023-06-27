package processors;

import static model.enums.Isolation.DOCKER;
import static model.enums.Isolation.DOCKER_WITH_RESTRICTIONS;
import static model.enums.Isolation.HOST;
import static model.enums.Isolation.VM;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import model.BenchmarkResult;
import model.MemoryResult;
import model.TimeResult;
import model.TimeResult.BenchmarkData;
import model.enums.BenchmarkTest;
import model.enums.Isolation;

@UtilityClass
public class DataVisualisation {

    public void displayExecutionTimeArrays(Map<String, Map<String, List<BenchmarkResult>>> dataset) {
        for (BenchmarkTest test : BenchmarkTest.values()) {
            System.out.println("Tests for " + test);
            for (Isolation isolation : List.of(HOST, DOCKER, DOCKER_WITH_RESTRICTIONS, VM)) {
                var timeList = getResponseTimesForTestAndIsolation(test, isolation, dataset);
                var str = new StringBuilder();
                timeList.forEach(item -> str.append(item).append(" "));
                System.out.println(isolation.getName() + "(" + timeList.size() + "):\n" + str + "\n");
            }
            System.out.println("\n\n");
        }
    }

    public void displayAverageExecutionTimePercentage(Map<String, Map<String, List<BenchmarkResult>>> dataset) {
        for (BenchmarkTest test : BenchmarkTest.values()) {
            System.out.println("Tests for " + test);
            var hostAverage = MetricComputationProcessor.average(getResponseTimesForTestAndIsolation(test, HOST, dataset));
            for (Isolation isolation : List.of(HOST, DOCKER, DOCKER_WITH_RESTRICTIONS, VM)) {
                var isolationAverage = MetricComputationProcessor.average(getResponseTimesForTestAndIsolation(test, isolation, dataset));
                System.out.println(isolation.getName() + ": " + isolationAverage / hostAverage * 100);
            }
            System.out.println("\n\n");
        }
    }

    public void displayMinAvgMaxExecutionTime(Map<String, Map<String, List<BenchmarkResult>>> dataset) {
        for (BenchmarkTest test : BenchmarkTest.values()) {
            System.out.println("Tests for " + test);
            for (Isolation isolation : List.of(HOST, DOCKER, DOCKER_WITH_RESTRICTIONS, VM)) {
                var isolationMin = getResponseTimesForTestAndIsolation(test, isolation, dataset).stream().mapToLong(x1->x1).min().getAsLong();
                var isolationAverage = MetricComputationProcessor.average(getResponseTimesForTestAndIsolation(test, isolation, dataset));
                var isolationMax = getResponseTimesForTestAndIsolation(test, isolation, dataset).stream().mapToLong(x1->x1).max().getAsLong();
                System.out.println(isolation.getName() + ": " + isolationMin + " " + isolationAverage + " " + isolationMax);
            }
            System.out.println("\n\n");
        }
    }

    public void displayAverageMemoryUsed(Map<String, Map<String, List<BenchmarkResult>>> dataset) {
        for (BenchmarkTest test : BenchmarkTest.values()) {
            System.out.println("Tests for " + test);
            for (Isolation isolation : List.of(HOST, DOCKER, DOCKER_WITH_RESTRICTIONS, VM)) {
                var isolationAverage = getMemoryUsedForTestAndIsolation(test, isolation, dataset).stream().mapToLong(x1->x1).average().getAsDouble();
                System.out.println(isolation.getName() + ": " + isolationAverage);
            }
            System.out.println("\n\n");
        }
    }

    public void displayAverageTotalCompilationTime(Map<String, Map<String, List<BenchmarkResult>>> dataset) {
        for (BenchmarkTest test : BenchmarkTest.values()) {
            System.out.println("Tests for " + test);
            for (Isolation isolation : List.of(HOST, DOCKER, DOCKER_WITH_RESTRICTIONS, VM)) {
                var isolationAverage = getTotalCompilationTimeForTestAndIsolation(test, isolation, dataset).stream().mapToLong(x1->x1).average().getAsDouble();
                System.out.println(isolation.getName() + ": " + isolationAverage);
            }
            System.out.println("\n\n");
        }
    }

    private static List<Long> getResponseTimesForTestAndIsolation(BenchmarkTest test, Isolation isolation, Map<String, Map<String, List<BenchmarkResult>>> dataset) {
        var testList = dataset.get(isolation.getName())
            .get(test.getName());
        var timeList = testList.stream()
            .map(BenchmarkResult::getTimeResult)
            .map(TimeResult::getBenchmarkData)
            .map(BenchmarkData::getResults)
            .flatMap(Collection::stream)
            .skip(2)
            .map(TimeResult.Result::getUptime).toList();
        var memoryList = testList.stream()
            .map(BenchmarkResult::getMemoryResult)
            .map(MemoryResult::getBenchmarkData)
            .map(MemoryResult.BenchmarkData::getResults)
            .flatMap(Collection::stream)
            .skip(2)
            .map(MemoryResult.Result::getUptimeNs).toList();
        var result = new ArrayList<>(timeList);
        result.addAll(memoryList);
        return result.stream().sorted().toList();
    }

    private static List<Long> getMemoryUsedForTestAndIsolation(BenchmarkTest test, Isolation isolation, Map<String, Map<String, List<BenchmarkResult>>> dataset) {
        var testList = dataset.get(isolation.getName())
            .get(test.getName());
        return testList.stream()
            .map(BenchmarkResult::getMemoryResult)
            .map(MemoryResult::getBenchmarkData)
            .map(MemoryResult.BenchmarkData::getResults)
            .flatMap(Collection::stream)
            .skip(2)
            .map(x -> x.getJmxMemoryOldCollectionTotalMs() + x.getJmxMemoryYoungCollectionTotalMs()).toList();
    }

    private static List<Long> getTotalCompilationTimeForTestAndIsolation(BenchmarkTest test, Isolation isolation, Map<String, Map<String, List<BenchmarkResult>>> dataset) {
        var testList = dataset.get(isolation.getName())
            .get(test.getName());
        return testList.stream()
            .map(BenchmarkResult::getTimeResult)
            .map(TimeResult::getBenchmarkData)
            .map(TimeResult.BenchmarkData::getResults)
            .flatMap(Collection::stream)
            .skip(2)
            .map(TimeResult.Result::getCompilationTimeTotal).toList();
    }

}
