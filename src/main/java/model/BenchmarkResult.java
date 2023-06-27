package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BenchmarkResult {

    private MemoryResult memoryResult;
    private TimeResult timeResult;

}
