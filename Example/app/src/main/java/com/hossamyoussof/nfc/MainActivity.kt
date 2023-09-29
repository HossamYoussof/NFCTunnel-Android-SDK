package com.hossamyoussof.nfc

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hossamyoussof.nfc.ui.theme.NFCTheme
import com.rasheedapp.nfctunnel.NFCTunnel
import com.rasheedapp.nfctunnel.NFCTunnelListener

class MainActivity : ComponentActivity(), NFCTunnelListener {

    private var ndefMessage by mutableStateOf("Sample NDEF message")
    private val nfcTunnel: NFCTunnel by lazy {
        NFCTunnel(this).apply {
            listener = this@MainActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NFCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting("NFC reader")
                        DisplayNdefMessage(ndefMessage)
                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (nfcTunnel.isNFCEnabled()) {
            nfcTunnel.startSession()
        }
    }

    override fun onPause() {
        super.onPause()
        nfcTunnel.endSession()
    }

    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        nfcTunnel.onNewIntent(intent)
    }

    override fun onSuccess(json: String) {
        ndefMessage = json
    }

    override fun onFailure(error: Exception?) {
        ndefMessage = error?.message ?: "Unknown error"
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun DisplayNdefMessage(ndefMessage: String?) {
    Text(
        text = ndefMessage ?: "No NDEF message found",
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NFCTheme {
        Greeting("Android")
    }
}