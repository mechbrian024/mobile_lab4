package com.example.pokai.pokaichao_comp304sec001_lab4.data

import android.location.Location
import com.example.pokai.pokaichao_comp304sec001_lab4.R
import com.google.android.gms.maps.model.LatLng

object DataSource {
    val resturants = listOf(
        R.string.italian,
        R.string.bbq,
        R.string.thai
    )
    val restaurantLocations = mapOf(
        "Italian Restaurant" to LatLng(43.64377682089839, -79.37652467909616),
        "Thai Resturant" to LatLng(43.64806240978735, -79.38867592710139)
    )
}