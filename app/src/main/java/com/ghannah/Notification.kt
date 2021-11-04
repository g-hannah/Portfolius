package com.ghannah

import android.content.Context
import android.widget.Toast

/**
 * Singleton class for sending
 * toast notifications.
 */
object Notification
{
    fun send(ctx : Context, message : String)
    {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }

    fun error(ctx : Context, message : String?)
    {
        val msg : String = message ?: "Unknown error"

        Toast.makeText(ctx, "Error: $msg", Toast.LENGTH_SHORT).show()
    }
}