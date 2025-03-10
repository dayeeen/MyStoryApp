package com.dayeeen.mystoryapp

import com.dayeeen.mystoryapp.data.response.ListStoryItem

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "photo + $i",
                "date $i",
                "name $i",
                "description $i",
                null,
                null,
            )
            items.add(story)
        }
        return items
    }
}