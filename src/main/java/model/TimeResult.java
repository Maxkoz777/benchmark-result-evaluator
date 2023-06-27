package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeResult {

    private BenchmarkData benchmarkData;
    private Environment environment;

    @Data
    public static class BenchmarkData {
        private List<Result> results;
        private String termination;
    }

    @Data
    public static class Result {
        @JsonProperty("duration_ns")
        private BigDecimal duration;
        @JsonProperty("jmx_timers_compilation_time_ms")
        private long compilationTime;
        @JsonProperty("jmx_timers_compilation_total_ms")
        private long compilationTimeTotal;
        @JsonProperty("uptime_ns")
        private long uptime;
    }

}
