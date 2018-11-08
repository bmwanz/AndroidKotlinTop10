package com.maximumscrubbage.bw.androidkotlintop10

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import timber.log.Timber
import java.lang.Exception

class ParseApplications {
    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean {
        Timber.d("parse called with $xmlData")
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currectRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {

            }

        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return status
    }
}