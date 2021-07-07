package com.example.recorder

import java.io.File

class NetworkRecorder(val recordingInterceptor: RecordingInterceptor) {
    private lateinit var memoryRepo: MemoryRepo
    private lateinit var diskRepo: DiskRepo

    fun startRecording(root: File) {
        diskRepo = DiskRepo(root)
        memoryRepo = MemoryRepo()
        recordingInterceptor.setResponseRepo(memoryRepo)
    }

    fun stopRecording() {
        memoryRepo.getAllRecordings().let { records ->

//            diskRepo.write()
        }
    }
}