package processors;

import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MetricComputationProcessor {

    public double compute50thPercentile(List<Long> values) {
        return computePercentile(50, values);
    }

    public double computePercentile(int percentile, List<Long> values) {
        var position = percentile * 0.01 * values.size();
        var index = (int) position;
        if (position == index) {
            return values.stream().sorted().toList().get(index);
        }
        var elements = values.stream().sorted().skip(index - 1).limit(2).toList();
        return (elements.get(1) - elements.get(0)) * (position - index);
    }

    public double average(List<Long> values) {
        return values.stream().mapToDouble(x->x).average().getAsDouble();
    }

    public double overallRange(List<Long> values) {
        List<Long> list = values.stream().sorted().toList();
        return list.get(list.size() - 1) - list.get(0);
    }



}
