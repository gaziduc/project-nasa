package com.gazi.projectnasa

data class RoversObject(val rovers : List<RoverObject>)

data class RoverObject(val name : String, val max_sol : Int, val cameras : List<CameraObject>)

data class CameraObject(val name : String)


data class PhotosObject(val photos: List<PhotoObject>)

data class PhotoObject(val camera : CameraObject, val img_src : String, val earth_date : String)