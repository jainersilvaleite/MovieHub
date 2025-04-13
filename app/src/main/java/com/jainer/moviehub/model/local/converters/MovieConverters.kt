package com.jainer.moviehub.model.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// CLASSE PARA CONVERSÃO DE DADOS PARA ADEQUAÇÃO AOS TIPOS PERMITIDOS PELO BANCO DE DADOS LOCAL
class MovieConverters {
    /* LISTA PARA STRING E STRING PARA LISTA
    * (!) O banco de dados local não aceita listas, então contorna-se
    * esse problema convertendo-a para string e vice-versa*/
    @TypeConverter
    fun listToString(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun stringToList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}