package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    
    // User Transactions
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
    
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserByIdFlow(userId: Int): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long
    
    @Update
    suspend fun updateUser(user: User)

    // Experience Transactions
    @Query("SELECT * FROM experiences WHERE userId = :userId ORDER BY id DESC")
    fun getExperiencesForUser(userId: Int): Flow<List<ExperienceItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperience(experience: ExperienceItem): Long

    @Query("DELETE FROM experiences WHERE id = :experienceId")
    suspend fun deleteExperience(experienceId: Int)

    // Education Transactions
    @Query("SELECT * FROM educations WHERE userId = :userId ORDER BY id DESC")
    fun getEducationsForUser(userId: Int): Flow<List<EducationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEducation(education: EducationItem): Long

    @Query("DELETE FROM educations WHERE id = :educationId")
    suspend fun deleteEducation(educationId: Int)

    // Vacancy Transactions
    @Query("SELECT * FROM vacancies ORDER BY id DESC")
    fun getAllVacancies(): Flow<List<Vacancy>>

    @Query("SELECT * FROM vacancies WHERE companyId = :companyId ORDER BY id DESC")
    fun getVacanciesByCompany(companyId: Int): Flow<List<Vacancy>>

    @Query("SELECT * FROM vacancies WHERE id = :vacancyId LIMIT 1")
    suspend fun getVacancyById(vacancyId: Int): Vacancy?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: Vacancy): Long

    @Query("UPDATE vacancies SET viewsCount = viewsCount + 1 WHERE id = :vacancyId")
    suspend fun incrementVacancyViews(vacancyId: Int)

    // Application Transactions
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun applyForVacancy(application: JobApplication): Long

    @Query("DELETE FROM applications WHERE vacancyId = :vacancyId AND candidateId = :candidateId")
    suspend fun withdrawApplication(vacancyId: Int, candidateId: Int)

    @Query("SELECT * FROM applications WHERE candidateId = :candidateId")
    fun getApplicationsForCandidate(candidateId: Int): Flow<List<JobApplication>>

    @Query("SELECT * FROM applications WHERE vacancyId = :vacancyId")
    fun getApplicationsForVacancy(vacancyId: Int): Flow<List<JobApplication>>

    @Query("SELECT COUNT(*) FROM applications WHERE vacancyId = :vacancyId")
    fun getApplicationCountForVacancy(vacancyId: Int): Flow<Int>

    @Query("SELECT users.* FROM users INNER JOIN applications ON users.id = applications.candidateId WHERE applications.vacancyId = :vacancyId")
    fun getAppliedCandidatesForVacancy(vacancyId: Int): Flow<List<User>>

    // Favorite Transactions
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(favorite: FavoriteVacancy): Long

    @Query("DELETE FROM favorites WHERE vacancyId = :vacancyId AND candidateId = :candidateId")
    suspend fun removeFavorite(vacancyId: Int, candidateId: Int)

    @Query("SELECT * FROM favorites WHERE candidateId = :candidateId")
    fun getFavoritesForCandidate(candidateId: Int): Flow<List<FavoriteVacancy>>
}
