package com.bingwasokoni.dollars

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import android.util.Log

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {
                val body = sms.displayMessageBody
                val sender = sms.displayOriginatingAddress

                // Basic logic to parse M-Pesa SMS
                // Example: "OB94VW92A Confirmed. Ksh50.00 sent to ... for ... from 0712345678"
                // This regex is a simple placeholder and must be adapted to exact MPESA structure
                if (sender?.contains("MPESA") == true || body.contains("Confirmed")) {
                    val amountRegex = Regex("Ksh(\\d+)\\.\\d{2}")
                    val phoneRegex = Regex("from\\s(07\\d{8}|01\\d{8}|254\\d{9})")
                    
                    val amountMatch = amountRegex.find(body)
                    val phoneMatch = phoneRegex.find(body)

                    if (amountMatch != null && phoneMatch != null) {
                        val amount = amountMatch.groupValues[1].toInt()
                        var phone = phoneMatch.groupValues[1]

                        Log.d("SmsReceiver", "Matched Payment: $amount from $phone")
                        
                        // Look up offer and check state
                        val offerManager = OfferManager(context)
                        val offer = offerManager.getOfferByPrice(amount.toDouble())
                        
                        if (offer != null) {
                            if (!offerManager.isAgentActive()) {
                                Log.d("SmsReceiver", "Agent is PAUSED. Ignoring matched payment for amount: $amount")
                                return
                            }

                            // Replace {pn} and @ with exact parsed phone number
                            val ussdRaw = offer.ussdPattern
                                .replace("{pn}", phone)
                                .replace("pn", phone)
                                .replace("@", phone)
                            
                            // To run USSD automatically we invoke the ACTION_CALL intent
                            // The UssdService will pick up the dialogs
                            val encodedHash = Uri.encode("#")
                            var dialable = ussdRaw.replace("#", encodedHash)
                            
                            Log.d("SmsReceiver", "Triggering USSD: $ussdRaw")
                            
                            val callIntent = Intent(Intent.ACTION_CALL)
                            callIntent.data = Uri.parse("tel:$dialable")
                            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            
                            try {
                                context.startActivity(callIntent)
                            } catch (e: Exception) {
                                Log.e("SmsReceiver", "Failed to start call", e)
                            }
                        }
                    }
                }
            }
        }
    }
}
