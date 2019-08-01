package com.reinforce.sdk.changemethod

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.Directory
import org.gradle.api.tasks.TaskAction

/**
 * @anthor kb_jay* create at 2019-08-01 13:13
 */
class ChangeMethodTask extends DefaultTask {
    String destDir
    String sourceDir

    ChangeMethodTask() {
        group = "reinforce"
        description = "抽空方法"
    }

    @TaskAction
    void doAction() {
        sourceDir = project.extensions.changeMethod.sourcePath
        destDir = project.extensions.changeMethod.destPath

        File sourceFileDir = new File(sourceDir)

        if (!sourceFileDir.isDirectory()) {
            throw new GradleException("source 参数错误！！")
        }
        traverseSource(sourceFileDir)
    }

    void traverseSource(File source) {
        source.eachFile { file ->
            if (file.isDirectory()) {
                String temp = file.getAbsolutePath().substring(sourceDir.length())
                new File(destDir + temp).mkdirs()
                traverseSource(file)
            } else {
                String temp = file.getAbsolutePath().substring(sourceDir.length())
                JavaChangeMethodUtils.changeMethod(file.getAbsolutePath(), destDir + temp)
            }
        }
    }
}