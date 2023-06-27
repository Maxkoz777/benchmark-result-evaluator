package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoryResult {

    private BenchmarkData benchmarkData;
    private Environment environment;

    @Data
    public static class BenchmarkData {
        private List<Result> results;
        private String termination;
    }

    @Data
    public static class Result {
        @JsonProperty("jmx_memory_young_collection_delta")
        private long jmxMemoryYoungCollectionDelta;
        @JsonProperty("jmx_memory_old_collection_delta")
        private long jmxMemoryOldCollectionDelta;
        @JsonProperty("jmx_memory_old_collection_total_ms")
        private long jmxMemoryOldCollectionTotalMs;
        @JsonProperty("jmx_memory_young_collection_time_ms")
        private long jmxMemoryYoungCollectionTimeMs;
        @JsonProperty("jmx_memory_used_delta")
        private long jmxMemoryUsedDelta;
        @JsonProperty("duration_ns")
        private long durationNs;
        @JsonProperty("jmx_memory_young_collection_count")
        private long jmxMemoryYoungCollectionCount;
        @JsonProperty("jmx_memory_used_size")
        private long jmxMemoryUsedSize;
        @JsonProperty("uptime_ns")
        private long uptimeNs;
        @JsonProperty("jmx_memory_young_collection_total_ms")
        private long jmxMemoryYoungCollectionTotalMs;
        @JsonProperty("jmx_memory_old_collection_time_ms")
        private long jmxMemoryOldCollectionTimeMs;
        @JsonProperty("jmx_memory_old_collection_count")
        private long jmxMemoryOldCollectionCount;
    }

}
