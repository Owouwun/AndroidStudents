package com.example.scheduleappdb.LogicsZone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleappdb.R
import com.example.scheduleappdb.databinding.MainActivityBinding
import com.example.scheduleappdb.RVZone.CustomRecyclerAdapterForStudents
import com.example.scheduleappdb.RVZone.RecyclerItemClickListener
import com.example.scheduleappdb.UIZone.group.*
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MainActivity :AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    StudentDetailsDialogFragment.OnInputListenerSortId {

    private val gson: Gson = GsonBuilder().create()
    private lateinit var connection: Connection
    private var connectionStage: Int = Constants.ConnectionStage.WaitingForConnection.toInt
    private var startTime: Long = 0

    private lateinit var db: AppDatabase
    private lateinit var goDao: GroupOperatorDao

    private lateinit var cm_groupName_textView: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var nv: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var cm_progressBar: ProgressBar
    private lateinit var cm_rv_students: RecyclerView

    private var go: GroupOperator = GroupOperator()
    private var currentGroupID: Int = -1
    private var currentStudentID: Int = -1
    private var waitingForUpdate: Boolean = false

    private lateinit var binding: MainActivityBinding

    fun putStudent(bundle: Bundle, student: Student) {
        bundle.putString("studentName", student.name)
        bundle.putInt("number", student.number)
        bundle.putSerializable("exams", student.exams)
        bundle.putFloat("mean", student.mean)
        bundle.putBoolean("confirmed",student.confirmed)
    }
    fun putStudent(intent: Intent, student: Student) {
        intent.putExtra("studentName", student.name)
        intent.putExtra("number", student.number)
        intent.putExtra("exams", student.exams)
        intent.putExtra("mean", student.mean)
        intent.putExtra("confirmed",student.confirmed)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.maAppBar.abToolbar)

        cm_groupName_textView = findViewById(R.id.cm_textView_groupName)
        drawerLayout = binding.maLayoutDrawer
        nv = binding.maNavigationView
        nv.setNavigationItemSelectedListener(this)
        toolbar = findViewById(R.id.ab_toolbar)
        toolbar.apply { setNavigationIcon(R.drawable.ic_my_menu) }
        toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        cm_progressBar = findViewById(R.id.cm_progressBar)
        cm_rv_students = findViewById(R.id.cm_rv_students)
        cm_rv_students.visibility = View.INVISIBLE
        cm_rv_students.layoutManager = LinearLayoutManager(this)

        cm_rv_students.addOnItemTouchListener(
            RecyclerItemClickListener(
                cm_rv_students,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        currentStudentID = position
                        val studentDetails = StudentDetailsDialogFragment()
                        val tempStudent = go.getStudent(currentGroupID, currentStudentID)
                        val bundle = Bundle()

                        putStudent(bundle, tempStudent)

                        bundle.putString("connection", connectionStage.toString())
                        studentDetails.arguments = bundle
                        studentDetails.show(supportFragmentManager, "MyCustomDialog")
                    }
                    override fun onItemLongClick(view: View, position: Int) {
                        currentStudentID = position
                        val toast = Toast.makeText(
                            applicationContext,
                            "Подтвержден: " + go.getStudent(currentGroupID, currentStudentID).confirmed.toString(),
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            )
        )

        db = App.instance?.database!!
        goDao = db.groupOperatorDao()

        startTime = System.currentTimeMillis()

        connection = Connection(
            Constants.serverIP,
            Constants.serverPort,
            "{R}",
            this
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (currentGroupID != Constants.ConnectionStage.OutOfConnection.toInt)
            menu.getItem(0).isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_actionAdd) {
            val intent = Intent()
            intent.setClass(this, StudentActivity::class.java)
            intent.putExtra("action", Constants.Action.Adding.toInt)
            setResult(1)
            resultLauncher.launch(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class Connection(
        private val SERVER_IP: String,
        private val SERVER_PORT: Int,
        private val refreshCommand: String,
        private val activity: Activity
    ) {
        private var outputServer: PrintWriter? = null
        private var inputServer: BufferedReader? = null
        var thread1: Thread? = null
        private var threadT: Thread? = null

        internal inner class Thread1Server : Runnable {
            override fun run() {
                val socket: Socket
                try {
                    socket = Socket(SERVER_IP, SERVER_PORT)
                    outputServer = PrintWriter(socket.getOutputStream())
                    inputServer = BufferedReader(InputStreamReader(socket.getInputStream()))
                    Thread(Thread2Server()).start()
                    sendDataToServer(refreshCommand)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        internal inner class Thread2Server : Runnable {
            override fun run() {
                while (true) {
                    try {
                        val message = inputServer!!.readLine()
                        if (message != null)
                            activity.runOnUiThread { processingInputStream(message) }
                        else {
                            thread1 = Thread(Thread1Server())
                            thread1!!.start()
                            return
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        internal inner class Thread3Server(private val message: String) : Runnable {
            override fun run() {
                outputServer!!.write(message)
                outputServer!!.flush()
            }
        }

        internal inner class ThreadT : Runnable {
            override fun run() {
                while (true) {
                    if (System.currentTimeMillis() - startTime > 5000L &&
                        connectionStage == Constants.ConnectionStage.WaitingForConnection.toInt) {
                        activity.runOnUiThread {
                            val toast = Toast.makeText(
                                applicationContext,
                            "Подключение к серверу\n" +
                                    "не установлено",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                        connectionStage = Constants.ConnectionStage.OutOfConnection.toInt
                        cm_progressBar.visibility = View.INVISIBLE
                        go = goDao.getById(1)
                        if (go!=null)
                            for (i in 0 until go.getGroups()!!.size)
                                activity.runOnUiThread {
                                    nv.menu.add(0, i, 0, go.getGroups()!![i].name as CharSequence)
                                }
                    }
                }
            }
        }

        fun sendDataToServer(text: String) {
            Thread(Thread3Server(text + "\n")).start()
        }

        private fun processingInputStream(text: String) {
            goDao.delete(GroupOperator())
            val tempGo: GroupOperator = gson.fromJson(text, GroupOperator::class.java)

            goDao.insert(tempGo)

            if (connectionStage != 1) {
                val toast = Toast.makeText(
                    applicationContext,
                    "Подключение к серверу\n" +
                            "установлено",
                    Toast.LENGTH_LONG
                )
                toast.show()
            }

            cm_progressBar.visibility = View.INVISIBLE
            for (i in 0 until (go.getGroups()?.size?:0))
                nv.menu.removeItem(i)
            val tempArrayListGroups: ArrayList<Group>? = tempGo.getGroups()
            go.setGroups(tempArrayListGroups)
            for (i in 0 until (tempArrayListGroups?.size?:0))
                nv.menu.add(0, i, 0, tempArrayListGroups!![i].name as CharSequence)
            if (waitingForUpdate || connectionStage == Constants.ConnectionStage.OutOfConnection.toInt) {
                if (currentGroupID != -1){
                    cm_rv_students.adapter = CustomRecyclerAdapterForStudents(
                        go.getNames(currentGroupID),
                        go.getNumbers(currentGroupID),
                        go.getMeans(currentGroupID)
                    )
                }
                waitingForUpdate = false
            }
            connectionStage = Constants.ConnectionStage.SuccessfulConnection.toInt
        }

        init {
            thread1 = Thread(Thread1Server())
            thread1!!.start()
            threadT = Thread(ThreadT())
            threadT!!.start()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val tempString = "Группа ${item.title}"
        cm_groupName_textView.text = tempString

        invalidateOptionsMenu()
        currentGroupID = item.itemId
        cm_rv_students.adapter = CustomRecyclerAdapterForStudents(
            go.getNames(currentGroupID),
            go.getNumbers(currentGroupID),
            go.getMeans(currentGroupID)
        )
        cm_rv_students.visibility = View.VISIBLE
        return true
    }

    fun delExam() {
        connection.sendDataToServer("d$currentGroupID,$currentStudentID")
        waitingForUpdate = true
    }

    override fun sendInputSortId(sortId: Int) {
        if (sortId > -1 && sortId < 8) {
            go.sortStudents(currentGroupID, sortId)
            if (connectionStage == Constants.ConnectionStage.SuccessfulConnection.toInt)
                connection.sendDataToServer("u" + gson.toJson(go))
        }
        if (sortId == 8) {        // Удаление
            val manager: FragmentManager = supportFragmentManager
            val myDialogFragmentDelStudent = MyDialogFragmentDelStudent()
            val bundle = Bundle()
            bundle.putString("studentName", go.getStudent(currentGroupID, currentStudentID).name)
            myDialogFragmentDelStudent.arguments = bundle
            myDialogFragmentDelStudent.show(manager, "myDialog")
        }
        if (sortId == 9) {        // Изменение
            val tempStudent = go.getStudent(currentGroupID, currentStudentID)
            val intent = Intent()
            intent.setClass(this, StudentActivity::class.java)
            intent.putExtra("action", Constants.Action.Editing.toInt)

            putStudent(intent, tempStudent)

            setResult(1)
            resultLauncher.launch(intent)
        }
        cm_rv_students.adapter = CustomRecyclerAdapterForStudents(
            go.getNames(currentGroupID),
            go.getNumbers(currentGroupID),
            go.getMeans(currentGroupID)
        )
    }

    var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data : Intent? = result.data
            val action = data?.getIntExtra("action", -1)!!

            val tempStudent = Student(
                data.getStringExtra("studentName") ?: "",
                data.getIntExtra("number",-1),
                data.getSerializableExtra("exams") as ArrayList<Exam>,
                //data.getParcelableArrayListExtra("exams", Class<Exam>),
                data.getFloatExtra("mean", -1F),
                data.getBooleanExtra("confirmed", false)
            )
            val tempExamJSON: String = gson.toJson(tempStudent)

            if (action == Constants.Action.Adding.toInt) {
                val tempStringToSend = "a${go.getGroups()!![currentGroupID].name}##$tempExamJSON"
                connection.sendDataToServer(tempStringToSend)
                waitingForUpdate = true
            }
            if (action == Constants.Action.Editing.toInt) {
                val tempStringToSend = "e$currentGroupID,$currentStudentID##$tempExamJSON"
                connection.sendDataToServer(tempStringToSend)
                waitingForUpdate = true
            }
        }
    }
}
