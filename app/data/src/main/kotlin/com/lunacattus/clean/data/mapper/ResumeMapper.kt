package com.lunacattus.clean.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lunacattus.clean.data.local.entity.ResumeEntity
import com.lunacattus.clean.domain.model.Education
import com.lunacattus.clean.domain.model.Resume
import com.lunacattus.clean.domain.model.WorkExperience

object ResumeMapper {

    fun Resume.mapper(): ResumeEntity {
        return ResumeEntity(
            id = this.id,
            name = this.name,
            age = this.age,
            email = this.email,
            phone = this.phone,
            educationList = Gson().toJson(this.education),
            workExperienceList = Gson().toJson(this.workExperience),
            skillList = Gson().toJson(this.skills)
        )
    }

    fun ResumeEntity.mapper(): Resume {
        return Resume(
            id = this.id,
            name = this.name,
            age = this.age,
            email = this.email,
            phone = this.phone,
            education = Gson().fromJson(
                this.educationList,
                object : TypeToken<List<Education>>() {}.type
            ),
            workExperience = Gson().fromJson(
                this.workExperienceList,
                object : TypeToken<List<WorkExperience>>() {}.type
            ),
            skills = Gson().fromJson(
                this.skillList,
                object : TypeToken<String>() {}.type
            ),
        )
    }
}