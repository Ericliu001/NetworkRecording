package com.example.recorder

import okhttp3.ResponseBody
import com.example.recorder.data.RecordedRequest
import com.example.recorder.data.RecordedResponse
import java.io.*
import java.nio.file.Files

class DiskRepo(private val root: File) {
    fun read(request: RecordedRequest, matchRule: MatchRule): List<RecordedResponse> {
        TODO("Not yet implemented")
    }


    fun write(request: RecordedRequest, responses: List<RecordedResponse>) {

    }

    fun write(request: RecordedRequest, response: RecordedResponse) {
        val requestEndpoint = request.url.encodedPath()
        try {
            val pathname = getRecordedResponsePath(requestEndpoint)
            if (File(pathname).exists()) {
                val responseFile = File(pathname)
                if (responseFile.exists()) {

                    val fileArray = Files.readAllBytes(responseFile.toPath())

                    val responseBody = ResponseBody.create(null, fileArray)
                }
            } else {
                writeResponses(response, requestEndpoint)
            }
        } catch (e: IOException) {
            println(e)
        }
    }


    private fun getRecordedResponsePath(requestEndpoint: String): String {
        return root.absolutePath + requestEndpoint + "/" + "responseBody"
    }

    private fun writeResponses(response: RecordedResponse, requestEndpoint: String) {
        // TODO: 7/7/21 write to disk
//        val parentDir = constructPathSegments(root, requestEndpoint)
//        val outputFile: File = File(parentDir, "responseBody")
//        if (outputFile.exists()) {
//            outputFile.delete()
//            outputFile.createNewFile()
//        } else {
//            outputFile.createNewFile()
//        }
//
//        var outputStream: FileOutputStream? = null
//        var inputStream: InputStream? = null
//        try {
//            var cursor = 0
//            outputStream = FileOutputStream(outputFile)
//            inputStream = response.body().inputStream()
//            inputStream.let {
//                while (cursor != -1) {
//                    if (inputStream != null) {
//                        cursor = inputStream.read()
//                        outputStream.write(cursor)
//                    }
//                }
//            }
//        } finally {
//            inputStream?.let { inputStream.close() }
//
//            outputStream?.let { outputStream.close() }
//        }
    }


    /**
     * Construct a pathSegment string to be used as the key in the map. This will ensure we're
     * starting with a '/'.
     *
     * @param encodedPath The path segments as given from OkHttp.
     * @return The constructed path segment.
     */
    private fun constructPathSegments(rootPath: File, requestPath: String): File {
        val dir = File(rootPath, requestPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }


}