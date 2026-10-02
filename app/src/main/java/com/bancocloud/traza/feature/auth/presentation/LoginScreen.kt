package com.bancocloud.traza.feature.auth.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.bancocloud.traza.R
import com.bancocloud.traza.core.designsystem.theme.TrazaColors
import com.bancocloud.traza.core.designsystem.theme.TrazaShapes
import com.bancocloud.traza.core.designsystem.theme.TrazaSpacing

@Composable
fun LoginScreen(
    isLoading: Boolean,
    message: AuthMessage?,
    onLogin: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(TrazaSpacing.Space4),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp),
            shape = TrazaShapes.Card,
            color = TrazaColors.Surface,
            border = BorderStroke(1.dp, TrazaColors.Border),
        ) {
            Column(
                modifier = Modifier.padding(TrazaSpacing.Space5),
                verticalArrangement = Arrangement.spacedBy(TrazaSpacing.Space4),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(TrazaSpacing.Space2)) {
                    Text(
                        text = stringResource(R.string.brand_name),
                        color = TrazaColors.BrandStrong,
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Text(
                        text = stringResource(R.string.login_title),
                        color = TrazaColors.TextPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = stringResource(R.string.login_subtitle),
                        color = TrazaColors.TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                message?.let {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { liveRegion = LiveRegionMode.Assertive },
                        color = TrazaColors.ErrorBackground,
                        shape = TrazaShapes.Control,
                        border = BorderStroke(1.dp, TrazaColors.ErrorBorder),
                    ) {
                        Text(
                            text = stringResource(it.resourceId()),
                            modifier = Modifier.padding(TrazaSpacing.Space3),
                            color = TrazaColors.ErrorText,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    singleLine = true,
                    label = { Text(stringResource(R.string.login_email)) },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    colors = trazaTextFieldColors(),
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    singleLine = true,
                    label = { Text(stringResource(R.string.login_password)) },
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                stringResource(
                                    if (passwordVisible) R.string.login_hide_password
                                    else R.string.login_show_password,
                                ),
                            )
                        }
                    },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onDone = {
                            if (!isLoading && email.isNotBlank() && password.isNotBlank()) {
                                focusManager.clearFocus()
                                onLogin(email, password)
                            }
                        },
                    ),
                    colors = trazaTextFieldColors(),
                )

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onLogin(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TrazaColors.BrandPrimary,
                        contentColor = TrazaColors.BrandOnPrimary,
                    ),
                    shape = TrazaShapes.Control,
                ) {
                    if (isLoading) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(TrazaSpacing.Space2),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.height(18.dp),
                                color = TrazaColors.BrandOnPrimary,
                                strokeWidth = 2.dp,
                            )
                            Text(stringResource(R.string.login_loading))
                        }
                    } else {
                        Text(stringResource(R.string.login_action))
                    }
                }
            }
        }
    }
}

@Composable
fun SessionInitializingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = TrazaColors.BrandStrong)
            Spacer(Modifier.height(TrazaSpacing.Space3))
            Text(
                text = stringResource(R.string.session_validating),
                color = TrazaColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun trazaTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = TrazaColors.FocusBorder,
    unfocusedBorderColor = TrazaColors.Border,
    focusedLabelColor = TrazaColors.BrandStrong,
    cursorColor = TrazaColors.BrandStrong,
)

private fun AuthMessage.resourceId(): Int = when (this) {
    AuthMessage.INVALID_CREDENTIALS -> R.string.login_error_credentials
    AuthMessage.NETWORK_UNAVAILABLE -> R.string.login_error_network
    AuthMessage.SERVER_UNAVAILABLE -> R.string.login_error_server
    AuthMessage.SESSION_EXPIRED -> R.string.login_error_expired
    AuthMessage.ACCESS_DENIED -> R.string.login_error_access_denied
    AuthMessage.ADMIN_ONLY -> R.string.login_error_admin_only
    AuthMessage.UNEXPECTED -> R.string.login_error_unexpected
}
