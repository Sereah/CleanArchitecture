package com.lunacattus.clean.domain.model

data class Resume(
    val id: String,
    val name: String,
    val age: Int,
    val email: String,
    val phone: String,
    val education: List<Education>,
    val workExperience: List<WorkExperience>,
    val skills: List<String>
)