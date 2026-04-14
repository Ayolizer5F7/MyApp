package com.bingwasokoni.dollars

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class UssdService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val source = event.source ?: return
        val className = event.className?.toString() ?: ""

        // Catch typical System Dialog windows
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || 
            event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            
            // Read the text on screen
            val textNodes = ArrayList<String>()
            extractText(source, textNodes)
            val dialogText = textNodes.joinToString(" ").lowercase()
            
            Log.d("UssdService", "Dialog Text Seen: $dialogText")

            // Logic to process the states based on User rules
            if (dialogText.contains("already recommended")) {
                Log.d("UssdService", "State: FAILED (Double Purchase Detected)")
                clickCancel(source)
            } else if (dialogText.contains("successful") || dialogText.contains("reward the offer is succeed")) {
                Log.d("UssdService", "State: SUCCESS")
                clickOk(source)
                // TODO: Save to database/tracker
            } else {
                // Unknown state or pending, log it for review
                Log.d("UssdService", "State: PENDING/PROCESSING")
            }
        }
    }

    private fun extractText(node: AccessibilityNodeInfo?, list: ArrayList<String>) {
        if (node == null) return
        if (node.text != null) {
            list.add(node.text.toString())
        }
        for (i in 0 until node.childCount) {
            extractText(node.getChild(i), list)
        }
    }

    private fun clickOk(node: AccessibilityNodeInfo) {
        val okNode = findNodeByText(node, "OK", "SEND", "ACCEPT")
        okNode?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }

    private fun clickCancel(node: AccessibilityNodeInfo) {
        val cancelNode = findNodeByText(node, "CANCEL", "DISMISS")
        cancelNode?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
    }

    private fun findNodeByText(node: AccessibilityNodeInfo, vararg searchTexts: String): AccessibilityNodeInfo? {
        for (text in searchTexts) {
            val list = node.findAccessibilityNodeInfosByText(text)
            if (list.isNotEmpty()) {
                return list[0]
            }
        }
        return null
    }

    override fun onInterrupt() {
        Log.d("UssdService", "Service Interrupted")
    }
}
