package com.example.recorder

import com.example.recorder.repo.DiskRepo
import com.example.recorder.repo.MemoryRepo
import java.io.File

class NetworkRecorder(val recordingInterceptor: RecordingInterceptor) {
    private lateinit var memoryRepo: MemoryRepo
    private lateinit var diskRepo: DiskRepo

    fun startRecording(root: File) {
        diskRepo = DiskRepo(root)
        memoryRepo = MemoryRepo()
        recordingInterceptor.setResponseRepo(memoryRepo)
    }

    fun saveRecordsToFiles() {
        diskRepo.writeRecords(memoryRepo.getAllRecordings())
    }
}