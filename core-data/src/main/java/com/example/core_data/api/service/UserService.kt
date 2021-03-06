package com.example.core_data.api.service

import com.example.core_data.api.request.guru.RegisterGuruRequest
import com.example.core_data.api.request.siswa.RegisterSiswaRequest
import com.example.core_data.api.response.CommonResponse
import com.example.core_data.api.response.LoginResponse
import com.example.core_data.api.response.guru.GuruResponse
import com.example.core_data.api.response.guru.RegisterGuruResponse
import com.example.core_data.api.response.siswa.RegisterSiswaResponse
import com.example.core_data.api.response.siswa.SiswaResponse
import retrofit2.http.*

interface UserService {

    @FormUrlEncoded
    @POST(Login)
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @PUT(ChangePassword)
    suspend fun changePassword(
        @Path("id_user") idUser: Int,
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String,
    ): CommonResponse

    @GET(GetUserById)
    suspend fun getGuruById(
        @Path("id_user") idUser: Int
    ): GuruResponse

    @GET(GetUserById)
    suspend fun getSiswaById(
        @Path("id_user") idUser: Int
    ): SiswaResponse

    @POST(Register)
    suspend fun registerGuru(
        @Body body: RegisterGuruRequest
    ): RegisterGuruResponse

    @POST(Register)
    suspend fun registerSiswa(
        @Body body: RegisterSiswaRequest
    ): RegisterSiswaResponse

    companion object {
        //region API Path
        const val Login = "login"
        const val Register = "register"
        const val ChangePassword = "change-password/{id_user}"
        const val GetUserById = "user-detail/{id_user}"
        //endregion
    }


}