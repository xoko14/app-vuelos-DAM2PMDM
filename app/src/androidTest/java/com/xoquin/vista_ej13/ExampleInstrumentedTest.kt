package com.xoquin.vista_ej13

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xoquin.vista_ej13.dao.VueloDAO

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.xoquin.vista_ej13", appContext.packageName)
    }

    @Test
    fun getVuelos(){
        val vdao = VueloDAO()
        val vuelos = vdao.getAll()
        Log.i("VUELO", vuelos.size.toString())
        vuelos.forEach {
            Log.i("VUELO", it.toString())
        }
    }
}