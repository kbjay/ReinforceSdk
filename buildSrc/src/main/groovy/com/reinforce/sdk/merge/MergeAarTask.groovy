package com.reinforce.sdk.merge

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

/**
 *
 * @anthor kb_jay* create at 2019-08-01 13:14
 */
class MergeAarTask extends DefaultTask {

    MergeAarTask() {
        group = "reinforce"
        description = "合并aar"
    }

    @TaskAction
    void doAction() {

        String emptyAarPath = project.extensions.mergeAar.emptyAarPath
        if (emptyAarPath == null || emptyAarPath == "") {
            throw new GradleException("emptyAarPath == null")
        }
        String realAarPath = project.extensions.mergeAar.realAarPath
        if (realAarPath == null || realAarPath == "") {
            throw new GradleException("realAarPath == null")
        }
        String destAarPath = project.extensions.mergeAar.destAarPath
        if (destAarPath == null || destAarPath == "") {
            throw new GradleException("destAarPath == null")
        }
        String sdkPath = project.extensions.mergeAar.sdkPath
        if (sdkPath == null || sdkPath == "") {
            throw new GradleException("sdkPath == null")
        }
        String destAarName = project.extensions.mergeAar.destAarName
        if (destAarName == null || destAarName == "") {
            destAarName = "reinforce.aar"
        }
//        解压获取realAar跟emptyAar
//        emptyAar的asset中新建一个resource.txt
//        将realAar中的jar文件转成dex文件
//        将dex文件copy到resource.txt中。
//        txt文件后面添加数据（xireinforce）
        File buildFile = new File(destAarPath + "/reinforce")
        buildFile.mkdir()

        File emptyDir = new File(buildFile, "empty")
        emptyDir.mkdir()

        File realDir = new File(buildFile, "real")
        realDir.mkdir()

        ZipUtils.unzip(new File(emptyAarPath), emptyDir)
        ZipUtils.unzip(new File(realAarPath), realDir)

        println "[+] unzip success"

        String command = "${sdkPath}/dx --dex --output=${realDir.getAbsolutePath()}/real.dex ${realDir.getAbsolutePath()}/classes.jar"
        def execute = command.execute()
        execute.waitFor()

        println "[+] jar->dex success"

        File realDex = new File(realDir, "real.dex")

        File emptyAssets = new File(emptyDir, "assets")
        emptyAssets.mkdir()

        File targetFile = new File(emptyAssets, "resource.txt")

        targetFile.withDataOutputStream { os ->
            realDex.withDataInputStream { is ->
                os << is
            }
        }

        targetFile.append("xireinforce".getBytes())

        println "[+] move dex and add secret success"

        ZipUtils.zip(emptyDir.getAbsolutePath(), buildFile.getAbsolutePath() + "/" + destAarName)

        println "[+] merge success -> ${buildFile.getAbsolutePath() + "/" + destAarName}"

        emptyDir.deleteDir()
        realDir.deleteDir()

    }
}