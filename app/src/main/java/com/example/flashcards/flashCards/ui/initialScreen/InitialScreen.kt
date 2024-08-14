package com.example.flashcards.flashCards.ui.initialScreen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.flashcards.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun InitialScreen(viewModel: InitialScreenViewModel,
                  navHostController: NavHostController) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xffD6D6D6))
    ) {



        val (buttons, logo, title) = createRefs()

        val guidelineBottom = createGuidelineFromBottom(0.10f)
        val guidelineTop = createGuidelineFromTop(0.10f)

        val context = LocalContext.current

        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    val account = task.getResult(ApiException::class.java)
                    viewModel.loginWithGoogleAccount(account.idToken!!) { success ->
                        if (success) {
                            navHostController.navigate("FlashCardsScreen")
                        } else {
                            Toast.makeText(context, "Error on Sign In with Google", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Login Google error", "error: $e")
                }
            }

        val googleSignInIntent = fun(): Intent {
            val options = GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            ).requestIdToken("36412686936-0oi15vrn58p3h8220f7qs6j0rlm4co69.apps.googleusercontent.com")
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, options)
            return googleSignInClient.signInIntent
        }

        Text(text = "Flash Cards App", fontWeight = FontWeight.Bold, fontSize = 48.sp,
            modifier = Modifier.constrainAs(title){
                top.linkTo(guidelineTop)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
            })

        Image(painter = painterResource(id = R.drawable.flashcards_logo), contentDescription = "App logo",
            modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(buttons.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }
                .size(270.dp))
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .constrainAs(buttons) {
                    bottom.linkTo(guidelineBottom)
                }
        ) {
            AuthButton("Login") {
                launcher.launch(googleSignInIntent())
            }

        }
    }
}

@Composable
fun AuthButton(text: String, func:() -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff052A62)),
        onClick = {
            func()
        }) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(4f)
            )

            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier
                    .weight(1f)
                    .size(45.dp),
            )
        }
    }
}