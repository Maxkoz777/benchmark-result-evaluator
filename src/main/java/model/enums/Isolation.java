package model.enums;

import lombok.Getter;

public enum Isolation {
    HOST("host"),
    VM("vm"),
    DOCKER("docker"),
    DOCKER_WITH_RESTRICTIONS("docker-with-restrictions");

    @Getter
    private final String name;

    Isolation(String name) {
        this.name = name;
    }
}
