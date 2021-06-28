package com.skillbox.strava.ui.fragment.addActivities

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentAddActivitiesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddActivitiesFragment : ViewBindingFragment<FragmentAddActivitiesBinding>(FragmentAddActivitiesBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = listOf("Male", "Female")
        val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, items)
        binding.activitiesTypeValue.setAdapter(adapter)
        binding.activitiesTypeValue.setOnItemClickListener { _, _, position, _ ->
            Log.d("LGT_CHOOSE", "Выбран ${items[position]}")
        }

        binding.activitiesDateValue.setOnClickListener {
            getDateOfTime()
        }
    }

    private fun getDateOfTime() {
        var dateOfTime = ""

        val datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Выберите дату забега")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
        var hour = 0
        var minutes = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getTimeZone("UTC")
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minutes =  calendar.get(Calendar.MINUTE)
        }
        val timePicker =
                MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(hour)
                        .setMinute(minutes)
                        .setTitleText("Выберите время начала забега")
                        .build()

        timePicker.addOnPositiveButtonClickListener {
            dateOfTime += "time"
            binding.activitiesDateValue.setText("dateOfTime")
        }

        timePicker.addOnCancelListener {
            binding.activitiesDateValue.setText("Выберите время")
        }

        datePicker.addOnPositiveButtonClickListener {
            binding.activitiesDateValue.setText("Дата выбрана")
            dateOfTime = "data "
            val fm = activity?.supportFragmentManager
            val tag = "dialog_time"
            val oldFragment = fm?.findFragmentByTag(tag)
            oldFragment?.let {
                fm.beginTransaction().remove(oldFragment).commit();
            }
            fm?.let {
                timePicker.show(fm, tag)
            }
        }
        datePicker.addOnCancelListener {
            binding.activitiesDateValue.setText("Выберите дату")
        }

        val fm = activity?.supportFragmentManager
        val tag = "dialog_date"
        val oldFragment = fm?.findFragmentByTag(tag)
        oldFragment?.let {
            fm.beginTransaction().remove(oldFragment).commit();
        }
        fm?.let {
            datePicker.show(fm, tag)
        }
    }

}