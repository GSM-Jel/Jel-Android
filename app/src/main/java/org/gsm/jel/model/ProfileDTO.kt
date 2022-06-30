package org.gsm.jel.model

import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.LiveData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileDTO(
    val name: String,
    val major: String,
    val language: String,
    val profile: Uri,
    val uid: String
): Parcelable