package com.dededev.logistics.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Logistic (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "nama_barang")
    var namaBarang: String? = null,

    @ColumnInfo(name = "kategori")
    var kategori: String? = null,

    @ColumnInfo(name = "wilayah")
    var wilayah: String? = null,

    @ColumnInfo(name = "stok_awal")
    var stokAwal: Int = 0,

    @ColumnInfo(name = "stok_akhir")
    var stokAkhir: Int = 0,

    @ColumnInfo(name = "kondisi")
    var kondisi: String? = null,

    @ColumnInfo(name = "prioritas_kirim")
    var prioritasKirim: Int = 0,
): Parcelable