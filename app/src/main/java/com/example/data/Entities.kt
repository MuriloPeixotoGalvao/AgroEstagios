package com.example.data

import androidx.room.*

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val name: String,
    val cpf: String,
    val phone: String,
    val password: String,
    val isCompany: Boolean,
    
    // Company specific
    val companySocialReason: String? = null,
    val companyCnpj: String? = null,
    val companySize: String? = null,
    
    // Candidate specific
    val professionalTitle: String? = null,
    val professionalSummary: String? = null,
    val skills: String? = null // comma separated e.g. "React,TypeScript,Node.js"
)

@Entity(tableName = "experiences")
data class ExperienceItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val title: String,
    val companyName: String,
    val period: String,
    val workMode: String
)

@Entity(tableName = "educations")
data class EducationItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val degree: String,
    val institution: String,
    val period: String
)

@Entity(tableName = "vacancies")
data class Vacancy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val companyId: Int,
    val companyName: String,
    val title: String,
    val contractType: String, // CLT, PJ, Estágio
    val workMode: String,     // Presencial, Híbrido, Remoto
    val location: String,
    val salaryMin: String? = null,
    val salaryMax: String? = null,
    val requirements: String,
    val deadline: String,
    val viewsCount: Int = 0
)

@Entity(tableName = "applications", indices = [Index(value = ["vacancyId", "candidateId"], unique = true)])
data class JobApplication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vacancyId: Int,
    val candidateId: Int
)
