package com.reinforce.sdk

import com.reinforce.sdk.changemethod.ChangeMethodBean
import com.reinforce.sdk.changemethod.ChangeMethodTask
import com.reinforce.sdk.merge.MergeAarBean
import com.reinforce.sdk.merge.MergeAarTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 * @anthor kb_jay* create at 2019-08-01 13:14
 */
class ProjectPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("changeMethod", ChangeMethodBean)
        project.extensions.create("mergeAar", MergeAarBean)

        project.tasks.create("mergeAar", MergeAarTask)
        project.tasks.create("changeMethod", ChangeMethodTask)
    }
}