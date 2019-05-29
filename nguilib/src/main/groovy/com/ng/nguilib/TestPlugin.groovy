package com.ng.nguilib

import org.gradle.api.Plugin
import opg.gradle.api.Project

public class TestPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println "make success"
    }
}