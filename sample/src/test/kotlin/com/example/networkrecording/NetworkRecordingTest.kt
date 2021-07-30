package com.example.networkrecording

import com.example.recorder.NetworkRecorder
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
//@Config(shadows = [ShadowShadowEnvironment::class])
class NetworkRecordingTest {

    @Rule
    @JvmField
    val testMethodName: TestName = TestName()

    private lateinit var mainScope: MainScope
    private lateinit var networkRecorder: NetworkRecorder


    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        mainScope = MainScopeImpl()
        networkRecorder = mainScope.networkRecorder()

        val testName = testMethodName.methodName
        RuntimeEnvironment.setTempDirectory(NotTempDirectory(testName))
        networkRecorder.startRecording(File("src/test/assets", testName))
    }

    @After
    fun tearDown() {
        networkRecorder.saveRecordsToFiles()
    }

    @Test
    fun testActivityLaunch() {
        val controller = buildActivity(MainActivity::class.java).setup()
    }
}