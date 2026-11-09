package com.example.libraryapp.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libraryapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    viewModel: EditProfileViewModel = viewModel(factory = EditProfileViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsState()
    var showChooser by rememberSaveable { mutableStateOf(false) }

    val pickPhoto = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) viewModel.onPhotoPicked(uri.toString())
    }

    val takePhoto = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        viewModel.onCameraResult(success)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_profile_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.onDone(onSaved = onBack) },
                        enabled = state.isLoaded
                    ) { Text(stringResource(R.string.profile_done)) }
                }
            )
        }
    ) { innerPadding ->
        if (!state.isLoaded) return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Avatar(
                photoUri = state.photoUri,
                modifier = Modifier.clickable { showChooser = true }
            )

            Text(
                text = stringResource(R.string.edit_profile_photo_hint),
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium
            )

            OutlinedTextField(
                value = state.fullName,
                onValueChange = viewModel::onFullNameChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(stringResource(R.string.field_full_name)) }
            )
            OutlinedTextField(
                value = state.resumeUrl,
                onValueChange = viewModel::onResumeUrlChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                label = { Text(stringResource(R.string.field_resume_url)) }
            )
        }
    }

    if (showChooser) {
        AlertDialog(
            onDismissRequest = { showChooser = false },
            title = { Text(stringResource(R.string.photo_chooser_title)) },
            text = {
                Column {
                    TextButton(onClick = {
                        showChooser = false
                        pickPhoto.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) { Text(stringResource(R.string.photo_gallery)) }
                    TextButton(onClick = {
                        showChooser = false
                        takePhoto.launch(Uri.parse(viewModel.prepareCameraTarget()))
                    }) { Text(stringResource(R.string.photo_camera)) }
                }
            },
            confirmButton = {
                TextButton(onClick = { showChooser = false }) {
                    Text(stringResource(R.string.profile_cancel))
                }
            }
        )
    }
}