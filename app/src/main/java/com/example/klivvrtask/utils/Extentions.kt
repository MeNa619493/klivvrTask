package com.example.klivvrtask.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast


fun Context.navigateToGoogleMaps(longitude: Double, latitude: Double) {
    val uri = "http://maps.google.com/maps?q=loc:${latitude},${longitude}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    intent.setPackage("com.google.android.apps.maps")
    try {
        this.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "No map application found", Toast.LENGTH_SHORT).show()
    }
}