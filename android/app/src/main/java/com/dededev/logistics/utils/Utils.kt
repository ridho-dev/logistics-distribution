package com.dededev.logistics.utils

import com.dededev.logistics.database.Logistic

fun LogisticCondition(logistic: Logistic): Logistic {
    // KONDISI GUDANG
    // Tidak tersedia jika stok akhir gudang pusat kosong
    if (logistic.stokAkhir == 0) {
        logistic.kondisi  = "Tidak Tersedia"
    // Sedikit jika barang di akhir bulan kurang dari 50% stok pada awal bulan
    } else if (logistic.stokAkhir < 0.5*logistic.stokAwal) {
        logistic.kondisi  =  "Sedikit"
    } else {
        logistic.kondisi  = "Banyak"
    }

    return logistic
}

fun predictPrioritas(logisticPusat: Logistic, logisticDaerah: Logistic): Logistic {
    
    if (logisticPusat.kondisi == "Banyak" && logisticDaerah.kondisi == "Banyak") logisticDaerah.prioritasKirim = 0
    else if (logisticPusat.kondisi == "Banyak" && logisticDaerah.kondisi == "Banyak") logisticDaerah.prioritasKirim = 0
    else if (logisticPusat.kondisi == "Banyak" && logisticDaerah.kondisi == "Sedikit") logisticDaerah.prioritasKirim = 1
    else if (logisticPusat.kondisi == "Banyak" && logisticDaerah.kondisi == "Tidak Tersedia") logisticDaerah.prioritasKirim = 1
    else if (logisticPusat.kondisi == "Sedikit" && logisticDaerah.kondisi == "Banyak") logisticDaerah.prioritasKirim = 0
    else if (logisticPusat.kondisi == "Sedikit" && logisticDaerah.kondisi == "Sedikit") logisticDaerah.prioritasKirim = 1
    else if (logisticPusat.kondisi == "Sedikit" && logisticDaerah.kondisi == "Tidak Tersedia") logisticDaerah.prioritasKirim = 1
    else if (logisticPusat.kondisi == "Tidak Tersedia" && logisticDaerah.kondisi == "Banyak") logisticDaerah.prioritasKirim = 0
    else if (logisticPusat.kondisi == "Tidak Tersedia" && logisticDaerah.kondisi == "Sedikit") logisticDaerah.prioritasKirim = 0
    else if (logisticPusat.kondisi == "Tidak Tersedia" && logisticDaerah.kondisi == "Tidak Tersedia") logisticDaerah.prioritasKirim = 0

    return logisticDaerah
}