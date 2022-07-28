package com.didi.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.SkipWhenEmpty;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateTask extends DefaultTask {
    private FileCollection inputFiles;

    private File outputFile;

    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    @SkipWhenEmpty
    public FileCollection getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(FileCollection inputFiles) {
        this.inputFiles = inputFiles;
    }

    @OutputFile
    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    @TaskAction
    public void generate() throws IOException {
        outputFile.getParentFile().mkdirs();

        List<String> statements = inputFiles.getFiles().stream()
            .filter(file -> file.getName().endsWith(".class"))
            .map(file -> file.getName().substring(0, file.getName().length() - 6))
            .map(className -> "   private com.didi." + className + " _" + className + ";")
            .collect(Collectors.toList());

        List<String> lines = new ArrayList<>();
        lines.add("package com.didi;");
        lines.add("class ServiceRegistry {");
        lines.addAll(statements);
        lines.add("}");

        Files.write(outputFile.toPath(), lines);
    }
}
