package model.enums;

import lombok.Getter;

public enum BenchmarkTest {
    DB_SHOOTOUT("db-shootout"),
    DEC_TREE("dec-tree"),
    FINAGLE_CHIRPER("finagle-chirper"),
    FJ_KMEANS("fj-kmeans"),
    FUTURE_GENETIC("future-genetic"),
    SCALA_KMEANS("scala-kmeans");

    @Getter
    private final String name;

    BenchmarkTest(String name) {
        this.name = name;
    }
}
