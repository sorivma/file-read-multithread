package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

public class FileSumTask extends FileCounter implements Callable<Long>{
    private String fileName;

    public FileSumTask(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Long call() throws Exception {
        return super.count(fileName);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
