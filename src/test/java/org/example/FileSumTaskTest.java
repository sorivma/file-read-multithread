package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileSumTaskTest {
    @Test
    void testCall(){
        try {
            System.out.println(new FileSumTask("C:\\Users\\soriv\\OneDrive\\Рабочий стол\\data\\file-16.txt").call());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}