package com.github.riadzx.xscdscsdpsttmvdv.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.github.riadzx.xscdscsdpsttmvdv.MyBundle

@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) {

    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
    }

    fun getRandomNumber() = (1..100).random()
}
