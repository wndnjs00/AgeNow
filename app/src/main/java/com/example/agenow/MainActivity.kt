package com.example.agecalculator

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.agenow.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // 현재 날짜 저장용 Calendar 객체
    private val currentDate = Calendar.getInstance()

    // 선택한 생년월일 저장용 Calendar 객체
    private var selectedDate: Calendar? = null

    // 날짜 표시 포맷
    private val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

    // UI 요소들
    private lateinit var tvSelectedDate: TextView
    private lateinit var btnSelectDate: Button
    private lateinit var tvAge: TextView
    private lateinit var tvKoreanAge: TextView
    private lateinit var tvMinutes: TextView
    private lateinit var tvHours: TextView
    private lateinit var tvDays: TextView
    private lateinit var tvMonths: TextView
    private lateinit var cardAge: CardView
    private lateinit var cardKoreanAge: CardView
    private lateinit var cardMinutes: CardView
    private lateinit var cardHours: CardView
    private lateinit var cardDays: CardView
    private lateinit var cardMonths: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI 요소 초기화
        initializeViews()

        // 이벤트 리스너 설정
        setupListeners()
    }

    private fun initializeViews() {
        tvSelectedDate = findViewById(R.id.tv_selected_date)
        btnSelectDate = findViewById(R.id.btn_select_date)
        tvAge = findViewById(R.id.tv_age)
        tvKoreanAge = findViewById(R.id.tv_korean_age)
        tvMinutes = findViewById(R.id.tv_minutes)
        tvHours = findViewById(R.id.tv_hours)
        tvDays = findViewById(R.id.tv_days)
        tvMonths = findViewById(R.id.tv_months)

        cardAge = findViewById(R.id.card_age)
        cardKoreanAge = findViewById(R.id.card_korean_age)
        cardMinutes = findViewById(R.id.card_minutes)
        cardHours = findViewById(R.id.card_hours)
        cardDays = findViewById(R.id.card_days)
        cardMonths = findViewById(R.id.card_months)
    }

    private fun setupListeners() {
        // 날짜 선택 버튼 클릭 이벤트
        btnSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        // 각 카드 클릭 이벤트
        cardAge.setOnClickListener {
            showDetailDialog("당신의 나이는", "${calculateAge()}세", "만으로는 ${calculateKoreanAge()}세입니다", "🎂")
        }

        cardKoreanAge.setOnClickListener {
            showDetailDialog("당신의 만 나이는", "${calculateKoreanAge()}세", "연 나이로는 ${calculateAge()}세입니다", "🎉")
        }

        cardMinutes.setOnClickListener {
            showDetailDialog("태어난 후 지난 시간", "${calculateMinutes()}분", "꽤 긴 시간이죠!", "⏱️")
        }

        cardHours.setOnClickListener {
            showDetailDialog("태어난 후 지난 시간", "${calculateHours()}시간", "많은 경험을 했을 거예요", "🕰️")
        }

        cardDays.setOnClickListener {
            showDetailDialog("태어난 후 지난 시간", "${calculateDays()}일", "하루하루 소중한 시간입니다", "📅")
        }

        cardMonths.setOnClickListener {
            showDetailDialog("태어난 후 지난 시간", "${calculateMonths()}개월", "시간이 빠르게 흐르네요", "📆")
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // 선택한 날짜로 Calendar 객체 설정
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                selectedDate = selectedCalendar

                // 화면에 선택한 날짜 표시
                tvSelectedDate.text = dateFormat.format(selectedCalendar.time)

                // 나이 및 경과 시간 계산하여 표시
                updateAgeInformation()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // 과거 날짜만 선택 가능하도록 설정
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateAgeInformation() {
        tvAge.text = "${calculateAge()}세"
        tvKoreanAge.text = "${calculateKoreanAge()}세"
        tvMinutes.text = "${calculateMinutes()}분"
        tvHours.text = "${calculateHours()}시간"
        tvDays.text = "${calculateDays()}일"
        tvMonths.text = "${calculateMonths()}개월"
    }

    private fun calculateAge(): Int {
        if (selectedDate == null) return 0

        var age = currentDate.get(Calendar.YEAR) - selectedDate!!.get(Calendar.YEAR)

        // 생일이 지났는지 체크
        if (currentDate.get(Calendar.MONTH) < selectedDate!!.get(Calendar.MONTH) ||
            (currentDate.get(Calendar.MONTH) == selectedDate!!.get(Calendar.MONTH) &&
                    currentDate.get(Calendar.DAY_OF_MONTH) < selectedDate!!.get(Calendar.DAY_OF_MONTH))) {
            age--
        }

        return age
    }

    private fun calculateKoreanAge(): Int {
        if (selectedDate == null) return 0

        val age = currentDate.get(Calendar.YEAR) - selectedDate!!.get(Calendar.YEAR)

        // 한국 만 나이는 생일에 +1
        return age
    }

    private fun calculateMinutes(): Long {
        if (selectedDate == null) return 0

        val diffInMillis = currentDate.timeInMillis - selectedDate!!.timeInMillis
        return TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
    }

    private fun calculateHours(): Long {
        if (selectedDate == null) return 0

        val diffInMillis = currentDate.timeInMillis - selectedDate!!.timeInMillis
        return TimeUnit.MILLISECONDS.toHours(diffInMillis)
    }

    private fun calculateDays(): Long {
        if (selectedDate == null) return 0

        val diffInMillis = currentDate.timeInMillis - selectedDate!!.timeInMillis
        return TimeUnit.MILLISECONDS.toDays(diffInMillis)
    }

    private fun calculateMonths(): Int {
        if (selectedDate == null) return 0

        var months = (currentDate.get(Calendar.YEAR) - selectedDate!!.get(Calendar.YEAR)) * 12
        months += currentDate.get(Calendar.MONTH) - selectedDate!!.get(Calendar.MONTH)

        // 현재 일이 생일 일보다 작으면 한 달 덜 계산
        if (currentDate.get(Calendar.DAY_OF_MONTH) < selectedDate!!.get(Calendar.DAY_OF_MONTH)) {
            months--
        }

        return months
    }

    private fun showDetailDialog(title: String, value: String, description: String, emoji: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_detail)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 다이얼로그 UI 요소
        val tvDialogTitle = dialog.findViewById<TextView>(R.id.tv_dialog_title)
        val tvDialogValue = dialog.findViewById<TextView>(R.id.tv_dialog_value)
        val tvDialogDescription = dialog.findViewById<TextView>(R.id.tv_dialog_description)
        val ivDialogEmoji = dialog.findViewById<ImageView>(R.id.iv_dialog_emoji)
        val btnDialogConfirm = dialog.findViewById<Button>(R.id.btn_dialog_confirm)

        // 데이터 설정
        tvDialogTitle.text = title
        tvDialogValue.text = value
        tvDialogDescription.text = description

        // 이모지 텍스트뷰로 설정 (ImageView 대신)
        val tvEmoji = TextView(this)
        tvEmoji.text = emoji
        tvEmoji.textSize = 30f

        // 오류 수정: parent as Int 부분을 제거하고 레이아웃 직접 참조
        val container = dialog.findViewById<ConstraintLayout>(R.id.dialog_container)
        container.removeView(ivDialogEmoji)
        container.addView(tvEmoji)

        // 이모지 TextView 레이아웃 설정
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        params.topToBottom = R.id.tv_dialog_description
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        params.topMargin = resources.getDimensionPixelSize(R.dimen.spacing_medium) // 적절한 마진 리소스 필요
        tvEmoji.layoutParams = params
        tvEmoji.gravity = Gravity.CENTER
        tvEmoji.visibility = View.VISIBLE

        // 확인 버튼 클릭 이벤트
        btnDialogConfirm.setOnClickListener {
            dialog.dismiss()
        }

        // 애니메이션 적용
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        container.startAnimation(slideUp)

        dialog.show()
    }
}