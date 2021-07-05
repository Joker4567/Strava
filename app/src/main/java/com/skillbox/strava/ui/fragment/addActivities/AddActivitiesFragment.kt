package com.skillbox.strava.ui.fragment.addActivities

import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.skillbox.core.extensions.OnTouchListener
import com.skillbox.core.extensions.afterTextChanged
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.state.StateToolbar
import com.skillbox.shared_model.ToolbarModel
import com.skillbox.shared_model.network.ActivityType
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentAddActivitiesBinding
import dagger.hilt.android.AndroidEntryPoint
import icepick.Icepick
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AddActivitiesFragment : ViewBindingFragment<FragmentAddActivitiesBinding>(FragmentAddActivitiesBinding::inflate) {

    override val screenViewModel by viewModels<AddActivitiesViewModel>()
    private var selectedType: ActivityType? = null
    private var startDownload: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Опять же очень захламлен onViewCreated, лучше разнести все по функциям
        WorkManager.getInstance(requireContext())
                .getWorkInfosForUniqueWorkLiveData(DOWNLOAD_WORK_ID)
                .observe(viewLifecycleOwner, { if(startDownload) handleWorkInfo(it.first()) })

        setAdapterTypeActivities()
        binding.activitiesDateValue.OnTouchListener {
            getDateOfTime()
        }
        binding.activitiesButtonInsert.setOnClickListener {
            postActivities()
        }

        onError()
        StateToolbar.changeToolbarTitle(ToolbarModel("", visible = false))

        if (savedInstanceState != null) {
            try {
                Icepick.restoreInstanceState(this, savedInstanceState)
            } catch (ex: Exception) {
                Log.e("AddActivitiesFragment", ex.localizedMessage)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        try {
            Icepick.saveInstanceState(this, outState)
        } catch (ex: Exception) {
            Log.e("AddActivitiesFragment", ex.localizedMessage)
        }
        super.onSaveInstanceState(outState)
    }

    private fun stopDownload() {
        WorkManager.getInstance(requireContext()).cancelUniqueWork(DOWNLOAD_WORK_ID)
    }

    private fun handleWorkInfo(workInfo: WorkInfo) {
        val isFinished = workInfo.state.isFinished
        when(workInfo.state){
            WorkInfo.State.FAILED -> {
                Log.e("AddActivitiesFragment", "Ошибка добавления записи")
            }
            WorkInfo.State.SUCCEEDED -> {
                Log.d("AddActivitiesFragment", "Запись успешно добавлена")
            }
        }
        if(isFinished)
            Log.d("AddActivitiesFragment", "Передача записи завершена")
    }

    private fun onError() {
        binding.activitiesDateValue.afterTextChanged { str ->
            if(str.isNotEmpty()) {
                binding.activitiesDate.isErrorEnabled = false
                binding.activitiesDate.error = null
                binding.activitiesDateValue.error = null
            }
        }
        binding.activitiesTypeValue.afterTextChanged { str ->
            if(str.isNotEmpty()) {
                binding.activitiesType.isErrorEnabled = false
                binding.activitiesType.error = null
                binding.activitiesTypeValue.error = null
            }
        }
        binding.activitiesNameValue.afterTextChanged { str ->
            if(str.isNotEmpty()) {
                binding.activitiesName.isErrorEnabled = false
                binding.activitiesName.error = null
                binding.activitiesNameValue.error = null
            }
        }
        binding.activitiesTimeValue.afterTextChanged { str ->
            if(str.isNotEmpty()) {
                binding.activitiesTime.isErrorEnabled = false
                binding.activitiesTime.error = null
                binding.activitiesTimeValue.error = null
            }
        }
        binding.activitiesDistanceValue.afterTextChanged { str ->
            if(str.isNotEmpty()) {
                binding.activitiesDistance.isErrorEnabled = false
                binding.activitiesDistance.error = null
                binding.activitiesDistanceValue.error = null
            }
        }
    }

    private fun postActivities() {
        val name = binding.activitiesNameValue.text?.toString() ?: ""
        val type = selectedType
        val date = binding.activitiesDateValue.text?.toString() ?: ""
        val time = binding.activitiesTimeValue.text?.toString() ?: ""
        val distance = binding.activitiesDistanceValue.text?.toString() ?: ""
        val description = binding.activitiesDescriptionValue.text?.toString() ?: ""
        if(name.isEmpty()) {
            binding.activitiesName.isErrorEnabled = true
            binding.activitiesName.error = getString(R.string.add_activities_error_name)
            binding.activitiesName.errorIconDrawable = requireContext().getDrawable(R.drawable.ic_error) // Вот здесь даже сама студия подчеркивает и советует как сделать правильно
            binding.activitiesNameValue.requestFocus()
        }
        if(type == null) {
            binding.activitiesType.isErrorEnabled = true
            binding.activitiesType.error = getString(R.string.add_activities_error_type)
            binding.activitiesType.errorIconDrawable = requireContext().getDrawable(R.drawable.ic_error)
            binding.activitiesTypeValue.requestFocus()
        }
        if(date.isEmpty()) {
            binding.activitiesDate.isErrorEnabled = true
            binding.activitiesDate.error = getString(R.string.add_activities_error_date)
            binding.activitiesDate.errorIconDrawable = requireContext().getDrawable(R.drawable.ic_error)
            binding.activitiesDateValue.setError(getString(R.string.add_activities_error_date), requireContext().getDrawable(R.drawable.ic_error))
            binding.activitiesDateValue.requestFocus()
        }
        if(time.isEmpty()) {
            binding.activitiesTime.isErrorEnabled = true
            binding.activitiesTime.error = getString(R.string.add_activities_error_time)
            binding.activitiesTime.errorIconDrawable = requireContext().getDrawable(R.drawable.ic_error)
            binding.activitiesTimeValue.setError(getString(R.string.add_activities_error_minutes), requireContext().getDrawable(R.drawable.ic_error))
            binding.activitiesTimeValue.requestFocus()
        }
        if(distance.isEmpty()) {
            binding.activitiesDistance.isErrorEnabled = true
            binding.activitiesDistance.error = getString(R.string.add_activities_error_dist)
            binding.activitiesDistance.errorIconDrawable = requireContext().getDrawable(R.drawable.ic_error)
            binding.activitiesDistanceValue.setError(getString(R.string.add_activities_error_dist_metr), requireContext().getDrawable(R.drawable.ic_error))
            binding.activitiesDistanceValue.requestFocus()
        }

        // Как-то это бы на функции разбить все, а то какая-то мешанина все в одном
        if(name.isNotEmpty() && type != null && date.isNotEmpty() && time.isNotEmpty() && distance.isNotEmpty()) {
            startDownload = true

            val workData = workDataOf(
                    SendActivitiesWorker.MODEL_NAME to name,
                    SendActivitiesWorker.MODEL_DATE to date,
                    SendActivitiesWorker.MODEL_DESC to description,
                    SendActivitiesWorker.MODEL_DIST to distance,
                    SendActivitiesWorker.MODEL_TIME to time,
                    SendActivitiesWorker.MODEL_TYPE to type.name
            )

            screenViewModel.saveLocal(name, ActivityType.valueOf(type.name), date, time.toInt(), description, distance.toFloat())

            val workConstraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)//имеется подключение к интернету
                    .setRequiresBatteryNotLow(true)//Нормальный уровень заряда
                    .build()

            val workRequest = OneTimeWorkRequestBuilder<SendActivitiesWorker>()
                    .setInputData(workData)
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
                    .setConstraints(workConstraints)
                    .build()

            WorkManager.getInstance(requireContext())
                    .enqueueUniqueWork(DOWNLOAD_WORK_ID, ExistingWorkPolicy.KEEP, workRequest)

            if (isResumed) {
                findNavController()
                        .navigateUp()
            }
        }
    }

    private fun setAdapterTypeActivities() {
        val items = listOf(
                ActivityType.Ride.name,
                ActivityType.Crossfit.name,
                ActivityType.Hike.name,
                ActivityType.IceSkate.name,
                ActivityType.Workout.name
        )
        val adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, items)
        binding.activitiesTypeValue.setAdapter(adapter)
        binding.activitiesTypeValue.setOnItemClickListener { _, _, position, _ ->
            selectedType = ActivityType.valueOf(items[position])
        }
    }

    private fun getDateOfTime() {
        // Тут тоже все как-то в одной куче, надо бы разбить
        var dateSelectedUnixTime = 0L

        val datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText(getString(R.string.add_activities_error_date_choose))
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
        var hour = 0
        var minutes = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getTimeZone("UTC")
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minutes = calendar.get(Calendar.MINUTE)
        }
        val timePicker =
                MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(hour)
                        .setMinute(minutes)
                        .setTitleText(getString(R.string.add_activities_error_choose_time))
                        .build()

        timePicker.addOnPositiveButtonClickListener {
            var millisecond = 0L
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = dateSelectedUnixTime
                millisecond = calendar.timeInMillis
            }
            val dateUnixTime = (if (millisecond != 0L) millisecond else dateSelectedUnixTime) / 1000L
            val date = Date(dateUnixTime * 1000)

            val pattern = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val dateLocal: String = simpleDateFormat.format(date)

            binding.activitiesDateValue.setText(
                    "${dateLocal}T${timePicker.hour}:${timePicker.minute}:00Z"
            )
        }

        timePicker.addOnCancelListener {
            binding.activitiesDateValue.setText(getString(R.string.add_activities_error_choose_time))
        }

        datePicker.addOnPositiveButtonClickListener {
            dateSelectedUnixTime = it
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
            binding.activitiesDateValue.setText(getString(R.string.add_activities_error_date))
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

    companion object {
        private const val DOWNLOAD_WORK_ID = "download work"
    }
}