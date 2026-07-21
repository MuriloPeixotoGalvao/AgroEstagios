package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.Vacancy
import com.example.ui.*
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: AppViewModel) {
    val routeStack by viewModel.routeStack.collectAsState()
    val activeScreen = routeStack.lastOrNull() ?: Screen.Login
    val currentUser by viewModel.currentUser.collectAsState()
    val isCompany = currentUser?.isCompany == true

    var selectedVacancyForDetail by remember { mutableStateOf<Vacancy?>(null) }
    var showVacancyDetailDialog by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (activeScreen) {
                 is Screen.Login -> {
                    LoginScreen(
                        viewModel = viewModel,
                        onNavigateToRegister = { viewModel.navigateTo(Screen.Register) },
                        onLoginSuccess = { user ->
                            if (user.isCompany) {
                                viewModel.navigateAndClear(Screen.CompanyDashboard)
                            } else {
                                viewModel.navigateAndClear(Screen.SearchVacancy)
                            }
                        }
                    )
                }

                is Screen.Register -> {
                    RegisterScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateBack() },
                        onSuccessCandidate = {
                            viewModel.navigateAndClear(Screen.CompleteProfile)
                        },
                        onSuccessCompany = {
                            viewModel.navigateAndClear(Screen.CompanyDashboard)
                        }
                    )
                }

                is Screen.CompleteProfile -> {
                    CompleteProfileScreen(
                        viewModel = viewModel,
                        onFinish = {
                            viewModel.navigateAndClear(Screen.ProfileView)
                        }
                    )
                }

                is Screen.ProfileView -> {
                    ProfileViewScreen(
                        viewModel = viewModel,
                        onEditProfileClick = {
                            viewModel.navigateTo(Screen.CompleteProfile)
                        }
                    )
                }

                is Screen.SearchVacancy -> {
                    SearchVacancyScreen(
                        viewModel = viewModel,
                        onVacancyDetailClick = { vacancy ->
                            viewModel.incrementVacancyViews(vacancy.id)
                            selectedVacancyForDetail = vacancy
                            showVacancyDetailDialog = true
                        }
                    )
                }

                is Screen.Favorites -> {
                    FavoritesScreen(
                        viewModel = viewModel,
                        onVacancyDetailClick = { vacancy ->
                            viewModel.incrementVacancyViews(vacancy.id)
                            selectedVacancyForDetail = vacancy
                            showVacancyDetailDialog = true
                        }
                    )
                }

                is Screen.Applications -> {
                    ApplicationsScreen(
                        viewModel = viewModel,
                        onVacancyDetailClick = { vacancy ->
                            viewModel.incrementVacancyViews(vacancy.id)
                            selectedVacancyForDetail = vacancy
                            showVacancyDetailDialog = true
                        }
                    )
                }

                is Screen.PublishVacancy -> {
                    PublishVacancyScreen(
                        viewModel = viewModel,
                        onSuccess = {
                            viewModel.navigateAndClear(Screen.CompanyDashboard)
                        },
                        onBack = {
                            viewModel.navigateBack()
                        }
                    )
                }

                is Screen.CompanyDashboard -> {
                    CompanyDashboardScreen(
                        viewModel = viewModel,
                        onPublishVacancyClick = {
                            viewModel.navigateTo(Screen.PublishVacancy)
                        }
                    )
                }
            }
        }
    }

    // Common Vacancy Detail Dialog popup (completely functional, fully realized)
    if (showVacancyDetailDialog && selectedVacancyForDetail != null) {
        val vacancy = selectedVacancyForDetail!!
        val hasApplied = viewModel.candidateApplications.collectAsState().value.any { it.vacancyId == vacancy.id }

        Dialog(onDismissRequest = { showVacancyDetailDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Detalhes da Vaga",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = VagaGreen
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val isFav = viewModel.favoriteVacancyIds.collectAsState().value.contains(vacancy.id)
                            IconButton(onClick = { viewModel.toggleFavorite(vacancy.id) }) {
                                Icon(
                                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favoritar",
                                    tint = if (isFav) AccentRose else OutlineColor
                                )
                            }
                            IconButton(onClick = { showVacancyDetailDialog = false }) {
                                Text("Fechar", fontWeight = FontWeight.Bold, color = VagaGreen, fontSize = 12.sp)
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(vacancy.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                        Text("${vacancy.companyName} • ${vacancy.location}", fontSize = 14.sp, color = OnSurfaceVariantText)
                        Text("Tipo de Contrato: ${vacancy.contractType} • Modalidade: ${vacancy.workMode}", fontSize = 12.sp, color = VagaGreen, fontWeight = FontWeight.SemiBold)
                    }

                    if (vacancy.salaryMin != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = VagaGreen.copy(0.08f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = VagaGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Faixa Salarial Sugerida: R$ ${vacancy.salaryMin} - R$ ${vacancy.salaryMax}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VagaGreen
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Atribuições e Requisitos:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(vacancy.requirements, fontSize = 13.sp, color = OnSurfaceVariantText)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Prazo de Inscrição:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(vacancy.deadline, fontSize = 13.sp, color = OnSurfaceVariantText)
                    }

                    // Candidacy Action Button (interconnected with viewmodel!)
                    if (!isCompany) {
                        Button(
                            onClick = {
                                if (hasApplied) {
                                    viewModel.withdrawFromJob(vacancy.id)
                                } else {
                                    viewModel.applyForJob(vacancy.id)
                                }
                                showVacancyDetailDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (hasApplied) Color(0xFFBA1A1A) else VagaGreen
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = if (hasApplied) "Cancelar Inscrição" else "Candidatar-se nesta Vaga",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}


