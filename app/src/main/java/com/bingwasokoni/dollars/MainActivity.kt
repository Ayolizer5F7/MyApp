package com.bingwasokoni.dollars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bingwasokoni.dollars.ui.theme.*

class MainActivity : ComponentActivity() {
    private lateinit var offerManager: OfferManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        offerManager = OfferManager(this)

        setContent {
            DollarsTheme {
                MainScreen(offerManager)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(offerManager: OfferManager) {
    var isAgentActive by remember { mutableStateOf(offerManager.isAgentActive()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newState = !isAgentActive
                    offerManager.setAgentActive(newState)
                    isAgentActive = newState
                },
                containerColor = if (isAgentActive) DangerRed else NeonGreen,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow, // Replace with appropriate Start/Pause icon
                    contentDescription = "Toggle Agent",
                    tint = DarkBackground
                )
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Daily Subscription", style = MaterialTheme.typography.titleLarge)
                Text("Solo", color = MutedText)
            }
            Text("Expires on: 12 Apr 2026", color = MutedText, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Commission Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("THIS WEEK'S COMMISSION", color = MutedText, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ksh 878", color = NeonGreen, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Text("-84%", color = DangerRed, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Actions Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickActionButton("Quick Sell", NeonGreen)
                QuickActionButton("Transactions", NeonGreen)
                QuickActionButton("Shop", WarningOrange)
                QuickActionButton("My Bingwa", NeonGreen)
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // Edit Offer Button specific requirement (Placeholder for Navigation)
            Button(
                onClick = { /* Navigate to Edit Offer */ },
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage Offers", color = DarkBackground)
            }
        }
    }
}

@Composable
fun QuickActionButton(title: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Icon Placeholder
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, color = LightText, fontSize = 12.sp)
    }
}
