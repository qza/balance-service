package org.koko.balance.service.app.data;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Balance log path provider
 */
public class BalanceLogPath {

    private final Path path;

    private static final String PATH = "balance_log";

    public BalanceLogPath() {
        this.path = Paths.get(PATH);
    }

    public BalanceLogPath(String path) {
        this.path = Paths.get(Optional.ofNullable(path).orElse(PATH));
    }

    public Path getPath() {
        return path;
    }
}
