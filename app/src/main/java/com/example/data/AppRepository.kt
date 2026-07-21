package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {

    // User operations
    suspend fun getUserByEmail(email: String): User? = appDao.getUserByEmail(email)
    suspend fun getUserById(userId: Int): User? = appDao.getUserById(userId)
    fun getUserByIdFlow(userId: Int): Flow<User?> = appDao.getUserByIdFlow(userId)
    suspend fun insertUser(user: User): Long = appDao.insertUser(user)
    suspend fun updateUser(user: User) = appDao.updateUser(user)

    // Experience operations
    fun getExperiencesForUser(userId: Int): Flow<List<ExperienceItem>> = appDao.getExperiencesForUser(userId)
    suspend fun insertExperience(experience: ExperienceItem) = appDao.insertExperience(experience)
    suspend fun deleteExperience(experienceId: Int) = appDao.deleteExperience(experienceId)

    // Education operations
    fun getEducationsForUser(userId: Int): Flow<List<EducationItem>> = appDao.getEducationsForUser(userId)
    suspend fun insertEducation(education: EducationItem) = appDao.insertEducation(education)
    suspend fun deleteEducation(educationId: Int) = appDao.deleteEducation(educationId)

    // Vacancy operations
    fun getAllVacancies(): Flow<List<Vacancy>> = appDao.getAllVacancies()
    fun getVacanciesByCompany(companyId: Int): Flow<List<Vacancy>> = appDao.getVacanciesByCompany(companyId)
    suspend fun getVacancyById(vacancyId: Int): Vacancy? = appDao.getVacancyById(vacancyId)
    suspend fun insertVacancy(vacancy: Vacancy): Long = appDao.insertVacancy(vacancy)
    suspend fun incrementVacancyViews(vacancyId: Int) = appDao.incrementVacancyViews(vacancyId)

    // Application operations
    suspend fun applyForVacancy(vacancyId: Int, candidateId: Int): Long {
        return appDao.applyForVacancy(JobApplication(vacancyId = vacancyId, candidateId = candidateId))
    }
    suspend fun withdrawApplication(vacancyId: Int, candidateId: Int) {
        appDao.withdrawApplication(vacancyId, candidateId)
    }
    fun getApplicationsForCandidate(candidateId: Int): Flow<List<JobApplication>> = appDao.getApplicationsForCandidate(candidateId)
    fun getApplicationsForVacancy(vacancyId: Int): Flow<List<JobApplication>> = appDao.getApplicationsForVacancy(vacancyId)
    fun getApplicationCountForVacancy(vacancyId: Int): Flow<Int> = appDao.getApplicationCountForVacancy(vacancyId)
    fun getAppliedCandidatesForVacancy(vacancyId: Int): Flow<List<User>> = appDao.getAppliedCandidatesForVacancy(vacancyId)

    // Favorite operations
    suspend fun addFavorite(vacancyId: Int, candidateId: Int): Long = appDao.addFavorite(FavoriteVacancy(vacancyId = vacancyId, candidateId = candidateId))
    suspend fun removeFavorite(vacancyId: Int, candidateId: Int) = appDao.removeFavorite(vacancyId, candidateId)
    fun getFavoritesForCandidate(candidateId: Int): Flow<List<FavoriteVacancy>> = appDao.getFavoritesForCandidate(candidateId)
}
