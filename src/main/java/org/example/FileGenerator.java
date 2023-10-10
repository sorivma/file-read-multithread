package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileGenerator {
    private final int fileNum;
    private final int numberNum;
    private final String folder;

    public FileGenerator(int fileNum, int numberNum, String folder) {
        this.fileNum = fileNum;
        this.numberNum = numberNum;
        this.folder = folder;
    }

    public GenerationResult generate() {
        Set<String> filePaths = generatePaths();

        if (Objects.requireNonNull(new File(folder).list()).length == fileNum) {
            return new GenerationResult(GenerationResult.State.NOT_NEEDED, filePaths);
        }

        for (String path: filePaths) {
            List<String> numbers = new ArrayList<>();

            for (int j = 0; j < numberNum; j++) {
                numbers.add(String.valueOf((int) Math.floor(Math.random() * 20) - 10));
            }

            Path file = Paths.get(path);
            try {
                Files.write(file, numbers, StandardCharsets.UTF_8);
            } catch (IOException e) {
                return new GenerationResult(GenerationResult.State.FAILED, Collections.emptySet());
            }
        }

        return new GenerationResult(GenerationResult.State.GENERATED, filePaths);
    }

    private Set<String> generatePaths() {
        Set<String> filePaths = new HashSet<>();

        for (int i = 0; i < fileNum; i++) {
            String fullPath = folder + "/file-" + i + ".txt";
            filePaths.add(fullPath);
        }

        return filePaths;
    }

    public record GenerationResult(FileGenerator.GenerationResult.State state, Set<String> generatedFiles) {
        public enum State {
            GENERATED,
            NOT_NEEDED,
            FAILED,
        }
    }
}
