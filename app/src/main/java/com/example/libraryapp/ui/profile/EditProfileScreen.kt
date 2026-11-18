package com.example.libraryapp.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libraryapp.R
import com.example.libraryapp.domain.model.LessonTimeValidation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    viewModel: EditProfileViewModel = viewModel(factory = EditProfileViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsState()
    var showChooser by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    var doneResult by rememberSaveable { mutableStateOf<DoneResult?>(null) }

    fun save() {
        viewModel.onDone(
            notificationsAllowed = NotificationManagerCompat.from(context).areNotificationsEnabled(),
            onResult = { result ->
                if (result == DoneResult.Scheduled) onBack() else doneResult = result
            }
        )
    }

    val notificationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { save() }

    val onDoneClick = {
        val needsRuntimePermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
        if (needsRuntimePermission) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            save()
        }
    }

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

    val timeErrorRes: Int? = if (state.lessonTimeTouched) {
        when (state.lessonTimeValidation) {
            LessonTimeValidation.Empty -> R.string.lesson_time_error_empty
            LessonTimeValidation.InvalidFormat -> R.string.lesson_time_error_format
            is LessonTimeValidation.Valid -> null
        }
    } else null

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
                        onClick = onDoneClick,
                        enabled = state.canSave
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
                style = MaterialTheme.typography.labelMedium
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
            OutlinedTextField(
                value = state.lessonTimeText,
                onValueChange = viewModel::onLessonTimeChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = timeErrorRes != null,
                label = { Text(stringResource(R.string.field_lesson_time)) },
                supportingText = {
                    Text(stringResource(timeErrorRes ?: R.string.lesson_time_hint))
                },
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = stringResource(R.string.cd_pick_time)
                        )
                    }
                }
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

    if (showTimePicker) {
        val current = (state.lessonTimeValidation as? LessonTimeValidation.Valid)?.time
        val timeState = rememberTimePickerState(
            initialHour = current?.hour ?: 9,
            initialMinute = current?.minute ?: 0,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text(stringResource(R.string.time_picker_title)) },
            text = { TimePicker(state = timeState) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onLessonTimePicked(timeState.hour, timeState.minute)
                    showTimePicker = false
                }) { Text(stringResource(R.string.time_picker_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(stringResource(R.string.profile_cancel))
                }
            }
        )
    }

    doneResult?.let { result ->
        val isExactAlarm = result == DoneResult.NeedExactAlarm
        AlertDialog(
            onDismissRequest = { doneResult = null },
            title = {
                Text(
                    stringResource(
                        if (isExactAlarm) R.string.exact_alarm_title else R.string.notifications_denied_title
                    )
                )
            },
            text = {
                Text(
                    stringResource(
                        if (isExactAlarm) R.string.exact_alarm_text else R.string.notifications_denied_text
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val intent = if (isExactAlarm && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Intent(
                            Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                            Uri.parse("package:${context.packageName}")
                        )
                    } else {
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:${context.packageName}")
                        )
                    }
                    doneResult = null
                    context.startActivity(intent)
                }) { Text(stringResource(R.string.open_settings)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    doneResult = null
                    onBack()
                }) { Text(stringResource(R.string.skip_reminder)) }
            }
        )
    }
}