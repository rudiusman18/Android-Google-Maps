package com.rudi_usman.coba.models

import com.google.gson.annotations.SerializedName


data class jaringanListrik (

    @SerializedName("type"     ) var type     : String?             = null,
    @SerializedName("subject"  ) var subject  : String?             = null,
    @SerializedName("features" ) var features : ArrayList<Features> = arrayListOf()

)

data class Geometry (

    @SerializedName("type"        ) var type        : String?                      = null,
    @SerializedName("coordinates" ) var coordinates : ArrayList<ArrayList<Double>> = arrayListOf()

)

data class Properties (

    @SerializedName("LAYER"     ) var LAYER    : String? = null,
    @SerializedName("Sumber"    ) var Sumber   : String? = null,
    @SerializedName("Tahun"     ) var Tahun    : String? = null,
    @SerializedName("Panjang_M" ) var PanjangM : Double? = null,
    @SerializedName("id"        ) var id       : Int?    = null,
    @SerializedName("author"    ) var author   : String? = null

)

data class Features (

    @SerializedName("type"       ) var type       : String?     = null,
    @SerializedName("geometry"   ) var geometry   : Geometry?   = Geometry(),
    @SerializedName("properties" ) var properties : Properties? = Properties()

)