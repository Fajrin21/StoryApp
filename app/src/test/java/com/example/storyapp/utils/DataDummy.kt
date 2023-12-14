package com.example.storyapp.utils

import com.example.storyapp.database.ListStoryDetail

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryDetail> {
        val newsList = ArrayList<ListStoryDetail>()
        for (i in 0..5) {
            val stories = ListStoryDetail(
                "Title $i",
                "fajrin",
                "fajrinbisabisabisa",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/academy/dos:233a660fbd20c03c1c429382f0e8a41020220318083850.png",
                "2023-03-11T18:12:22Z",
                null,
                null,
            )
            newsList.add(stories)
        }
        return newsList
    }
}