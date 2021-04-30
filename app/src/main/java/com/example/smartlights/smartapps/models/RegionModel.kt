package com.example.smartlights.smartapps.models

/**
 * Created by schnell on 22,April,2021
 */

class RegionModel(course_name: String) {
    private var course_name: String

    fun getCourse_name(): String {
        return course_name
    }

    fun setCourse_name(course_name: String) {
        this.course_name = course_name
    }

    init {
        this.course_name = course_name
    }
}