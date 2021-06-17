package com.gazi.projectnasa

import java.util.*

data class EONETObject (val events : List<EventObject>)

data class EventObject (val title : String, val categories : List<CategoryObject>, val geometry : List<GeometryObject>);

data class CategoryObject (val title : String)

data class GeometryObject (val magnitudeValue : Int?, val magnitudeUnit : String?, val date: String);