package com.example.core_data.domain

data class Siswa(
    val idUser: Long = 0L,
    val id: Long? = 0L,
    val username: String = "",
    val level: String = "",
    val lastLogin: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val nisn: String = "",
    var nama: String = "",
    val kelas: String = "",
    val tanggalLahir: String = "",
    val agama: String = "",
    val alamat: String = "",
    val foto: String = "",
    val asalSekolah: String = "",
    val statusAsalSekolah: String = "",
    val namaAyah: String = "",
    val umurAyah: String = "",
    val agamaAyah: String = "",
    val pendidikanTerakhirAyah: String = "",
    val pekerjaanAyah: String = "",
    val namaIbu: String = "",
    val umurIbu: String = "",
    val agamaIbu: String = "",
    val pendidikanTerakhirIbu: String = "",
    val pekerjaanIbu: String = "",
    val tempatLahir: String = ""
)

typealias ListSiswa = List<Siswa>