package com.example.khudiakov.tipcalculator

import android.graphics.Color.parseColor
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar

class MainActivity : AppCompatActivity() {

    val DEFAULT_TIP_PERCENTAGE = 10
    val REGULAR_TIP_PERCENTAGE = 5
    val EXCELLENT_TIP_PERCENTAGE = 20
    val ACTIVE_COLOR by lazy { parseColor("#FFD600") }

    var totalBillAmount = 0.0

    var percentage = 0
    var tipTotal = 0.0
    var finalBillAmount = 0.0

    val ratingChange = { view: View? ->
        when (view?.id) {
            ibRegularService.id -> percentage = REGULAR_TIP_PERCENTAGE
            ibGoodService.id -> percentage = DEFAULT_TIP_PERCENTAGE
            ibExcellentService.id -> percentage = EXCELLENT_TIP_PERCENTAGE
        }

        calculateFinalBill()
        setTipValue()
    }

    val setActive = { id: Int ->
        ibRegularService.colorFilter = null
        ibGoodService.colorFilter = null
        ibExcellentService.colorFilter = null

        when (id) {
            ibRegularService.id -> ibRegularService.setColorFilter(ACTIVE_COLOR)
            ibGoodService.id -> ibGoodService.setColorFilter(ACTIVE_COLOR)
            ibExcellentService.id -> ibExcellentService.setColorFilter(ACTIVE_COLOR)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTipValue()
        ibRegularService.setOnClickListener(ratingChange)
        ibGoodService.setOnClickListener(ratingChange)
        ibExcellentService.setOnClickListener(ratingChange)
        sbTipPercent.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    percentage = sbTipPercent.progress + 5

                    calculateFinalBill()
                    setTipValue()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        etBillAmount.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateFinalBill()

                setTipValue()
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun calculateFinalBill() {
        if (percentage == 0) {
            percentage = DEFAULT_TIP_PERCENTAGE
        }

        if (etBillAmount.text.isEmpty() || etBillAmount.text.toString() == ".") {
            totalBillAmount = 0.0
        } else {
            totalBillAmount = etBillAmount.text.toString().toDouble()
        }

        tipTotal = totalBillAmount * percentage / 100
        finalBillAmount = totalBillAmount + tipTotal
    }

    private fun setTipValue() {
        tvTipPercent.text = getString(R.string.main_msg_tip_percent, percentage)
        tvTipAmount.text = getString(R.string.main_msg_tip_total, tipTotal)
        tvBillTotalAmount.text = getString(R.string.main_msg_bill_total_result, finalBillAmount)
        sbTipPercent.progress = percentage - 5

        if (percentage < 10) {
            setActive(ibRegularService.id)
        } else if (percentage < 20) {
            setActive(ibGoodService.id)
        } else {
            setActive(ibExcellentService.id)
        }
    }
}
