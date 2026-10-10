package de.meineapp.aufgaben

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

data class Task(val text: String, var done: Boolean = false)

class MainActivity : Activity() {

    private val tasks = mutableListOf<Task>()
    private lateinit var adapter: ArrayAdapter<Task>
    private lateinit var input: EditText
    private lateinit var counter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.input)
        counter = findViewById(R.id.counter)
        val list = findViewById<ListView>(R.id.list)

        loadTasks()

        adapter = object : ArrayAdapter<Task>(this, android.R.layout.simple_list_item_checked, tasks) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as CheckedTextView
                val task = getItem(position)!!
                view.text = task.text
                view.isChecked = task.done
                view.paintFlags = if (task.done) {
                    view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
                view.alpha = if (task.done) 0.5f else 1f
                return view
            }
        }
        list.adapter = adapter
        list.emptyView = findViewById(R.id.empty)

        list.setOnItemClickListener { _, _, position, _ ->
            tasks[position].done = !tasks[position].done
            changed()
        }
        list.setOnItemLongClickListener { _, _, position, _ ->
            AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(tasks[position].text)
                .setPositiveButton(R.string.delete) { _, _ ->
                    tasks.removeAt(position)
                    changed()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
            true
        }

        findViewById<Button>(R.id.addButton).setOnClickListener { addTask() }
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addTask()
                true
            } else {
                false
            }
        }
        findViewById<Button>(R.id.clearDoneButton).setOnClickListener {
            tasks.removeAll { it.done }
            changed()
        }

        updateCounter()
    }

    private fun addTask() {
        val text = input.text.toString().trim()
        if (text.isEmpty()) return
        tasks.add(0, Task(text))
        input.text.clear()
        changed()
    }

    private fun changed() {
        adapter.notifyDataSetChanged()
        updateCounter()
        saveTasks()
    }

    private fun updateCounter() {
        val done = tasks.count { it.done }
        counter.text = getString(R.string.open_count, tasks.size - done, done)
    }

    private fun saveTasks() {
        val json = JSONArray()
        tasks.forEach { json.put(JSONObject().put("text", it.text).put("done", it.done)) }
        getPreferences(MODE_PRIVATE).edit().putString(KEY_TASKS, json.toString()).apply()
    }

    private fun loadTasks() {
        val raw = getPreferences(MODE_PRIVATE).getString(KEY_TASKS, null) ?: return
        val json = JSONArray(raw)
        for (i in 0 until json.length()) {
            val obj = json.getJSONObject(i)
            tasks.add(Task(obj.getString("text"), obj.getBoolean("done")))
        }
    }

    companion object {
        private const val KEY_TASKS = "tasks"
    }
}
