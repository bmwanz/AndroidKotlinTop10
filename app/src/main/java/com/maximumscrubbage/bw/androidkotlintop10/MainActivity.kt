package com.maximumscrubbage.bw.androidkotlintop10

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    // by lazy: initialize when it's first used, xmlListView not available until setContentView
    private val downloadData by lazy { DownloadData(this, xmlListView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.d("MainActivity - onCreate called")

//        val downloadData = DownloadData(this, xmlListView)
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")

        Timber.d("MainActivity - onCreate done")
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData.cancel(true)
    }

    // Kotlin's version of static
    companion object {
        private class DownloadData(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {

            // leaks context
//            var propContext : Context = context

            var propContext : Context by Delegates.notNull()
            var propListView : ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listView
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)

                val parseApplications = ParseApplications()
                parseApplications.parse(result)

//                val arrayAdapter = ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplications.applications)
//                propListView.adapter = arrayAdapter

                val feedAdapter = FeedAdapter(propContext, R.layout.list_record, parseApplications.applications)
                propListView.adapter = feedAdapter
            }

            override fun doInBackground(vararg url: String?): String {
                Timber.d("DownloadData - doInBackground: starts with ${url[0]}")

                val rssFeed = downloadXML(url[0])

                if (rssFeed.isEmpty()) {
                    Timber.e("DownloadData - doInBackground: error downloading")
                }

                return rssFeed
            }

            private fun downloadXML(urlPath: String?): String {

                return URL(urlPath).readText()

//                val xmlResult = StringBuilder()
//
//                try {
//                    val url = URL(urlPath)
//                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//                    val response = connection.responseCode
//                    Log.d(TAG, "downloadXML: response code was $response")
//
////            val inputStream = connection.inputStream
////            val inputStreamReader = InputStreamReader(inputStream)
////            val reader = BufferedReader(inputStreamReader)
//
////                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
////                    val inputBuffer = CharArray(500)
////                    var charsRead = 0
////
////                    // if charsRead < 0, input is over and terminates
////                    while (charsRead >= 0) {
////                        charsRead = reader.read(inputBuffer)
////                        if (charsRead > 0) {
////                            xmlResult.append(String(inputBuffer, 0, charsRead))
////                        }
////                    }
////                    reader.close()
//
//
////                    val stream = connection.inputStream
//                    connection.inputStream.buffered().reader().use { xmlResult.append(it.readText()) }
//
//                    Log.d(TAG, "Received ${xmlResult.length} bytes")
//                    return xmlResult.toString()
//
////                } catch (e: MalformedURLException) {
////                    Log.e(TAG, "downloadXML: Invalid URL ${e.message}")
////                } catch (e: IOException) {
////                    Log.e(TAG, "downloadXML: IO exception reading data: ${e.message}")
////                } catch (e: SecurityException) {
////                    Log.e(TAG, "downloadXML: security exception, needs permission? ${e.message}")
////                } catch (e: Exception) {
////                    Log.e(TAG, "Unknown error: ${e.message}")
////                }
//
//                } catch (e: Exception) {
//                    val errorMessage: String = when (e) {
//                        is MalformedURLException -> "downloadXML: Invalid URL ${e.message}"
//                        is IOException -> "downloadXML: IO Exception reading data: ${e.message}"
//                        is SecurityException -> {
//                            e.printStackTrace()
//                            "downloadXML: Secureity Exception. Needs Permission? ${e.message}"
//                        }
//                        else -> "Unknown error: ${e.message}"
//                    }
//                }
//
//                return "" // there was a problem, return empty string
            }
        }
    }
}

class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageURL: String = ""

    override fun toString(): String {
        return """
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            imageURL = $imageURL
        """.trimIndent()
        // remove indentations from newlines
    }
}
