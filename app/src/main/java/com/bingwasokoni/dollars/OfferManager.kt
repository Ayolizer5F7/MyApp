package com.bingwasokoni.dollars

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

data class Offer(
    val id: String,
    val name: String,
    val commission: Double,
    val ussdPattern: String,
    val category: String,
    val bingwaPrice: Double,
    val safPrice: Double,
    var isActive: Boolean = true
)

class OfferManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("offers_prefs_v2", Context.MODE_PRIVATE)

    fun saveOffer(offer: Offer) {
        val json = JSONObject()
        json.put("name", offer.name)
        json.put("commission", offer.commission)
        json.put("ussdPattern", offer.ussdPattern)
        json.put("category", offer.category)
        json.put("bingwaPrice", offer.bingwaPrice)
        json.put("safPrice", offer.safPrice)
        json.put("isActive", offer.isActive)
        prefs.edit().putString(offer.id, json.toString()).apply()
    }

    fun getOfferByPrice(amount: Double): Offer? {
        val all = getAllOffers()
        return all.find { it.bingwaPrice == amount && it.isActive }
    }

    fun getAllOffers(): List<Offer> {
        val list = mutableListOf<Offer>()
        for ((key, value) in prefs.all) {
            if (key == "is_agent_active") continue
            try {
                val json = JSONObject(value.toString())
                list.add(
                    Offer(
                        id = key,
                        name = json.optString("name", "Unnamed Offer"),
                        commission = json.optDouble("commission", 0.0),
                        ussdPattern = json.optString("ussdPattern", ""),
                        category = json.optString("category", "DATA"),
                        bingwaPrice = json.optDouble("bingwaPrice", 0.0),
                        safPrice = json.optDouble("safPrice", 0.0),
                        isActive = json.optBoolean("isActive", true)
                    )
                )
            } catch (e: Exception) {}
        }
        return list
    }

    fun isAgentActive(): Boolean {
        return prefs.getBoolean("is_agent_active", false)
    }

    fun setAgentActive(isActive: Boolean) {
        prefs.edit().putBoolean("is_agent_active", isActive).apply()
    }
}
