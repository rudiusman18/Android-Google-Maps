package com.rudi_usman.coba.models
import com.google.gson.annotations.SerializedName


data class penerimaBnpt (

    @SerializedName("subject"        ) var subject        : String?                   = null,
    @SerializedName("multi-location" ) var multi_location : ArrayList<Multi_location> = arrayListOf()

)


data class Multi_location (

@SerializedName("id"        ) var id        : Int?    = null,
@SerializedName("title"     ) var title     : String? = null,
@SerializedName("address"   ) var address   : String? = null,
@SerializedName("latitude"  ) var latitude  : String? = null,
@SerializedName("longitude" ) var longitude : String? = null,
@SerializedName("mini-info" ) var mini_info : String? = null,
@SerializedName("url"       ) var url       : String? = null

)
