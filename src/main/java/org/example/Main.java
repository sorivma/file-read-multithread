package org.example;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private final static Integer FILES_NUM = 17;
    private final static Integer INTEGERS_NUM = 10000;
    private final static Integer THREADS = 3;
    private final static String FOLDER_PATH = "C:/Users/soriv/OneDrive/Рабочий стол/data";

    public static void main(String[] args) {
        Set<String> fileNames = prepare();
        long start = System.nanoTime();
        Long sum = count(fileNames);
        long end = System.nanoTime();
        System.out.println("Sum by single thread execution: " + sum + " Completed in: "
                        + (end - start) / 10_000 + "(ms)");

        start = System.nanoTime();
        sum = countThreaded(fileNames);
        end = System.nanoTime();
        System.out.println("Sum by threaded execution: " + sum + " Completed in: " +
                (end - start) / 10_000 + "(ms)");
    }

    private static Long countThreaded(Set<String> fileNames) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
        Set<Future<Long>> sums = new HashSet<>();
        for (String fileName : fileNames) {
            Future<Long> future = executorService.submit(new FileSumTask(
                    fileName
            ));
            sums.add(future);
        }

        Long sum = 0L;
        for (Future<Long> future : sums) {
            try {
                sum += future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        executorService.shutdown();

        return sum;
    }

    private static Long count(Set<String> fileNames) {
        FileCounter fileCounter = new FileCounter();
        Long sum = 0L;
        for (String fileName : fileNames) {
            try {
                sum += fileCounter.count(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return sum;
    }


    public static Set<String> prepare() {
        FileGenerator fileGenerator = new FileGenerator(FILES_NUM, INTEGERS_NUM, FOLDER_PATH);
        FileGenerator.GenerationResult result = fileGenerator.generate();

        if (result.state().equals(FileGenerator.GenerationResult.State.NOT_NEEDED)) {
            System.out.println("FILES ALREADY GENERATED");
            return result.generatedFiles();
        }

        if (result.state().equals(FileGenerator.GenerationResult.State.FAILED)) {
            throw new RuntimeException("IO EXCEPTION HAPPENED");
        }

        System.out.println("GENERATED FILES\n" + result.generatedFiles());
        return result.generatedFiles();
    }
}