package com.dededev.logistics.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LogisticDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(logistic: Logistic)

    @Update
    fun update(logistic: Logistic)

    @Query("SELECT * FROM logistic")
    fun getAllLogistic(): LiveData<List<Logistic>>

    @Query("SELECT * FROM logistic WHERE kategori='Perlengkapan Kepala'")
    fun getPerlengkapanKepala(): LiveData<List<Logistic>>

    @Query("SELECT * FROM logistic WHERE kategori='Tutup Badan'")
    fun getTutupBadan(): LiveData<List<Logistic>>

    @Query("SELECT * FROM logistic WHERE prioritas_kirim = 1")
    fun getProcessedLogistic(): LiveData<List<Logistic>>
}