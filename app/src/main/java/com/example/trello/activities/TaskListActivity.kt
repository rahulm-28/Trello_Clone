package com.example.trello.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trello.R
import com.example.trello.adapters.TaskListItemsAdapter
import com.example.trello.firebase.FirestoreClass
import com.example.trello.models.Board
import com.example.trello.models.Card
import com.example.trello.models.Task
import com.example.trello.models.User
import com.example.trello.utils.Constants
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : BaseActivity() {

    // A global variable for Board Details.
    private lateinit var mBoardDetails: Board

    // A global variable for board document id as mBoardDocumentId
    private lateinit var mBoardDocumentId: String

    lateinit var mAssignedMembersDetailList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            mBoardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this@TaskListActivity, mBoardDocumentId)
    }

    /**
     * A function to setup action bar
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_task_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.name
        }

        toolbar_task_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu to use in the action bar
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_members -> {

                val intent = Intent(this@TaskListActivity, MembersActivity::class.java)
                intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
                startActivityForResult(intent, MEMBERS_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && (requestCode == MEMBERS_REQUEST_CODE || requestCode == CARD_DETAILS_REQUEST_CODE)
        ) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getBoardDetails(this@TaskListActivity, mBoardDocumentId)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    /**
     * A function to get the result of Board Detail.
     */
    fun boardDetails(board: Board) {

        mBoardDetails = board

        hideProgressDialog()

        // Call the function to setup action bar.
        setupActionBar()



        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAssignedMembersListDetails(
            this@TaskListActivity,
            mBoardDetails.assignedTo
        )
    }

    /**
     * A function to get the task list name from the adapter class which we will be using to create a new task list in the database.
     */
    fun createTaskList(taskListName: String) {

        Log.e("Task List Name", taskListName)

        // Create and Assign the task details
        val task = Task(taskListName, FirestoreClass().getCurrentUserID())

        mBoardDetails.taskList.add(0, task) // Add task to the first position of ArrayList
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1) // Remove the last position as we have added the item manually for adding the TaskList.

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
    }

    /**
     * A function to update the taskList
     */
    fun updateTaskList(position: Int, listName: String, model: Task) {

        val task = Task(listName, model.createdBy)

        mBoardDetails.taskList[position] = task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
    }

    /**
     * A function to delete the task list from database.
     */
    fun deleteTaskList(position: Int) {

        mBoardDetails.taskList.removeAt(position)

        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
    }

    /**
     * A function to get the result of add or updating the task list.
     */
    fun addUpdateTaskListSuccess() {

        hideProgressDialog()

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this@TaskListActivity, mBoardDetails.documentId!!)
    }

    /**
     * A function to create a card and update it in the task list.
     */
    fun addCardToTaskList(position: Int, cardName: String) {

        // Remove the last item
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        val cardAssignedUsersList: ArrayList<String> = ArrayList()
        cardAssignedUsersList.add(FirestoreClass().getCurrentUserID())

        val card = Card(cardName, FirestoreClass().getCurrentUserID(), cardAssignedUsersList)

        val cardsList = mBoardDetails.taskList[position].cards
        cardsList.add(card)

        val task = Task(
            mBoardDetails.taskList[position].title,
            mBoardDetails.taskList[position].createdBy,
            cardsList
        )

        mBoardDetails.taskList[position] = task

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
    }

    /**
     * A function for viewing and updating card details.
     */
    fun cardDetails(taskListPosition: Int, cardPosition: Int) {
        val intent = Intent(this@TaskListActivity, CardDetailsActivity::class.java)
        intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
        intent.putExtra(Constants.TASK_LIST_ITEM_POSITION, taskListPosition)
        intent.putExtra(Constants.CARD_LIST_ITEM_POSITION, cardPosition)
        intent.putExtra(Constants.BOARD_MEMBERS_LIST, mAssignedMembersDetailList)
        startActivityForResult(intent, CARD_DETAILS_REQUEST_CODE)
    }

    fun boardMembersDetailList(list: ArrayList<User>) {

        mAssignedMembersDetailList = list

        hideProgressDialog()

        // Here we are appending an item view for adding a list task list for the board.
        val addTaskList = Task(resources.getString(R.string.add_list))
        mBoardDetails.taskList.add(addTaskList)

        rv_task_list.layoutManager =
            LinearLayoutManager(this@TaskListActivity, LinearLayoutManager.HORIZONTAL, false)
        rv_task_list.setHasFixedSize(true)

        // Create an instance of TaskListItemsAdapter and pass the task list to it.
        val adapter = TaskListItemsAdapter(this@TaskListActivity, mBoardDetails.taskList)
        rv_task_list.adapter = adapter // Attach the adapter to the recyclerView.
    }

    fun updateCardsInTaskList(taskListPosition: Int, cards: ArrayList<Card>) {

        // Remove the last item
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        mBoardDetails.taskList[taskListPosition].cards = cards

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
    }

    /**
     * A companion object to declare the constants.
     */
    companion object {
        const val MEMBERS_REQUEST_CODE: Int = 13
        const val CARD_DETAILS_REQUEST_CODE: Int = 14
    }
}

//class TaskListActivity : BaseActivity() {
//
//    private lateinit var mBoardDetails: Board
//    private lateinit var mBoardDocumentId: String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_task_list)
//
//        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
//            mBoardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID).toString()
//        }
//
//        showProgressDialog(resources.getString(R.string.please_wait))
//        FirestoreClass().getBoardDetails(this, mBoardDocumentId)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == MEMBERS_REQUEST_CODE) {
//            showProgressDialog(resources.getString(R.string.please_wait))
//            FirestoreClass().getBoardDetails(this, mBoardDocumentId)
//        } else {
//            Log.e("Cancelled", "Cancelled")
//        }
//    }
//
////    override fun onResume() {
////        showProgressDialog(resources.getString(R.string.please_wait))
////        FirestoreClass().getBoardDetails(this, mBoardDocumentId)
////        super.onResume()
////    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_members, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.action_members -> {
//                val intent = Intent(this, MembersActivity::class.java)
//                intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
//                startActivityForResult(intent, MEMBERS_REQUEST_CODE)
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun setupActionBar() {
//
//        setSupportActionBar(toolbar_task_list_activity)
//
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
//            actionBar.title = mBoardDetails.name
//        }
//
//        toolbar_task_list_activity.setNavigationOnClickListener { onBackPressed() }
//    }
//
//    fun boardDetails(board: Board) {
//        mBoardDetails = board
//
//        hideProgressDialog()
//        setupActionBar()
//
//        val addTaskList = Task(resources.getString(R.string.add_list))
//        mBoardDetails.taskList.add(addTaskList)
//
//        rv_task_list.layoutManager =
//            LinearLayoutManager(this@TaskListActivity, LinearLayoutManager.HORIZONTAL, false)
//        rv_task_list.setHasFixedSize(true)
//
//        val adapter = TaskListItemsAdapter(this@TaskListActivity, mBoardDetails.taskList)
//        rv_task_list.adapter = adapter
//    }
//
//    fun addUpdateTaskListSuccess() {
//
//        hideProgressDialog()
//
//        showProgressDialog(resources.getString(R.string.please_wait))
//        FirestoreClass().getBoardDetails(this@TaskListActivity, mBoardDetails.documentId!!)
//    }
//
//    fun createTaskList(taskListName: String) {
//
//        Log.e("Task List Name", taskListName)
//
//        val task = Task(taskListName, FirestoreClass().getCurrentUserID())
//
//        mBoardDetails.taskList.add(0, task)
//        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
//
//        showProgressDialog(resources.getString(R.string.please_wait))
//
//        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
//    }
//
//    fun updateTaskList(position: Int, listName: String, model: Task) {
//
//        val task = Task(listName, model.createdBy)
//
//        mBoardDetails.taskList[position] = task
//        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
//
//        showProgressDialog(resources.getString(R.string.please_wait))
//        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
//    }
//
//    fun deleteTaskList(position: Int){
//
//        mBoardDetails.taskList.removeAt(position)
//
//        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
//
//        showProgressDialog(resources.getString(R.string.please_wait))
//        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
//    }
//
//    fun addCardToTaskList(position: Int, cardName: String) {
//
//        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)
//
//        val cardAssignedUsersList: ArrayList<String> = ArrayList()
//        cardAssignedUsersList.add(FirestoreClass().getCurrentUserID())
//
//        val card = Card(cardName, FirestoreClass().getCurrentUserID(), cardAssignedUsersList)
//
//        val cardsList = mBoardDetails.taskList[position].cards
//        cardsList.add(card)
//
//        val task = Task(
//            mBoardDetails.taskList[position].title,
//            mBoardDetails.taskList[position].createdBy,
//            cardsList
//        )
//
//        mBoardDetails.taskList[position] = task
//
//        showProgressDialog(resources.getString(R.string.please_wait))
//        FirestoreClass().addUpdateTaskList(this@TaskListActivity, mBoardDetails)
//    }
//
//    fun cardDetails(taskListPosition: Int, cardPosition: Int) {
//        val intent = Intent(this, CardDetailsActivity::class.java)
//        intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
//        intent.putExtra(Constants.TASK_LIST_ITEM_POSITION, taskListPosition)
//        intent.putExtra(Constants.CARD_LIST_ITEM_POSITION, cardPosition)
//        startActivity(intent)
//    }
//
//    companion object {
//        const val MEMBERS_REQUEST_CODE: Int = 13
//    }
//}