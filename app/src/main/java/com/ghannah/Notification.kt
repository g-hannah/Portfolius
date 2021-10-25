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
        Toast.makeText(ctx, message, Toast.LENGTH_LONG)
    }
}