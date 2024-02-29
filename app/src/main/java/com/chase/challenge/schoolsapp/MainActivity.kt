package com.chase.challenge.schoolsapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chase.challenge.schoolsapp.data.SchoolAndScoreList
import com.chase.challenge.schoolsapp.data.SchoolAndScoreListItem
import com.chase.challenge.schoolsapp.data.SchoolList
import com.chase.challenge.schoolsapp.data.SchoolListItem
import com.chase.challenge.schoolsapp.databinding.ActivityMainBinding
import com.chase.challenge.schoolsapp.viewmodels.NYCSchoolsViewModel
import com.chase.challenge.schoolsapp.viewmodels.NYCSchoolsViewModelFactory
import com.chase.challenge.schoolsapp.viewmodels.SocketTimeoutException


class MainActivity : AppCompatActivity() {
    private val TAG = "School list"
    private lateinit var viewModel: NYCSchoolsViewModel

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, NYCSchoolsViewModelFactory())[NYCSchoolsViewModel::class.java]

        try {
            //Checking network connection before fetching schools
            if(viewModel.isConnectedToNetwork(this))
                viewModel.fetchListOfSchools()
            else
                showDialog(getString(R.string.error), getString(R.string.network_error_message), true)
        }catch (e: SocketTimeoutException){
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.error))
            builder.setMessage(getString(R.string.generic_error_message))
            val dialog: AlertDialog = builder.create()
            builder.setPositiveButton(getString(R.string.retry)
            ) { dialog, which ->
                dialog.dismiss()
                viewModel.fetchListOfSchools()
            }

            dialog.show()
        }

        binding.viewModel = viewModel
        binding.schoolsListRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            val decoration = DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
            addItemDecoration(decoration)
        }

        viewModel.getAdapter().onItemClick = { schoolAndScoreItem ->
            val message = String.format(getString(R.string.score_message), schoolAndScoreItem.sat_critical_reading_avg_score, schoolAndScoreItem.sat_writing_avg_score, schoolAndScoreItem.sat_math_avg_score)
            showDialog(getString(R.string.title_school_scores), message, false)
        }

        viewModel.schoolListLiveData.observe(this, Observer { schoolList ->
            // onResponse of school list API
            Log.i(TAG, "Number of schools : " + schoolList.size.toString())
            viewModel.setSchoolsData(schoolList)
            viewModel.fetchSchoolScores()

        })

        viewModel.schoolScoreLiveData.observe(this, Observer { schoolScoreList ->
            // onResponse of School scores API
            Log.i(TAG, "School Scores list size : " + schoolScoreList.size.toString())
            binding.progressBar.visibility = View.GONE
            binding.nycSchoolResultsContainer.visibility = View.VISIBLE
            val schoolAndScoreList: SchoolAndScoreList = viewModel.mergeSchoolAndScores(schoolScoreList)
            Log.i(TAG, "School list size after merging : " + schoolAndScoreList.size.toString())
            viewModel.setAdapterData(schoolAndScoreList)
        })
    }

    private fun showDialog(title: String, message: String, isFinish: Boolean) {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle(title)
            setMessage(message)
            setPositiveButton(android.R.string.ok) { dialog, which ->
                if(isFinish)
                    finish()
            }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}