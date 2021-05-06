package com.example.passecure.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class PassecureItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val username: String,
    val password: String,
    val description: String,
): Parcelable
