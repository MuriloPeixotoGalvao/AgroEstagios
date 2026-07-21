@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.foundation.layout.ExperimentalLayoutApi::class
)
package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.data.*
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.Screen
import kotlinx.coroutines.launch

// Dynamic redirects of extended icons to standard core Material 3 icons for lightning-fast compilation
val Icons.Filled.Badge: ImageVector get() = Icons.Default.Info
val Icons.Filled.Call: ImageVector get() = Icons.Default.Phone
val Icons.Filled.LockReset: ImageVector get() = Icons.Default.Lock
val Icons.Filled.CameraAlt: ImageVector get() = Icons.Default.Edit
val Icons.Filled.Business: ImageVector get() = Icons.Default.Home
val Icons.Filled.School: ImageVector get() = Icons.Default.Star
val Icons.Filled.UploadFile: ImageVector get() = Icons.Default.Send
val Icons.Filled.Tune: ImageVector get() = Icons.Default.Settings
val Icons.Filled.SearchOff: ImageVector get() = Icons.Default.Search
val Icons.Outlined.Bookmark: ImageVector get() = Icons.Default.FavoriteBorder
val Icons.Filled.Bookmark: ImageVector get() = Icons.Default.Favorite
val Icons.Filled.CorporateFare: ImageVector get() = Icons.Default.Home
val Icons.Filled.TrendingUp: ImageVector get() = Icons.Default.Add
val Icons.Filled.Group: ImageVector get() = Icons.Default.Person
val Icons.Filled.Visibility: ImageVector get() = Icons.Default.Search
val Icons.Filled.Event: ImageVector get() = Icons.Default.Info
val Icons.Filled.Analytics: ImageVector get() = Icons.Default.Info
val Icons.Filled.Description: ImageVector get() = Icons.Default.Info
val Icons.Filled.WorkHistory: ImageVector get() = Icons.Default.Home
val Icons.Filled.Work: ImageVector get() = Icons.Default.Home
val Icons.Filled.Groups: ImageVector get() = Icons.Default.Person
val Icons.Filled.Verified: ImageVector get() = Icons.Default.CheckCircle
val Icons.Filled.Speed: ImageVector get() = Icons.Default.Info

// Common custom visual helpers
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = VagaGreen
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = VagaGreen
                    )
                }
            }
        },
        actions = actions ?: {},
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = SurfaceBright
        ),
        windowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier.shadow(1.dp)
    )
}

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    isCompany: Boolean,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = SurfaceContainerLowest,
        windowInsets = WindowInsets.navigationBars
    ) {
        NavigationBarItem(
            selected = currentScreen is Screen.SearchVacancy || currentScreen is Screen.CompanyDashboard,
            onClick = {
                if (isCompany) onNavigate(Screen.CompanyDashboard) else onNavigate(Screen.SearchVacancy)
            },
            icon = {
                Icon(
                    imageVector = if (isCompany) Icons.Default.CorporateFare else Icons.Default.Home,
                    contentDescription = "Início/Buscar"
                )
            },
            label = { Text("Início", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = VagaGreen,
                indicatorColor = VagaGreen,
                unselectedIconColor = OutlineColor,
                unselectedTextColor = OutlineColor
            )
        )

        NavigationBarItem(
            selected = currentScreen is Screen.Favorites && !isCompany,
            onClick = {
                onNavigate(Screen.Favorites)
            },
            icon = {
                Icon(
                    imageVector = if (currentScreen is Screen.Favorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favoritos"
                )
            },
            label = { Text("Favoritos", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            enabled = !isCompany,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = VagaGreen,
                indicatorColor = VagaGreen,
                unselectedIconColor = OutlineColor,
                unselectedTextColor = OutlineColor
            )
        )

        NavigationBarItem(
            selected = currentScreen is Screen.Applications && !isCompany,
            onClick = {
                onNavigate(Screen.Applications)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.WorkHistory,
                    contentDescription = "Candidaturas"
                )
            },
            label = { Text("Candidaturas", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            enabled = !isCompany,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = VagaGreen,
                indicatorColor = VagaGreen,
                unselectedIconColor = OutlineColor,
                unselectedTextColor = OutlineColor
            )
        )

        NavigationBarItem(
            selected = currentScreen is Screen.ProfileView || currentScreen is Screen.CompleteProfile,
            onClick = {
                onNavigate(Screen.ProfileView)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil"
                )
            },
            label = { Text("Perfil", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = VagaGreen,
                indicatorColor = VagaGreen,
                unselectedIconColor = OutlineColor,
                unselectedTextColor = OutlineColor
            )
        )
    }
}

// 1. LOGIN SCREEN
@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (User) -> Unit
) {
    var email by remember { mutableStateOf("lucas@example.com") }
    var password by remember { mutableStateOf("password") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SurfaceBg, SurfaceContainerLow)))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // App Icon & Name
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(VagaGreenLighter)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = "Estagiagros",
                    tint = VagaGreen,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Estagiagros",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceText
            )

            Text(
                text = "Sua conexão direta com o futuro do agronegócio.",
                fontSize = 14.sp,
                color = OnSurfaceVariantText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Acesse sua conta",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurfaceText,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail ou CPF") },
                        placeholder = { Text("nome@exemplo.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = OutlineColor) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("email_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VagaGreen,
                            focusedLabelColor = VagaGreen
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha") },
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = OutlineColor) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VagaGreen,
                            focusedLabelColor = VagaGreen
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Esqueci minha senha",
                        color = VagaGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = ErrorColor,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.login(
                                email = email,
                                password = password,
                                onSuccess = {
                                    errorMessage = null
                                    viewModel.currentUser.value?.let { onLoginSuccess(it) }
                                },
                                onError = { errorMessage = it }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("login_button")
                    ) {
                        Text("Entrar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(color = OutlineVariantColor)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Não tem uma conta?",
                        color = OnSurfaceVariantText,
                        fontSize = 14.sp
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Cadastre-se",
                        color = VagaGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onNavigateToRegister() }
                            .padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}

// 2. UNIFIED REGISTER SCREEN
@Composable
fun RegisterScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onSuccessCandidate: () -> Unit,
    onSuccessCompany: () -> Unit
) {
    var isCompany by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { AppHeader(title = if (isCompany) "Cadastro de Empresa" else "Cadastro de Candidato", onBackClick = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBg)
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isCompany) "Crie sua conta corporativa" else "Comece sua jornada",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = VagaGreen,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = if (isCompany) "Encontre os melhores talentos para sua empresa de forma ágil e profissional." else "Preencha os dados abaixo para criar seu perfil profissional e encontrar as melhores oportunidades.",
                fontSize = 14.sp,
                color = OnSurfaceVariantText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dual option Toggle Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLow)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (!isCompany) VagaGreen else Color.Transparent)
                        .clickable { isCompany = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sou Candidato",
                        fontWeight = FontWeight.Bold,
                        color = if (!isCompany) Color.White else OnSurfaceVariantText,
                        fontSize = 14.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isCompany) VagaGreen else Color.Transparent)
                        .clickable { isCompany = true }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sou Empresa",
                        fontWeight = FontWeight.Bold,
                        color = if (isCompany) Color.White else OnSurfaceVariantText,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isCompany) {
                CandidateRegisterForm(
                    viewModel = viewModel,
                    onSuccess = onSuccessCandidate
                )
            } else {
                CompanyRegisterForm(
                    viewModel = viewModel,
                    onSuccess = onSuccessCompany
                )
            }
        }
    }
}

@Composable
fun CandidateRegisterForm(
    viewModel: AppViewModel,
    onSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var termsChecked by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome Completo") },
                placeholder = { Text("Ex: João Silva") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = OutlineColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                placeholder = { Text("seu@email.com") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = OutlineColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            OutlinedTextField(
                value = cpf,
                onValueChange = { cpf = it },
                label = { Text("CPF") },
                placeholder = { Text("000.000.000-00") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = OutlineColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Telefone") },
                placeholder = { Text("(11) 99999-9999") },
                leadingIcon = { Icon(Icons.Default.Call, contentDescription = null, tint = OutlineColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                placeholder = { Text("••••••••") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = OutlineColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Senha") },
                placeholder = { Text("••••••••") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.LockReset, contentDescription = null, tint = OutlineColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = termsChecked,
                    onCheckedChange = { termsChecked = it },
                    colors = CheckboxDefaults.colors(checkedColor = VagaGreen)
                )
                Text(
                    text = "Aceito os termos e condições de uso e a política de privacidade.",
                    fontSize = 12.sp,
                    color = OnSurfaceVariantText,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = ErrorColor,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = {
                    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        errorMessage = "Por favor preencha todos os campos obrigatórios."
                        return@Button
                    }
                    if (password != confirmPassword) {
                        errorMessage = "As senhas não coincidem."
                        return@Button
                    }
                    if (!termsChecked) {
                        errorMessage = "Você precisa aceitar os termos de uso."
                        return@Button
                    }
                    val newUser = User(
                        email = email,
                        name = name,
                        cpf = cpf,
                        phone = phone,
                        password = password,
                        isCompany = false
                    )
                    viewModel.registerCandidate(
                        newUser,
                        onSuccess = {
                            errorMessage = null
                            onSuccess()
                        },
                        onError = { errorMessage = it }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Criar minha conta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun CompanyRegisterForm(
    viewModel: AppViewModel,
    onSuccess: () -> Unit
) {
    var brandName by remember { mutableStateOf("") }
    var socialReason by remember { mutableStateOf("") }
    var cnpj by remember { mutableStateOf("") }
    var corporateEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = brandName,
                onValueChange = { brandName = it },
                label = { Text("Nome Fantasia") },
                placeholder = { Text("Como sua empresa é conhecida") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            OutlinedTextField(
                value = socialReason,
                onValueChange = { socialReason = it },
                label = { Text("Razão Social") },
                placeholder = { Text("Nome jurídico completo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            OutlinedTextField(
                value = cnpj,
                onValueChange = { cnpj = it },
                label = { Text("CNPJ") },
                placeholder = { Text("00.000.000/0000-00") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            OutlinedTextField(
                value = corporateEmail,
                onValueChange = { corporateEmail = it },
                label = { Text("E-mail Corporativo") },
                placeholder = { Text("rh@suaempresa.com.br") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = OutlineColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                placeholder = { Text("••••••••") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = OutlineColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Senha") },
                placeholder = { Text("••••••••") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.LockReset, contentDescription = null, tint = OutlineColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = ErrorColor,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = {
                    if (brandName.isEmpty() || corporateEmail.isEmpty() || password.isEmpty()) {
                        errorMessage = "Favor preencher os campos essenciais."
                        return@Button
                    }
                    if (password != confirmPassword) {
                        errorMessage = "As senhas informadas não coincidem."
                        return@Button
                    }
                    val newCompany = User(
                        email = corporateEmail,
                        name = brandName,
                        cpf = "",
                        phone = "",
                        password = password,
                        isCompany = true,
                        companySocialReason = socialReason,
                        companyCnpj = cnpj,
                        companySize = ""
                    )
                    viewModel.registerCompany(
                        newCompany,
                        onSuccess = {
                            errorMessage = null
                            onSuccess()
                        },
                        onError = { errorMessage = it }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Criar Conta Empresa", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Spacer(modifier = Modifier.height(24.dp))
}

// 4. COMPLETAR PERFIL SCREEN
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompleteProfileScreen(
    viewModel: AppViewModel,
    onFinish: () -> Unit
) {
    val user = viewModel.currentUser.collectAsState().value
    var title by remember { mutableStateOf(user?.professionalTitle ?: "") }
    var summary by remember { mutableStateOf(user?.professionalSummary ?: "") }
    var skillInput by remember { mutableStateOf("") }
    var skillsList: List<String> by remember { mutableStateOf(user?.skills?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()) }
    
    val experiences by viewModel.candidateExperiences.collectAsState()
    val educations by viewModel.candidateEducations.collectAsState()

    var showAddExpDialog by remember { mutableStateOf(false) }
    var showAddEduDialog by remember { mutableStateOf(false) }

    // Dialog state variables
    var expTitle by remember { mutableStateOf("") }
    var expCompany by remember { mutableStateOf("") }
    var expPeriod by remember { mutableStateOf("") }
    var expMode by remember { mutableStateOf("Remoto") }

    var eduDegree by remember { mutableStateOf("") }
    var eduInstitution by remember { mutableStateOf("") }
    var eduPeriod by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppHeader(title = "Completar Perfil") },
        bottomBar = { BottomNavBar(Screen.CompleteProfile, false, { viewModel.navigateTo(it) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBg)
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // Photo Section
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = OutlineColor,
                                modifier = Modifier.size(52.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(VagaGreen)
                                .border(1.5.dp, Color.White, CircleShape)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Foto de Perfil", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantText)
                }
            }

            // Professional Info
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Informações Profissionais", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = VagaGreen)

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título Profissional") },
                        placeholder = { Text("Ex: Designer UX/UI Sênior") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
                    )

                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        label = { Text("Resumo Profissional") },
                        placeholder = { Text("Conte um pouco sobre sua trajetória e principais objetivos...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen),
                        maxLines = 5
                    )
                }
            }

            // Skills Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Habilidades", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = VagaGreen)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = skillInput,
                            onValueChange = { skillInput = it },
                            placeholder = { Text("Digite uma habilidade") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
                        )
                        IconButton(
                            onClick = {
                                if (skillInput.isNotEmpty() && !skillsList.contains(skillInput)) {
                                    skillsList = skillsList + skillInput
                                    skillInput = ""
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(VagaGreenLighter)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Adicionar Habilidade", tint = VagaGreen)
                        }
                    }

                    // Flexible Skills Chips Row
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        skillsList.forEach { skill ->
                            InputChip(
                                selected = true,
                                onClick = {},
                                label = { Text(skill, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remover",
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clickable {
                                                skillsList = skillsList - skill
                                            }
                                    )
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    selectedContainerColor = VagaGreenLighter,
                                    selectedLabelColor = VagaGreen
                                )
                            )
                        }
                    }
                }
            }

            // Experiences Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Experiência", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                        TextButton(onClick = { showAddExpDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = VagaGreen)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Adicionar", fontWeight = FontWeight.Bold, color = VagaGreen)
                        }
                    }

                    if (experiences.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Nenhuma experiência adicionada", color = OutlineColor, fontSize = 14.sp)
                        }
                    } else {
                        experiences.forEach { exp ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, OutlineVariantColor, RoundedCornerShape(8.dp))
                                    .background(SurfaceContainerLow)
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SurfaceContainerHigh),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Business, contentDescription = null, tint = OutlineColor)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(exp.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                                    Text(exp.companyName, fontSize = 12.sp, color = OnSurfaceVariantText)
                                    Text("${exp.period} • ${exp.workMode}", fontSize = 11.sp, color = OutlineColor)
                                }
                                IconButton(onClick = { viewModel.deleteExperience(exp.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Apagar", tint = ErrorColor)
                                }
                            }
                        }
                    }
                }
            }

            // Education Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Formação", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                        TextButton(onClick = { showAddEduDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = VagaGreen)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Adicionar", fontWeight = FontWeight.Bold, color = VagaGreen)
                        }
                    }

                    if (educations.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(BorderStroke(1.dp, OutlineColor), RoundedCornerShape(8.dp))
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.School, contentDescription = null, tint = OutlineColor, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Nenhuma formação adicionada", color = OnSurfaceVariantText, fontSize = 13.sp)
                            }
                        }
                    } else {
                        educations.forEach { edu ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, OutlineVariantColor, RoundedCornerShape(8.dp))
                                    .background(SurfaceContainerLow)
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SurfaceContainerHigh),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = OutlineColor)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(edu.degree, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                                    Text(edu.institution, fontSize = 12.sp, color = OnSurfaceVariantText)
                                    Text(edu.period, fontSize = 11.sp, color = OutlineColor)
                                }
                                IconButton(onClick = { viewModel.deleteEducation(edu.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Apagar", tint = ErrorColor)
                                }
                            }
                        }
                    }
                }
            }

            // Giant Save Button
            Button(
                onClick = {
                    viewModel.updateCandidateProfile(
                        title = title,
                        summary = summary,
                        skills = skillsList.joinToString(","),
                        onSuccess = { onFinish() }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Finalizar Perfil", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Add Experience Dialog
        if (showAddExpDialog) {
            Dialog(onDismissRequest = { showAddExpDialog = false }) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Adicionar Experiência", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = VagaGreen)

                        OutlinedTextField(
                            value = expTitle,
                            onValueChange = { expTitle = it },
                            label = { Text("Cargo") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = expCompany,
                            onValueChange = { expCompany = it },
                            label = { Text("Empresa") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = expPeriod,
                            onValueChange = { expPeriod = it },
                            label = { Text("Período (Ex: Jan 2022 - Presente)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Mode selectors
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Remoto", "Híbrido", "Presencial").forEach { mode ->
                                FilterChip(
                                    selected = expMode == mode,
                                    onClick = { expMode = mode },
                                    label = { Text(mode, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = VagaGreen,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showAddExpDialog = false }) {
                                Text("Cancelar", color = OutlineColor)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (expTitle.isNotEmpty() && expCompany.isNotEmpty()) {
                                        viewModel.addExperience(expTitle, expCompany, expPeriod, expMode)
                                        expTitle = ""
                                        expCompany = ""
                                        expPeriod = ""
                                        showAddExpDialog = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = VagaGreen)
                            ) {
                                Text("Adicionar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Add Education Dialog
        if (showAddEduDialog) {
            Dialog(onDismissRequest = { showAddEduDialog = false }) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Adicionar Formação", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = VagaGreen)

                        OutlinedTextField(
                            value = eduDegree,
                            onValueChange = { eduDegree = it },
                            label = { Text("Curso / Grau") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = eduInstitution,
                            onValueChange = { eduInstitution = it },
                            label = { Text("Instituição") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = eduPeriod,
                            onValueChange = { eduPeriod = it },
                            label = { Text("Período (Ex: 2018 - 2021)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showAddEduDialog = false }) {
                                Text("Cancelar", color = OutlineColor)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (eduDegree.isNotEmpty() && eduInstitution.isNotEmpty()) {
                                        viewModel.addEducation(eduDegree, eduInstitution, eduPeriod)
                                        eduDegree = ""
                                        eduInstitution = ""
                                        eduPeriod = ""
                                        showAddEduDialog = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = VagaGreen)
                            ) {
                                Text("Adicionar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

// 5. CANDIDATE PROFILE PREVIEW (PROPER HERO)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileViewScreen(
    viewModel: AppViewModel,
    onEditProfileClick: () -> Unit
) {
    val user = viewModel.currentUser.collectAsState().value
    val experiences by viewModel.candidateExperiences.collectAsState()
    val educations by viewModel.candidateEducations.collectAsState()

    Scaffold(
        topBar = { AppHeader(title = "Meu Perfil") },
        bottomBar = { BottomNavBar(Screen.ProfileView, false, { viewModel.navigateTo(it) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBg)
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // Hero section with background and Edit Button
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
            ) {
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = onEditProfileClick,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(VagaGreen)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar Perfil", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerHigh)
                                .border(3.dp, SurfaceContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = OutlineColor,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = user?.name ?: "Lucas Silva",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText
                        )

                        Text(
                            text = user?.professionalTitle ?: "Desenvolvedor Full Stack...",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurfaceVariantText
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = VagaGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "São Paulo, SP",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VagaGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = user?.professionalSummary ?: "Apaixonado por criar soluções escaláveis...",
                            fontSize = 14.sp,
                            color = OnSurfaceVariantText,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }

            // Skills Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Habilidades", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                    Text("Ver todas", color = VagaGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable {})
                }

                val skillChips = user?.skills?.split(",")?.filter { it.isNotEmpty() } ?: listOf("React", "TypeScript", "Node.js", "Docker", "AWS")
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    skillChips.forEach { chip ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(VagaGreenLighter)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = chip.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = VagaGreen
                            )
                        }
                    }
                }
            }

            // Experience Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Experiência Profissional", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)

                if (experiences.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        border = BorderStroke(1.dp, OutlineVariantColor)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Nenhuma experiência profissional cadastrada", color = OutlineColor, fontSize = 13.sp)
                        }
                    }
                } else {
                    experiences.forEach { exp ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, OutlineVariantColor.copy(0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SurfaceContainerLow),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Business, contentDescription = null, tint = VagaGreen)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(exp.companyName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                                    Text(exp.title, fontSize = 13.sp, color = OnSurfaceVariantText)
                                    Text("${exp.period} • ${exp.workMode}", fontSize = 12.sp, color = OutlineColor)
                                }
                            }
                        }
                    }
                }
            }

            // Resume updating actions
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Atualizar Currículo", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                OutlinedButton(
                    onClick = {},
                    border = BorderStroke(1.dp, OutlineVariantColor),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = VagaGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Exportar Perfil em PDF", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// 6. VACANCY SEARCH VIEW (BUSCAR VAGAS)
@Composable
fun SearchVacancyScreen(
    viewModel: AppViewModel,
    onVacancyDetailClick: (Vacancy) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filterWorkMode by viewModel.filterWorkMode.collectAsState()
    val filteredVacancies by viewModel.filteredVacancies.collectAsState()
    val candidateApplications by viewModel.candidateApplications.collectAsState()
    val favoriteVacancyIds by viewModel.favoriteVacancyIds.collectAsState()

    var showFiltersDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Estagiagros",
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = VagaGreen)
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = OutlineColor)
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(Screen.SearchVacancy, false, { viewModel.navigateTo(it) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBg)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Search & filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = { Text("Buscar por cargo, empresa ou cidade", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = OutlineColor) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VagaGreen,
                        unfocusedBorderColor = OutlineVariantColor,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    )
                )

                IconButton(
                    onClick = { showFiltersDialog = true },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VagaGreen)
                ) {
                    Icon(Icons.Default.Tune, contentDescription = "Filtrar", tint = Color.White)
                }
            }

            // Quick Filters horizontal scrolling chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Todas", "Remoto", "CLT", "Júnior", "Tecnologia").forEach { chip ->
                    val isSelected = filterWorkMode == chip
                    ElevatedFilterChip(
                        selected = isSelected,
                        onClick = { viewModel.filterWorkMode.value = chip },
                        label = { Text(chip, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.elevatedFilterChipColors(
                            selectedContainerColor = VagaGreen,
                            selectedLabelColor = Color.White,
                            containerColor = SurfaceContainerLowest,
                            labelColor = OnSurfaceVariantText
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Job feed list
            if (filteredVacancies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.SearchOff, contentDescription = null, tint = OutlineColor, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Nenhuma vaga encontrada", fontWeight = FontWeight.Bold, color = OnSurfaceText)
                        Text("Tente buscar por termos diferentes ou ajuste os filtros.", color = OnSurfaceVariantText, fontSize = 13.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredVacancies.size) { index ->
                        val vacancy = filteredVacancies[index]
                        val hasApplied = candidateApplications.any { it.vacancyId == vacancy.id }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                            border = BorderStroke(1.dp, SurfaceContainerHigh)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(VagaGreenLighter),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = vacancy.companyName.take(2).uppercase(),
                                                fontWeight = FontWeight.Bold,
                                                color = VagaGreen,
                                                fontSize = 14.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = vacancy.title,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OnSurfaceText,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${vacancy.companyName} • ${vacancy.location}",
                                                fontSize = 13.sp,
                                                color = OnSurfaceVariantText,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    val isFavorite = favoriteVacancyIds.contains(vacancy.id)
                                    IconButton(
                                        onClick = { viewModel.toggleFavorite(vacancy.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = if (isFavorite) "Desfavoritar" else "Favoritar",
                                            tint = if (isFavorite) AccentRose else OutlineColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Metadata Tags row
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.horizontalScroll(rememberScrollState())
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(VagaGreenLighter)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(vacancy.workMode.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(SurfaceContainerLow)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(vacancy.contractType.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantText)
                                    }

                                    if (vacancy.salaryMin != null) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(AccentRose.copy(alpha = 0.15f))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text("R$ ${vacancy.salaryMin.toInt().div(1000)}k - ${vacancy.salaryMax?.toInt()?.div(1000)}k", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentRoseDark)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Prazo: ${vacancy.deadline}",
                                        fontSize = 12.sp,
                                        color = OutlineColor,
                                        fontWeight = FontWeight.Medium
                                    )

                                    TextButton(
                                        onClick = { onVacancyDetailClick(vacancy) }
                                    ) {
                                        Text("Ver detalhes", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Custom Filters dialog
        if (showFiltersDialog) {
            Dialog(onDismissRequest = { showFiltersDialog = false }) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Filtros Avançados", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = VagaGreen)

                        Text("Modalidade de Trabalho", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Todas", "Remoto", "Presencial", "Híbrido").forEach { mode ->
                                FilterChip(
                                    selected = filterWorkMode == mode,
                                    onClick = { viewModel.filterWorkMode.value = mode },
                                    label = { Text(mode) }
                                )
                            }
                        }

                        Button(
                            onClick = { showFiltersDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Aplicar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// 7. PUBLISH VACANCY SCREEN (POST A JOB)
@Composable
fun PublishVacancyScreen(
    viewModel: AppViewModel,
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var contractType by remember { mutableStateOf("Estágio") }
    var workMode by remember { mutableStateOf("Presencial") }
    var location by remember { mutableStateOf("") }
    var salaryMin by remember { mutableStateOf("") }
    var salaryMax by remember { mutableStateOf("") }
    var requirements by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("30/08/2026") }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Estagiagros",
                onBackClick = onBack,
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = VagaGreen)
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = OutlineColor)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBg)
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Publicar Nova Vaga", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                Text("Preencha as informações detalhadas para atrair os melhores talentos.", fontSize = 14.sp, color = OnSurfaceVariantText)
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    
                    // Section 1: Informações Básicas
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Work, contentDescription = null, tint = VagaGreen)
                        Text("Informações Básicas", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título da Vaga") },
                        placeholder = { Text("Ex: Desenvolvedor Frontend Sênior") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Contract Dropdown
                        var isContractExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = contractType,
                                onValueChange = {},
                                label = { Text("Contrato") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { isContractExpanded = !isContractExpanded }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
                            )
                            DropdownMenu(
                                expanded = isContractExpanded,
                                onDismissRequest = { isContractExpanded = false }
                            ) {
                                listOf("Estágio", "CLT", "PJ", "Temporário").forEach { type ->
                                    DropdownMenuItem(text = { Text(type) }, onClick = {
                                        contractType = type
                                        isContractExpanded = false
                                    })
                                }
                            }
                        }

                        // Work Mode Dropdown
                        var isModeExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = workMode,
                                onValueChange = {},
                                label = { Text("Modalidade") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { isModeExpanded = !isModeExpanded }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
                            )
                            DropdownMenu(
                                expanded = isModeExpanded,
                                onDismissRequest = { isModeExpanded = false }
                            ) {
                                listOf("Presencial", "Híbrido", "Remoto").forEach { mode ->
                                    DropdownMenuItem(text = { Text(mode) }, onClick = {
                                        workMode = mode
                                        isModeExpanded = false
                                    })
                                }
                            }
                        }
                    }

                    Divider(color = OutlineVariantColor.copy(0.3f))

                    // Section 2: Localização e Remuneração
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = VagaGreen)
                        Text("Localização e Remuneração", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                    }

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Cidade / Estado") },
                        placeholder = { Text("Ex: São Paulo, SP") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = OutlineColor) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = salaryMin,
                            onValueChange = { salaryMin = it },
                            label = { Text("Mínimo") },
                            leadingIcon = { Text("R$", color = OutlineColor, modifier = Modifier.padding(start = 12.dp)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
                        )
                        Text("até", color = OutlineColor, fontSize = 13.sp)
                        OutlinedTextField(
                            value = salaryMax,
                            onValueChange = { salaryMax = it },
                            label = { Text("Máximo") },
                            leadingIcon = { Text("R$", color = OutlineColor, modifier = Modifier.padding(start = 12.dp)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
                        )
                    }

                    Divider(color = OutlineVariantColor.copy(0.3f))

                    // Section 3: Requisitos e Prazos
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = VagaGreen)
                        Text("Requisitos e Prazos", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                    }

                    OutlinedTextField(
                        value = requirements,
                        onValueChange = { requirements = it },
                        label = { Text("Requisitos e Qualificações") },
                        placeholder = { Text("Descreva as competências técnicas e comportamentais...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
                    )

                    OutlinedTextField(
                        value = deadline,
                        onValueChange = { deadline = it },
                        label = { Text("Prazo de Inscrição") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = VagaGreen, focusedLabelColor = VagaGreen)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (title.isNotEmpty() && location.isNotEmpty()) {
                                viewModel.publishVacancy(
                                    title = title,
                                    contractType = contractType,
                                    workMode = workMode,
                                    location = location,
                                    salaryMin = salaryMin,
                                    salaryMax = salaryMax,
                                    requirements = requirements,
                                    deadline = deadline,
                                    onSuccess = { onSuccess() }
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Publicar Vaga", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    OutlinedButton(
                        onClick = onBack,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, OutlineVariantColor),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = VagaGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Salvar Rascunho", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Recruiter Tip Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = VagaGreenLighter),
                border = BorderStroke(1.dp, VagaGreen.copy(0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = VagaGreen)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Dica do Recrutador", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                        Text("Vagas com faixas salariais transparentes recebem até 40% mais candidaturas qualificadas no Estagiagros.", fontSize = 12.sp, color = OnSurfaceVariantText)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// 8. COMPANY RECRUITER DASHBOARD
@Composable
fun CompanyDashboardScreen(
    viewModel: AppViewModel,
    onPublishVacancyClick: () -> Unit
) {
    val recruiterVacancies by viewModel.recruiterVacancies.collectAsState()
    val scope = rememberCoroutineScope()
    var selectedVacancyForCandidates by remember { mutableStateOf<Vacancy?>(null) }
    var showCandidatesDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Estagiagros",
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = VagaGreen)
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CorporateFare, contentDescription = null, tint = VagaGreen)
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(Screen.CompanyDashboard, true, { viewModel.navigateTo(it) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBg)
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // Header and Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Olá, TechSolutions", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                    Text("Gerencie suas vagas e novos talentos.", fontSize = 13.sp, color = OnSurfaceVariantText)
                }

                Button(
                    onClick = onPublishVacancyClick,
                    colors = ButtonDefaults.buttonColors(containerColor = VagaGreenLight),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.shadow(2.dp, RoundedCornerShape(12.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Nova Vaga", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            // Stats Bento Grid
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("TOTAL VAGAS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OutlineColor)
                            Text("08", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = VagaGreen, modifier = Modifier.size(12.dp))
                                Text("+2 este mês", fontSize = 10.sp, color = VagaGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .border(BorderStroke(1.5.dp, VagaGreen), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("CANDIDATOS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OutlineColor)
                            Text("142", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Group, contentDescription = null, tint = VagaGreen, modifier = Modifier.size(12.dp))
                                Text("12 novos hoje", fontSize = 10.sp, color = VagaGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("VISUALIZAÇÕES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OutlineColor)
                            Text("2.4k", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Visibility, contentDescription = null, tint = OutlineColor, modifier = Modifier.size(12.dp))
                                Text("Média 300/vaga", fontSize = 10.sp, color = OnSurfaceVariantText, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ENTREVISTAS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OutlineColor)
                            Text("15", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Event, contentDescription = null, tint = AccentRoseDark, modifier = Modifier.size(12.dp))
                                Text("4 agendadas", fontSize = 10.sp, color = AccentRoseDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Created Listings List (Active Jobs)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Vagas Ativas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                    TextButton(onClick = {}) {
                        Text("Ver todas", fontWeight = FontWeight.Bold, color = VagaGreen)
                    }
                }

                if (recruiterVacancies.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, OutlineVariantColor),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
                    ) {
                        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("Nenhuma vaga cadastrada ainda. Clique em Nova Vaga!", color = OnSurfaceVariantText, fontSize = 13.sp)
                        }
                    }
                } else {
                    recruiterVacancies.forEach { vacancy ->
                        // Get total candidates registered for this vacant
                        val appliedCountState = viewModel.getRepositoryCountFlow(vacancy.id).collectAsState(initial = 0)

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(VagaGreenLighter),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.CorporateFare, contentDescription = null, tint = VagaGreen)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(vacancy.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                                            Text("${vacancy.workMode} • Publicada recentemente", fontSize = 11.sp, color = OutlineColor)
                                        }
                                    }
                                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = OutlineColor)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val formattedTag = vacancy.requirements.split(" ").take(2).joinToString(" & ")
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(30.dp))
                                            .background(VagaGreenLighter)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(formattedTag.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(30.dp))
                                            .background(SurfaceContainerLow)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(vacancy.contractType.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantText)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Divider(color = SurfaceContainerHigh)
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = VagaGreen, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("${appliedCountState.value} Candidatados", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Column {
                                            Text("VISUALIZAÇÕES", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OutlineColor)
                                            Text("${vacancy.viewsCount}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                                        }

                                        IconButton(
                                            onClick = {
                                                selectedVacancyForCandidates = vacancy
                                                showCandidatesDialog = true
                                            },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(VagaGreen)
                                        ) {
                                            Icon(Icons.Default.Analytics, contentDescription = "Candidatos", tint = Color.White, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Candidates display list Dialog
        if (showCandidatesDialog && selectedVacancyForCandidates != null) {
            val appliedCandidatesState = viewModel.getAppliedCandidates(selectedVacancyForCandidates!!.id).collectAsState(initial = emptyList())
            
            Dialog(onDismissRequest = { showCandidatesDialog = false }) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Candidatos Inscritos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = VagaGreen
                        )
                        Text(
                            text = selectedVacancyForCandidates!!.title,
                            fontSize = 13.sp,
                            color = OnSurfaceVariantText
                        )

                        Divider(color = OutlineVariantColor.copy(0.3f))

                        if (appliedCandidatesState.value.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Nenhum candidato inscrito ainda.", color = OutlineColor, fontSize = 13.sp)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(appliedCandidatesState.value.size) { cardIdx ->
                                    val candidate = appliedCandidatesState.value[cardIdx]
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, OutlineVariantColor.copy(0.5f), RoundedCornerShape(8.dp))
                                            .background(SurfaceContainerLow)
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(SurfaceContainerHigh),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Person, contentDescription = null, tint = OutlineColor)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(candidate.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OnSurfaceText)
                                            Text(candidate.professionalTitle ?: "Candidato Autônomo", fontSize = 11.sp, color = OnSurfaceVariantText)
                                            Text(candidate.email, fontSize = 11.sp, color = OutlineColor)
                                        }
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { showCandidatesDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Fechar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// Global visual helpers for chips coloring
val VagaGreenLighter: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF144719) else Color(0xFFF1F5EB)

// Helper mapping function in ViewModel for count reactive flows
fun AppViewModel.getRepositoryCountFlow(vacancyId: Int) = repository.getApplicationCountForVacancy(vacancyId)

// Favorites Screen
@Composable
fun FavoritesScreen(
    viewModel: AppViewModel,
    onVacancyDetailClick: (Vacancy) -> Unit
) {
    val favoritedVacancies by viewModel.favoritedVacancies.collectAsState()
    val favoriteVacancyIds by viewModel.favoriteVacancyIds.collectAsState()

    Scaffold(
        topBar = {
            AppHeader(
                title = "Vagas Favoritas",
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = VagaGreen)
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = OutlineColor)
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(Screen.Favorites, false, { viewModel.navigateTo(it) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBg)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Suas vagas salvas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceText,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (favoritedVacancies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(VagaGreenLighter),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = VagaGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Nenhuma vaga favoritada ainda",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Toque no ícone de coração em qualquer vaga para salvá-la aqui nos seus favoritos.",
                            fontSize = 13.sp,
                            color = OnSurfaceVariantText,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { viewModel.navigateTo(Screen.SearchVacancy) },
                            colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Explorar Vagas", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoritedVacancies.size) { index ->
                        val vacancy = favoritedVacancies[index]
                        val isFavorite = favoriteVacancyIds.contains(vacancy.id)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                            border = BorderStroke(1.dp, SurfaceContainerHigh)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(VagaGreenLighter),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = vacancy.companyName.take(2).uppercase(),
                                                fontWeight = FontWeight.Bold,
                                                color = VagaGreen,
                                                fontSize = 14.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = vacancy.title,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OnSurfaceText,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${vacancy.companyName} • ${vacancy.location}",
                                                fontSize = 13.sp,
                                                color = OnSurfaceVariantText,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.toggleFavorite(vacancy.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Desfavoritar",
                                            tint = if (isFavorite) AccentRose else OutlineColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.horizontalScroll(rememberScrollState())
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(VagaGreenLighter)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(vacancy.workMode.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(SurfaceContainerLow)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(vacancy.contractType.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantText)
                                    }

                                    if (vacancy.salaryMin != null) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(AccentRose.copy(alpha = 0.15f))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text("R$ ${vacancy.salaryMin.toInt().div(1000)}k - ${vacancy.salaryMax?.toInt()?.div(1000)}k", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentRoseDark)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Prazo: ${vacancy.deadline}",
                                        fontSize = 12.sp,
                                        color = OutlineColor,
                                        fontWeight = FontWeight.Medium
                                    )

                                    TextButton(
                                        onClick = { onVacancyDetailClick(vacancy) }
                                    ) {
                                        Text("Ver detalhes", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VagaGreen)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Applications Screen
@Composable
fun ApplicationsScreen(
    viewModel: AppViewModel,
    onVacancyDetailClick: (Vacancy) -> Unit
) {
    val appliedVacancies by viewModel.appliedVacancies.collectAsState()
    val favoriteVacancyIds by viewModel.favoriteVacancyIds.collectAsState()

    Scaffold(
        topBar = {
            AppHeader(
                title = "Minhas Candidaturas",
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = VagaGreen)
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = OutlineColor)
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(Screen.Applications, false, { viewModel.navigateTo(it) }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBg)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Candidaturas em Andamento (${appliedVacancies.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceText,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (appliedVacancies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(VagaGreenLighter),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkHistory,
                                contentDescription = null,
                                tint = VagaGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Nenhuma candidatura efetuada",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceText,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Candidate-se às vagas do seu interesse para acompanhar o processo seletivo por aqui.",
                            fontSize = 13.sp,
                            color = OnSurfaceVariantText,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { viewModel.navigateTo(Screen.SearchVacancy) },
                            colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Buscar Vagas", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(appliedVacancies.size) { index ->
                        val vacancy = appliedVacancies[index]
                        val isFavorite = favoriteVacancyIds.contains(vacancy.id)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                            border = BorderStroke(1.dp, SurfaceContainerHigh)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(VagaGreenLighter),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = vacancy.companyName.take(2).uppercase(),
                                                fontWeight = FontWeight.Bold,
                                                color = VagaGreen,
                                                fontSize = 14.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = vacancy.title,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OnSurfaceText,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${vacancy.companyName} • ${vacancy.location}",
                                                fontSize = 13.sp,
                                                color = OnSurfaceVariantText,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.toggleFavorite(vacancy.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Favoritar",
                                            tint = if (isFavorite) AccentRose else OutlineColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Status badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(VagaGreenLighter)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = VagaGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Candidatura Enviada",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VagaGreen
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.horizontalScroll(rememberScrollState())
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(SurfaceContainerLow)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(vacancy.workMode.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantText)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(SurfaceContainerLow)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(vacancy.contractType.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariantText)
                                    }

                                    if (vacancy.salaryMin != null) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(AccentRose.copy(alpha = 0.15f))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text("R$ ${vacancy.salaryMin.toInt().div(1000)}k - ${vacancy.salaryMax?.toInt()?.div(1000)}k", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentRoseDark)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(
                                        onClick = { viewModel.withdrawFromJob(vacancy.id) }
                                    ) {
                                        Text("Desistir", fontSize = 12.sp, color = OutlineColor)
                                    }

                                    Button(
                                        onClick = { onVacancyDetailClick(vacancy) },
                                        colors = ButtonDefaults.buttonColors(containerColor = VagaGreen),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Ver Detalhes", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
