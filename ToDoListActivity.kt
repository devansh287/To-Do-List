package com.example.a394.cs193a_hw2_devansh

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_to_do_list.*
import java.io.File
import java.io.PrintStream
import java.util.*

class ToDoListActivity : AppCompatActivity() {

    //array that stores the to do list
    private lateinit var items: ArrayList<String>
    //adapter
    private lateinit var myAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)
        var list12 = findViewById<ListView>(R.id.list123)
        items = ArrayList<String>()
        myAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_1, items
        )
        //checking if the file exists or not, and if it exists, copying the existing materials to the array
        //if file doesn't exist, then the function creates a file
        val name = "list_items.txt"
        //helper function
        fileWork(name)
        list12.adapter = myAdapter

        //if item pressed at for long, it is removed
        list123.setOnItemLongClickListener { list, item, index, rowID ->
            items.removeAt(index)
            myAdapter.notifyDataSetChanged()
            true
        }

        //if item is pressed at, it moves to the top of the list
        list123.setOnItemClickListener { list, item, index, rowID ->
            val c = items[index]
            items.removeAt(index)
            items.add(0, c)
            myAdapter.notifyDataSetChanged()
        }
    }

    //helper function that does all the preliminary work required for the file
    private fun fileWork(name: String) {
        var file = File(name)
        var fileExists = file.exists()
        if (fileExists) {
            val scan = Scanner(openFileInput(name))
            while (scan.hasNextLine()) {
                items.add(scan.nextLine())
            }
            scan.close()
        } else {
            val outStream = PrintStream(
                openFileOutput(
                    name,
                    MODE_PRIVATE
                )
            )
            outStream.close()
        }
    }

    //entering an item in the list by pressing the button
    fun enter_item(view: View) {
        val entry = findViewById<EditText>(R.id.task)
        val pr = findViewById<EditText>(R.id.priority)

        //task name
        val action: String = entry.text.toString()

        //priority of the task
        val x = pr.text.toString()

        //adding the task
        if (x.isEmpty()) {
            items.add(0, action)
        } else {
            val prior: Int = x.toInt()
            if (prior > items.size) items.add(0, action)
            else items.add(prior - 1, action)
        }
        entry.text.clear()
        pr.text.clear()
        myAdapter.notifyDataSetChanged()
    }


    //updating the file after the app closes
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?)
    {
        super.onSaveInstanceState(outState, outPersistentState)
        val outStream = PrintStream(
            openFileOutput
                (
                "list_items.txt",
                MODE_PRIVATE
            )
        )
        val i = 0
        while (i in 1..items.size) {
            outStream.println(items[i])
        }
        outStream.close()
    }
}