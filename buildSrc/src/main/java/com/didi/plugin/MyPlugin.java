package com.didi.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.compile.JavaCompile;

import java.util.Arrays;

class MyPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        GenerateTask generateTask = project.getTasks().create("generateServiceRegistry", GenerateTask.class, task -> {
            task.dependsOn("compileJava");
            task.setInputFiles(project.fileTree("build/classes/java/main").filter(file -> !file.getName().contains("ServiceRegistry")));
            task.setOutputFile(project.file("build/intermediate/com/didi/ServiceRegistry.java"));
        });

        project.getTasks().create("compileGeneratedServiceRegistry", JavaCompile.class, task -> {
            task.setSource(project.fileTree("build/intermediate"));
            task.setIncludes(Arrays.asList("**/*.java"));
            task.setClasspath(project.files("build/classes/java/main"));
            task.setDestinationDir(project.file("build/classes/java/main"));
            task.dependsOn(generateTask);
        });
    }
}
