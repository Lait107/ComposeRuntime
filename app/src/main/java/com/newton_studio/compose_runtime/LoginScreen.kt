package com.newton_studio.compose_runtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.newton_studio.compose_runtime.domain.AuthInteractor
import com.newton_studio.compose_runtime.domain.model.AuthEvent
import com.newton_studio.compose_runtime.domain.model.AuthState
import com.newton_studio.compose_runtime.ui.theme.ComposeRuntimeTheme
import javax.inject.Inject

class LoginScreen : ComponentActivity() {

    @Inject
    lateinit var authInteractor: AuthInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
        enableEdgeToEdge()
        setContent {
            ComposeRuntimeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val state by authInteractor.state.collectAsState()
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Content(
                            interactor = authInteractor,
                            state = state,
                            modifier = Modifier.padding(innerPadding),
                        )
                    }
                }
            }
        }
    }

    private fun injectDependency() {
        (application as App).appComponent.inject(this)
    }
}

@Composable
private fun Content(
    interactor: AuthInteractor,
    state: AuthState,
    modifier: Modifier,
) {
    when (state) {
        is AuthState.Loading ->
            CircularProgressIndicator(
                modifier = modifier.padding(bottom = 16.dp)
            )

        is AuthState.LoggedIn -> Greeting(
            message = stringResource(R.string.hello_message,state.user.login),
            modifier,
        )

        is AuthState.LoggedOut -> Greeting(
            message = stringResource(R.string.sing_in_message),
            modifier,
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    when (state) {
        is AuthState.Loading -> Unit
        is AuthState.LoggedIn ->
            Button(
                onClick = {
                    interactor.send(AuthEvent.Logout)
                }
            ) {
                Text(text = stringResource(R.string.button_logout))
            }
        is AuthState.LoggedOut ->
            Button(
                onClick = {
                    interactor.send(AuthEvent.Login("User", ""))
                }
            ) {
                Text(text = stringResource(R.string.button_login))
            }
    }
}

@Composable
private fun Greeting(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeRuntimeTheme {
        Greeting("Android")
    }
}