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
import com.example.scheduleappdb.databinding.ActivityMainBinding
import com.example.scheduleappdb.RVZone.CustomRecyclerAdapterForExams
import com.example.scheduleappdb.RVZone.RecyclerItemClickListener
//import com.example.scheduleappdb.UIZone.group.DbHelper
//import com.example.scheduleappdb.UIZone.group.Subject
//import com.example.scheduleappdb.UIZone.group.Group
//import com.example.scheduleappdb.UIZone.group.GroupOperator
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
    SubjectDetailsDialogFragment.OnInputListenerSortId {

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

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        cm_groupName_textView = findViewById(R.id.cm_groupName_textView)
        drawerLayout = binding.drawerLayout
        nv = binding.amNavigationView
        nv.setNavigationItemSelectedListener(this)
        toolbar = findViewById(R.id.toolbar)
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
                        val studentDetails = SubjectDetailsDialogFragment()
                        val tempStudent = go.getStudent(currentGroupID, currentStudentID)
                        val bundle = Bundle()
                        bundle.putString("name", tempStudent.name)
                        bundle.putInt("number", tempStudent.number)
                        bundle.putString("ex1_name", tempStudent.ex1_name)

                        val ex1_name: String,
                        val ex1_mark: Int,
                        val ex1_date: String,
                        val ex2_name: String,
                        val ex2_mark: Int,
                        val ex2_date: String,
                        val ex3_name: String,
                        val ex3_mark: Int,
                        val ex3_date: String,
                        val ex4_name: String,
                        val ex4_mark: Int,
                        val ex4_date: String,
                        val ex5_name: String,
                        val ex5_mark: Int,
                        val ex5_date: String,

                        val mean: Double,
                        val confirmed: Boolean
                        bundle.putString("name", tempStudent.name)
                        bundle.putString("teacherName", tempStudent.nameOfTeacher)
                        bundle.putString("auditory", tempStudent.auditory.toString())
                        bundle.putString("building", tempStudent.building)
                        bundle.putString("time", tempStudent.time)
                        bundle.putString("dow", tempStudent.dow.toString())
                        bundle.putString("week_parity", tempStudent.weekParity.toString())
                        bundle.putString("req_eq", tempStudent.comment)
                        bundle.putString("connection", connectionStage.toString())
                        studentDetails.arguments = bundle
                        studentDetails.show(supportFragmentManager, "MyCustomDialog")
                    }
                    override fun onItemLongClick(view: View, position: Int) {
                        currentStudentID = position
                        val toast = Toast.makeText(
                            applicationContext,
                            "Корпус: " + go.getStudent(currentGroupID, currentStudentID).building,
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
        if (currentGroupID != -1)
            menu.getItem(0).isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            val intent = Intent()
            intent.setClass(this, EditSubjectActivity::class.java)
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
                        for (i in 0 until go.getGroups().size)
                            activity.runOnUiThread {
                                nv.menu.add(0, i, 0, go.getGroups()[i].name as CharSequence)
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
            for (i in 0 until go.getGroups().size)
                nv.menu.removeItem(i)
            val tempArrayListGroups: ArrayList<Group> = tempGo.getGroups()
            go.setGroups(tempArrayListGroups)
            for (i in 0 until tempArrayListGroups.size)
                nv.menu.add(0, i, 0, tempArrayListGroups[i].name as CharSequence)
            if (waitingForUpdate || connectionStage == Constants.ConnectionStage.OutOfConnection.toInt) {
                if (currentGroupID != -1){
                    cm_rv_students.adapter = CustomRecyclerAdapterForExams(
                        go.getNames(currentGroupID),
                        go.getDow(currentGroupID),
                        go.getTimes(currentGroupID)
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
        cm_rv_students.adapter = CustomRecyclerAdapterForExams(
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
            val myDialogFragmentDelSubject = MyDialogFragmentDelSubject()
            val bundle = Bundle()
            bundle.putString("subject", go.getStudent(currentGroupID, currentStudentID).name)
            myDialogFragmentDelSubject.arguments = bundle
            myDialogFragmentDelSubject.show(manager, "myDialog")
        }
        if (sortId == 9) {        // Изменение
            val tempExam = go.getStudent(currentGroupID, currentStudentID)
            val intent = Intent()
            intent.setClass(this, EditSubjectActivity::class.java)
            intent.putExtra("action", Constants.Action.Editing.toInt)
            intent.putExtra("subject", tempExam.name)
            intent.putExtra("teacher", tempExam.nameOfTeacher)
            intent.putExtra("auditory", tempExam.auditory.toString())
            intent.putExtra("building", tempExam.building)
            intent.putExtra("time", tempExam.time)
            intent.putExtra("dow", tempExam.dow.toString())
            intent.putExtra("week_parity", tempExam.weekParity.toString())
            intent.putExtra("req_eq", tempExam.comment)
            //startActivityForResult(intent, 1)
            setResult(1)
            resultLauncher.launch(intent)
        }
        cm_rv_students.adapter = CustomRecyclerAdapterForExams(
            go.getNames(currentGroupID),
            go.getDow(currentGroupID),
            go.getTimes(currentGroupID)
        )
    }

    var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data : Intent? = result.data
            val action = data?.getIntExtra("action", -1)!!
            val examName = data.getStringExtra("subject") ?: ""
            val teacherName = data.getStringExtra("teacher") ?: ""
            val auditory = data.getIntExtra("auditory", -1)
            val date = data.getStringExtra("building") ?: ""
            val time = data.getStringExtra("time") ?: ""
            val people = data.getIntExtra("dow", -1)
            val abstract = data.getIntExtra("week_parity", -1)
            val comment = data.getStringExtra("req_eq") ?: ""
            val tempStudent = Student(
                examName,
                teacherName,
                auditory,
                date,
                time,
                people,
                abstract,
                comment
            )
            val tempExamJSON: String = gson.toJson(tempStudent)

            if (action == Constants.Action.Adding.toInt) {
                val tempStringToSend = "a${go.getGroups()[currentGroupID].name}##$tempExamJSON"
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
