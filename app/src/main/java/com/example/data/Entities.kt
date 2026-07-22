package com.example.data

import androidx.room.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val name: String = "",
    val cpf: String = "",
    val phone: String = "",
    val password: String = "",
    @SerialName("is_company") val isCompany: Boolean = false,
    
    // Company specific
    @SerialName("company_social_reason") val companySocialReason: String? = null,
    @SerialName("company_cnpj") val companyCnpj: String? = null,
    @SerialName("company_size") val companySize: String? = null,
    
    // Candidate specific
    @SerialName("professional_title") val professionalTitle: String? = null,
    @SerialName("professional_summary") val professionalSummary: String? = null,
    val skills: String? = null // comma separated e.g. "React,TypeScript,Node.js"
)

@Serializable
@Entity(tableName = "experiences")
data class ExperienceItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerialName("user_id") val userId: Int,
    val title: String,
    @SerialName("company_name") val companyName: String,
    val period: String,
    @SerialName("work_mode") val workMode: String
)

@Serializable
@Entity(tableName = "educations")
data class EducationItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerialName("user_id") val userId: Int,
    val degree: String,
    val institution: String,
    val period: String
)

@Serializable
@Entity(tableName = "vacancies")
data class Vacancy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerialName("company_id") val companyId: Int,
    @SerialName("company_name") val companyName: String,
    val title: String,
    @SerialName("contract_type") val contractType: String, // CLT, PJ, Estágio
    @SerialName("work_mode") val workMode: String,     // Presencial, Híbrido, Remoto
    val location: String,
    @SerialName("salary_min") val salaryMin: String? = null,
    @SerialName("salary_max") val salaryMax: String? = null,
    val requirements: String,
    val deadline: String,
    @SerialName("views_count") val viewsCount: Int = 0
)

@Serializable
@Entity(tableName = "applications", indices = [Index(value = ["vacancyId", "candidateId"], unique = true)])
data class JobApplication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerialName("vacancy_id") val vacancyId: Int,
    @SerialName("candidate_id") val candidateId: Int
)

@Serializable
@Entity(tableName = "favorites", indices = [Index(value = ["vacancyId", "candidateId"], unique = true)])
data class FavoriteVacancy(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerialName("vacancy_id") val vacancyId: Int,
    @SerialName("candidate_id") val candidateId: Int
)

