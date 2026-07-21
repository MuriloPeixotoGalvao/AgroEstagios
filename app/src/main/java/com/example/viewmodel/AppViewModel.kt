package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object CompleteProfile : Screen()
    object ProfileView : Screen()
    object SearchVacancy : Screen()
    object PublishVacancy : Screen()
    object CompanyDashboard : Screen()
}

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = AppRepository(db.appDao)

    // Routing Backstack
    private val _routeStack = MutableStateFlow<List<Screen>>(listOf(Screen.Login))
    val routeStack: StateFlow<List<Screen>> = _routeStack.asStateFlow()

    // Current Session State
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Job search search queries and options
    val searchQuery = MutableStateFlow("")
    val filterWorkMode = MutableStateFlow("Todas") // "Todas", "Remoto", "CLT", "Júnior", "Tecnologia" (treated as custom filters)

    // Database flows compiled reactively
    val allVacancies: StateFlow<List<Vacancy>> = repository.getAllVacancies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered Vacancies derived flow
    val filteredVacancies: StateFlow<List<Vacancy>> = combine(allVacancies, searchQuery, filterWorkMode) { vacancies, query, filter ->
        vacancies.filter { vac ->
            val matchQuery = query.isEmpty() || 
                vac.title.contains(query, ignoreCase = true) || 
                vac.companyName.contains(query, ignoreCase = true) || 
                vac.location.contains(query, ignoreCase = true)
            
            val matchFilter = when (filter) {
                "Todas" -> true
                "Remoto" -> vac.workMode.equals("Remoto", ignoreCase = true)
                "CLT" -> vac.contractType.equals("CLT", ignoreCase = true)
                "Júnior" -> vac.title.contains("Júnior", ignoreCase = true) || vac.title.contains("Junior", ignoreCase = true) || vac.title.contains("Pleno", ignoreCase = true).not() && vac.title.contains("Sênior", ignoreCase = true).not()
                "Tecnologia" -> vac.title.contains("Developer", ignoreCase = true) || vac.title.contains("Designer", ignoreCase = true) || vac.title.contains("Desenvolvedor", ignoreCase = true) || vac.title.contains("Analista", ignoreCase = true)
                else -> true
            }
            matchQuery && matchFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Precompiled user metrics and flows
    val candidateExperiences: StateFlow<List<ExperienceItem>> = _currentUser.flatMapLatest { user ->
        if (user != null) repository.getExperiencesForUser(user.id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val candidateEducations: StateFlow<List<EducationItem>> = _currentUser.flatMapLatest { user ->
        if (user != null) repository.getEducationsForUser(user.id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recruiterVacancies: StateFlow<List<Vacancy>> = _currentUser.flatMapLatest { user ->
        if (user != null && user.isCompany) repository.getVacanciesByCompany(user.id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // State flow for my job applications (as candidate)
    val candidateApplications: StateFlow<List<JobApplication>> = _currentUser.flatMapLatest { user ->
        if (user != null && !user.isCompany) repository.getApplicationsForCandidate(user.id) else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        viewModelScope.launch {
            // Check if seeding is required
            val existingVacancies = db.appDao.getAllVacancies().first()
            if (existingVacancies.isEmpty()) {
                // First, insert some seeded companies and candidates
                val lucas = User(
                    email = "lucas@example.com",
                    name = "Lucas Silva",
                    cpf = "123.456.789-00",
                    phone = "(11) 99999-9999",
                    password = "password",
                    isCompany = false,
                    professionalTitle = "Desenvolvedor Full Stack Sênior",
                    professionalSummary = "Apaixonado por criar soluções escaláveis e experiências de usuário memoráveis. Com mais de 6 anos de experiência em React, Node.js e design de sistemas.",
                    skills = "React,TypeScript,Node.js,UX Design,Docker,AWS"
                )
                val candidateId = db.appDao.insertUser(lucas).toInt()

                // Insert seed experiences for Lucas
                db.appDao.insertExperience(ExperienceItem(userId = candidateId, title = "Desenvolvedor Sênior", companyName = "TechNova Solutions", period = "Jan 2021 - Presente", workMode = "Remoto"))
                db.appDao.insertExperience(ExperienceItem(userId = candidateId, title = "Desenvolvedor Pleno", companyName = "Global Payments Inc.", period = "Mar 2018 - Dez 2020", workMode = "Híbrido"))
                db.appDao.insertExperience(ExperienceItem(userId = candidateId, title = "Desenvolvedor Júnior", companyName = "Inova Startup", period = "Jan 2016 - Fev 2018", workMode = "Presencial"))

                // Insert seed education for Lucas
                db.appDao.insertEducation(EducationItem(userId = candidateId, degree = "Análise e Desenvolvimento de Sistemas", institution = "USP - ESALQ", period = "2015 - 2018"))

                val techSolutions = User(
                    email = "rh@suaempresa.com.br",
                    name = "TechSolutions",
                    cpf = "000.000.000-00",
                    phone = "(11) 88888-8888",
                    password = "password",
                    isCompany = true,
                    companySocialReason = "Tech Soluções Inovadoras LTDA",
                    companyCnpj = "12.345.678/0001-90",
                    companySize = "Média"
                )
                val companyId = db.appDao.insertUser(techSolutions).toInt()

                // Insert seed vacancies matching screenshots
                db.appDao.insertVacancy(
                    Vacancy(
                        companyId = companyId,
                        companyName = "Nubank",
                        title = "Product Designer Sênior",
                        contractType = "Full-Time",
                        workMode = "Remoto",
                        location = "São Paulo, SP",
                        salaryMin = "12000",
                        salaryMax = "18000",
                        requirements = "Atuar no design de soluções financeiras de ponta, focado na experiência do usuário de varejo digital.",
                        deadline = "30/07/2026",
                        viewsCount = 842
                    )
                )

                db.appDao.insertVacancy(
                    Vacancy(
                        companyId = companyId,
                        companyName = "Itaú Unibanco",
                        title = "Desenvolvedor Backend Java",
                        contractType = "CLT",
                        workMode = "Híbrido",
                        location = "São Paulo, SP",
                        salaryMin = "9000",
                        salaryMax = "14000",
                        requirements = "Desenvolvimento de microserviços em Java e Spring Boot com deploy em nuvem AWS.",
                        deadline = "25/07/2026",
                        viewsCount = 320
                    )
                )

                db.appDao.insertVacancy(
                    Vacancy(
                        companyId = companyId,
                        companyName = "iFood",
                        title = "Analista de Dados Pleno",
                        contractType = "Pleno",
                        workMode = "Remoto",
                        location = "Osasco, SP",
                        salaryMin = "7000",
                        salaryMax = "11000",
                        requirements = "Extração de valor de grandes volumes de logs de delivery com Spark, SQL e visualizações com Python.",
                        deadline = "15/07/2026",
                        viewsCount = 158
                    )
                )

                db.appDao.insertVacancy(
                    Vacancy(
                        companyId = companyId,
                        companyName = "Mercado Livre",
                        title = "Gerente de Logística",
                        contractType = "Sênior",
                        workMode = "Presencial",
                        location = "Cajamar, SP",
                        salaryMin = "14000",
                        salaryMax = "21000",
                        requirements = "Gestão estratégica de centros de distribuição rápida nível LATAM.",
                        deadline = "12/07/2026",
                        viewsCount = 422
                    )
                )
            }
        }
    }

    // Navigation logic
    fun navigateTo(screen: Screen) {
        val current = _routeStack.value.toMutableList()
        current.add(screen)
        _routeStack.value = current
    }

    fun navigateBack() {
        val current = _routeStack.value.toMutableList()
        if (current.size > 1) {
            current.removeAt(current.size - 1)
            _routeStack.value = current
        }
    }

    fun navigateAndClear(screen: Screen) {
        _routeStack.value = listOf(screen)
    }

    // Auth actions
    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            if (user != null && user.password == password) {
                _currentUser.value = user
                onSuccess()
            } else {
                onError("Credenciais incorretas ou usuário não encontrado.")
            }
        }
    }

    fun registerCandidate(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val existing = repository.getUserByEmail(user.email)
            if (existing != null) {
                onError("E-mail já cadastrado.")
                return@launch
            }
            val id = repository.insertUser(user)
            val registered = user.copy(id = id.toInt())
            _currentUser.value = registered
            onSuccess()
        }
    }

    fun registerCompany(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val existing = repository.getUserByEmail(user.email)
            if (existing != null) {
                onError("E-mail corporativo já cadastrado.")
                return@launch
            }
            val id = repository.insertUser(user)
            val registered = user.copy(id = id.toInt())
            _currentUser.value = registered
            onSuccess()
        }
    }

    fun logout() {
        _currentUser.value = null
        navigateAndClear(Screen.Login)
    }

    // Profile updates
    fun updateCandidateProfile(title: String, summary: String, skills: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val updatedUser = user.copy(
                professionalTitle = title,
                professionalSummary = summary,
                skills = skills
            )
            repository.updateUser(updatedUser)
            _currentUser.value = updatedUser
            onSuccess()
        }
    }

    fun addExperience(title: String, company: String, period: String, workMode: String) {
        viewModelScope.launch {
            val userId = _currentUser.value?.id ?: return@launch
            repository.insertExperience(ExperienceItem(userId = userId, title = title, companyName = company, period = period, workMode = workMode))
        }
    }

    fun deleteExperience(id: Int) {
        viewModelScope.launch {
            repository.deleteExperience(id)
        }
    }

    fun addEducation(degree: String, institution: String, period: String) {
        viewModelScope.launch {
            val userId = _currentUser.value?.id ?: return@launch
            repository.insertEducation(EducationItem(userId = userId, degree = degree, institution = institution, period = period))
        }
    }

    fun deleteEducation(id: Int) {
        viewModelScope.launch {
            repository.deleteEducation(id)
        }
    }

    // Vacancy actions
    fun publishVacancy(title: String, contractType: String, workMode: String, location: String, salaryMin: String, salaryMax: String, requirements: String, deadline: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val newVacancy = Vacancy(
                companyId = user.id,
                companyName = user.companySocialReason ?: user.name,
                title = title,
                contractType = contractType,
                workMode = workMode,
                location = location,
                salaryMin = if (salaryMin.isNotEmpty()) salaryMin else null,
                salaryMax = if (salaryMax.isNotEmpty()) salaryMax else null,
                requirements = requirements,
                deadline = deadline
            )
            repository.insertVacancy(newVacancy)
            onSuccess()
        }
    }

    // Apply for job
    fun applyForJob(vacancyId: Int) {
        viewModelScope.launch {
            val candidate = _currentUser.value ?: return@launch
            if (!candidate.isCompany) {
                repository.applyForVacancy(vacancyId = vacancyId, candidateId = candidate.id)
                // Track candidate metrics optionally
            }
        }
    }

    // Withdraw registration
    fun withdrawFromJob(vacancyId: Int) {
        viewModelScope.launch {
            val candidate = _currentUser.value ?: return@launch
            repository.withdrawApplication(vacancyId = vacancyId, candidateId = candidate.id)
        }
    }

    // Recruiter view candidates count details
    fun getAppliedCandidates(vacancyId: Int): Flow<List<User>> {
        return repository.getAppliedCandidatesForVacancy(vacancyId)
    }

    // View incrementer
    fun incrementVacancyViews(vacancyId: Int) {
        viewModelScope.launch {
            repository.incrementVacancyViews(vacancyId)
        }
    }
}
