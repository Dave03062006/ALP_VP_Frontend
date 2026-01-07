package com.example.alp_vp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.alp_vp.VPApplication
import com.example.alp_vp.data.dto.Profile
import com.example.alp_vp.data.dto.UpdateProfileRequest
import com.example.alp_vp.data.dto.profile.Achievement
import com.example.alp_vp.data.dto.profile.ProfileStatistics
import com.example.alp_vp.data.dto.profile.ProfileTheme
import com.example.alp_vp.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val profile: Profile? = null,
    val statistics: ProfileStatistics? = null,
    val achievements: List<Achievement> = emptyList(),
    val themes: List<ProfileTheme> = emptyList(),
    val selectedTheme: ProfileTheme? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEditMode: Boolean = false,
    val editedDisplayName: String = ""
)

class ProfileViewModel(
    application: Application,
    private val profileRepository: ProfileRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val currentProfileId = 1 // Hardcoded for now, should come from auth

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // Load all profile data in parallel
                val profile = profileRepository.getProfile(currentProfileId)
                val statistics = profileRepository.getStatistics(currentProfileId)
                val achievements = profileRepository.getAchievements(currentProfileId)
                val themes = profileRepository.getThemes(currentProfileId)

                // Set default theme or first unlocked theme
                val selectedTheme = themes.firstOrNull { it.isUnlocked } ?: themes.firstOrNull()

                _uiState.update {
                    it.copy(
                        profile = profile,
                        statistics = statistics,
                        achievements = achievements,
                        themes = themes,
                        selectedTheme = selectedTheme,
                        isLoading = false,
                        editedDisplayName = profile.displayName ?: profile.username
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load profile: ${e.message}"
                    )
                }
            }
        }
    }

    fun toggleEditMode() {
        val currentState = _uiState.value
        _uiState.update {
            it.copy(
                isEditMode = !currentState.isEditMode,
                editedDisplayName = currentState.profile?.displayName ?: currentState.profile?.username ?: ""
            )
        }
    }

    fun updateDisplayName(newName: String) {
        _uiState.update { it.copy(editedDisplayName = newName) }
    }

    fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val currentState = _uiState.value
                val updatedProfile = profileRepository.updateProfile(
                    currentProfileId.toString(),
                    UpdateProfileRequest(displayName = currentState.editedDisplayName)
                )

                _uiState.update {
                    it.copy(
                        profile = updatedProfile,
                        isLoading = false,
                        isEditMode = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to update profile: ${e.message}"
                    )
                }
            }
        }
    }

    fun selectTheme(theme: ProfileTheme) {
        if (theme.isUnlocked) {
            _uiState.update { it.copy(selectedTheme = theme) }
        }
    }

    fun logout() {
        // Clear any stored auth data
        // This should be handled by auth manager
        _uiState.update { ProfileUiState() }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as VPApplication)
                ProfileViewModel(
                    application = application,
                    profileRepository = application.container.profileRepository
                )
            }
        }
    }
}

