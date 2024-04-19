package com.thanhvq.mapdrawline

import com.google.gson.annotations.SerializedName


data class DirectionsResponse(
    @SerializedName("routes")
    val routes: List<Route>? = null
)

data class Route(
    @SerializedName("overview_polyline")
    val overviewPolyline: OverviewPolyline? = null
)

data class OverviewPolyline(
    @SerializedName("points")
    val points: String? = null
)
