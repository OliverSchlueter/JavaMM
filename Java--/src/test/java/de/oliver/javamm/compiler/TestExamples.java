package de.oliver.javamm.compiler;

import org.junit.jupiter.api.Test;
import java.io.File;

class TestExamples {

    @Test
    void test(){
        File examples = new File("C:\\Users\\Oliver\\Desktop\\JavaPP\\Examples");
        for (File file : examples.listFiles()) {
            Compiler compiler = new Compiler(file.getPath());
            compiler.simulate();
        }
    }

}