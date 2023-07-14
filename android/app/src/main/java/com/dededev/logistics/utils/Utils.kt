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

//fun predict(logistic: Logistic): Logistic {
//    val stocks = LogisticCondition(logistic)
//    var prediction = 0
//    // 0 Bukan prioritas kirim; 1 Prioritas Kirim
//    if (stocks[0] == "Banyak" && stocks[1] == "Banyak") prediction = 0
//    else if (stocks[0] == "Banyak" && stocks[1] == "Banyak") prediction = 0
//    else if (stocks[0] == "Banyak" && stocks[1] == "Sedikit") prediction = 1
//    else if (stocks[0] == "Banyak" && stocks[1] == "Tidak Tersedia") prediction = 1
//    else if (stocks[0] == "Sedikit" && stocks[1] == "Banyak") prediction = 0
//    else if (stocks[0] == "Sedikit" && stocks[1] == "Sedikit") prediction = 1
//    else if (stocks[0] == "Sedikit" && stocks[1] == "Tidak Tersedia") prediction = 1
//    else if (stocks[0] == "Tidak Tersedia" && stocks[1] == "Banyak") prediction = 0
//    else if (stocks[0] == "Tidak Tersedia" && stocks[1] == "Sedikit") prediction = 0
//    else if (stocks[0] == "Tidak Tersedia" && stocks[1] == "Tidak Tersedia") prediction = 0
//
//    logistic.prioritasKirim = prediction
//    return logistic
//}