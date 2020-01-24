package com.example.h_mal.myapplication.ui



import android.content.Context
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import com.example.h_mal.myapplication.db.AppDatabase
import com.example.h_mal.myapplication.db.RepoDao
import com.example.h_mal.myapplication.model.Repo
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertSame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var repoDao: RepoDao
    private lateinit var db: AppDatabase
    private lateinit var context: Context

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = AppDatabase.invoke(context)
        repoDao = db.getRepoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.clearAllTables()
    }

    @Test
    @Throws(Exception::class)
    fun addAndUpdateEntries() {

        val item1 = Repo(
            274562,
            "yajl-objc",
            "",
            "",
            "",
            ""
        )
        val item2 = Repo(
            274563,
            "simplerrd",
            "",
            "",
            "",
            ""
        )

        val list: List<Repo> = mutableListOf(item1,item2).toList()

        val long = runBlocking { repoDao.saveAllRepos(list) }

        assertEquals(long.size, 2)


    }
}
