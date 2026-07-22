package com.example.data

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {

    private val supabase: SupabaseClient?
        get() = SupabaseClientProvider.client

    private fun isSupabaseConfigured(): Boolean {
        return SupabaseClientProvider.isConfigured
    }

    // Auth operations
    suspend fun signUpWithSupabase(user: User): Result<User> {
        return try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.auth.signUpWith(Email) {
                    email = user.email
                    password = user.password
                }
                client.from("users").insert(user)
            }
            val localId = appDao.insertUser(user)
            val registered = user.copy(id = localId.toInt())
            Result.success(registered)
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase signUp error", e)
            try {
                val localId = appDao.insertUser(user)
                Result.success(user.copy(id = localId.toInt()))
            } catch (localEx: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun signInWithSupabase(emailInput: String, passwordInput: String): Result<User?> {
        return try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.auth.signInWith(Email) {
                    email = emailInput
                    password = passwordInput
                }
                val remoteUser = client.from("users").select {
                    filter { eq("email", emailInput) }
                }.decodeSingleOrNull<User>()

                if (remoteUser != null) {
                    appDao.insertUser(remoteUser)
                    return Result.success(remoteUser)
                }
            }
            val localUser = appDao.getUserByEmail(emailInput)
            if (localUser != null && localUser.password == passwordInput) {
                Result.success(localUser)
            } else {
                Result.failure(Exception("Credenciais incorretas ou usuário não encontrado."))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase signIn error", e)
            val localUser = appDao.getUserByEmail(emailInput)
            if (localUser != null && localUser.password == passwordInput) {
                Result.success(localUser)
            } else {
                Result.failure(e)
            }
        }
    }

    // User operations
    suspend fun getUserByEmail(email: String): User? = appDao.getUserByEmail(email)
    suspend fun getUserById(userId: Int): User? = appDao.getUserById(userId)
    fun getUserByIdFlow(userId: Int): Flow<User?> = appDao.getUserByIdFlow(userId)
    suspend fun insertUser(user: User): Long = appDao.insertUser(user)

    suspend fun updateUser(user: User) {
        appDao.updateUser(user)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.from("users").update(user) {
                    filter { eq("id", user.id) }
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase updateUser error", e)
        }
    }

    // Sync remote vacancies
    suspend fun syncVacanciesFromSupabase() {
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                val remoteVacancies = client.from("vacancies").select().decodeList<Vacancy>()
                if (remoteVacancies.isNotEmpty()) {
                    remoteVacancies.forEach { appDao.insertVacancy(it) }
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase syncVacancies error", e)
        }
    }

    // Sync user activity
    suspend fun syncUserDataFromSupabase(userId: Int) {
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                val remoteApps = client.from("applications").select {
                    filter { eq("candidate_id", userId) }
                }.decodeList<JobApplication>()
                remoteApps.forEach { appDao.applyForVacancy(it) }

                val remoteFavs = client.from("favorites").select {
                    filter { eq("candidate_id", userId) }
                }.decodeList<FavoriteVacancy>()
                remoteFavs.forEach { appDao.addFavorite(it) }

                val remoteExps = client.from("experiences").select {
                    filter { eq("user_id", userId) }
                }.decodeList<ExperienceItem>()
                remoteExps.forEach { appDao.insertExperience(it) }

                val remoteEds = client.from("educations").select {
                    filter { eq("user_id", userId) }
                }.decodeList<EducationItem>()
                remoteEds.forEach { appDao.insertEducation(it) }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase syncUserData error", e)
        }
    }

    // Experience operations
    fun getExperiencesForUser(userId: Int): Flow<List<ExperienceItem>> = appDao.getExperiencesForUser(userId)

    suspend fun insertExperience(experience: ExperienceItem) {
        val localId = appDao.insertExperience(experience)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.from("experiences").insert(experience.copy(id = localId.toInt()))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase insertExperience error", e)
        }
    }

    suspend fun deleteExperience(experienceId: Int) {
        appDao.deleteExperience(experienceId)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.from("experiences").delete {
                    filter { eq("id", experienceId) }
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase deleteExperience error", e)
        }
    }

    // Education operations
    fun getEducationsForUser(userId: Int): Flow<List<EducationItem>> = appDao.getEducationsForUser(userId)

    suspend fun insertEducation(education: EducationItem) {
        val localId = appDao.insertEducation(education)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.from("educations").insert(education.copy(id = localId.toInt()))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase insertEducation error", e)
        }
    }

    suspend fun deleteEducation(educationId: Int) {
        appDao.deleteEducation(educationId)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.from("educations").delete {
                    filter { eq("id", educationId) }
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase deleteEducation error", e)
        }
    }

    // Vacancy operations
    fun getAllVacancies(): Flow<List<Vacancy>> = appDao.getAllVacancies()
    fun getVacanciesByCompany(companyId: Int): Flow<List<Vacancy>> = appDao.getVacanciesByCompany(companyId)
    suspend fun getVacancyById(vacancyId: Int): Vacancy? = appDao.getVacancyById(vacancyId)

    suspend fun insertVacancy(vacancy: Vacancy): Long {
        val localId = appDao.insertVacancy(vacancy)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                val toInsert = if (vacancy.id == 0) vacancy.copy(id = localId.toInt()) else vacancy
                client.from("vacancies").insert(toInsert)
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase insertVacancy error", e)
        }
        return localId
    }

    suspend fun incrementVacancyViews(vacancyId: Int) {
        appDao.incrementVacancyViews(vacancyId)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                val current = getVacancyById(vacancyId)
                if (current != null) {
                    client.from("vacancies").update(mapOf("views_count" to current.viewsCount)) {
                        filter { eq("id", vacancyId) }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase incrementVacancyViews error", e)
        }
    }

    // Application operations
    suspend fun applyForVacancy(vacancyId: Int, candidateId: Int): Long {
        val jobApp = JobApplication(vacancyId = vacancyId, candidateId = candidateId)
        val localId = appDao.applyForVacancy(jobApp)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.from("applications").insert(jobApp.copy(id = localId.toInt()))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase applyForVacancy error", e)
        }
        return localId
    }

    suspend fun withdrawApplication(vacancyId: Int, candidateId: Int) {
        appDao.withdrawApplication(vacancyId, candidateId)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.from("applications").delete {
                    filter {
                        eq("vacancy_id", vacancyId)
                        eq("candidate_id", candidateId)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase withdrawApplication error", e)
        }
    }

    fun getApplicationsForCandidate(candidateId: Int): Flow<List<JobApplication>> = appDao.getApplicationsForCandidate(candidateId)
    fun getApplicationsForVacancy(vacancyId: Int): Flow<List<JobApplication>> = appDao.getApplicationsForVacancy(vacancyId)
    fun getApplicationCountForVacancy(vacancyId: Int): Flow<Int> = appDao.getApplicationCountForVacancy(vacancyId)
    fun getAppliedCandidatesForVacancy(vacancyId: Int): Flow<List<User>> = appDao.getAppliedCandidatesForVacancy(vacancyId)

    // Favorite operations
    suspend fun addFavorite(vacancyId: Int, candidateId: Int): Long {
        val favorite = FavoriteVacancy(vacancyId = vacancyId, candidateId = candidateId)
        val localId = appDao.addFavorite(favorite)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.from("favorites").insert(favorite.copy(id = localId.toInt()))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase addFavorite error", e)
        }
        return localId
    }

    suspend fun removeFavorite(vacancyId: Int, candidateId: Int) {
        appDao.removeFavorite(vacancyId, candidateId)
        try {
            val client = supabase
            if (isSupabaseConfigured() && client != null) {
                client.from("favorites").delete {
                    filter {
                        eq("vacancy_id", vacancyId)
                        eq("candidate_id", candidateId)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Supabase removeFavorite error", e)
        }
    }

    fun getFavoritesForCandidate(candidateId: Int): Flow<List<FavoriteVacancy>> = appDao.getFavoritesForCandidate(candidateId)
}


