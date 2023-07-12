package com.dededev.logistics.utils

import com.dededev.logistics.database.Logistic

fun LogisticCondition(logistic: Logistic): MutableList<String> {
    val stocks = mutableListOf<String>()

    // KONDISI GUDANG PUSAT
    // Tidak tersedia jika stok akhir gudang pusat kosong
    if (logistic.stokAkhirPusat == 0) {
        stocks.add("Tidak Tersedia")
    // Sedikit jika barang di akhir bulan kurang dari 50% stok pada awal bulan
    } else if (logistic.stokAkhirPusat < 0.5*logistic.stokAwalPusat) {
        stocks.add("Sedikit")
    } else {
        stocks.add("Banyak")
    }

    // KONDISI GUDANG DAERAH
    // Tidak tersedia jika stok akhir gudang daerah kosong
    if (logistic.stokAkhirDaerah == 0) {
        stocks.add("Tidak Tersedia")
        // Sedikit jika barang di akhir bulan kurang dari 50% stok pada awal bulan
    } else if (logistic.stokAkhirDaerah < 0.5*logistic.stokAwalDaerah) {
        stocks.add("Sedikit")
    } else {
        stocks.add("Banyak")
    }

    return stocks
}

fun predict(logistic: Logistic): Logistic {
    val stocks = LogisticCondition(logistic)
    var prediction = 0
    // 0 Bukan prioritas kirim; 1 Prioritas Kirim
    if (stocks[0] == "Banyak" && stocks[1] == "Banyak") prediction = 0
    else if (stocks[0] == "Banyak" && stocks[1] == "Banyak") prediction = 0
    else if (stocks[0] == "Banyak" && stocks[1] == "Sedikit") prediction = 1
    else if (stocks[0] == "Banyak" && stocks[1] == "Tidak Tersedia") prediction = 1
    else if (stocks[0] == "Sedikit" && stocks[1] == "Banyak") prediction = 0
    else if (stocks[0] == "Sedikit" && stocks[1] == "Sedikit") prediction = 1
    else if (stocks[0] == "Sedikit" && stocks[1] == "Tidak Tersedia") prediction = 1
    else if (stocks[0] == "Tidak Tersedia" && stocks[1] == "Banyak") prediction = 0
    else if (stocks[0] == "Tidak Tersedia" && stocks[1] == "Sedikit") prediction = 0
    else if (stocks[0] == "Tidak Tersedia" && stocks[1] == "Tidak Tersedia") prediction = 0

    logistic.prioritasKirim = prediction
    return logistic
}