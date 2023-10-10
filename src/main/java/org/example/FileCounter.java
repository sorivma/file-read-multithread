package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileCounter {
    public Long count(String fileName) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))){
            return stream.mapToLong(Long::valueOf).sum();
        }
    }
}
