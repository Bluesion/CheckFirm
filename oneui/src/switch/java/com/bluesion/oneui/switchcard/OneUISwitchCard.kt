package com.bluesion.oneui.switchcard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.bluesion.oneui.R
import com.bluesion.oneui.databinding.LayoutOneuiSwitchCardBinding
import com.google.android.material.card.MaterialCardView

class OneUISwitchCard : MaterialCardView {

    private var listener: OneUISwitchCardListener? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    private val binding: LayoutOneuiSwitchCardBinding by lazy {
        LayoutOneuiSwitchCardBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.layout_oneui_switch_card, this, false)
        )
    }

    init {
        setCardBackgroundColor(
            ContextCompat.getColor(context, R.color.oneui_switch_card_background_off)
        )

        setOnClickListener {
            binding.materialSwitch.toggle()
        }
    }

    private fun initView() {
        binding.materialSwitch.setOnCheckedChangeListener { _, isChecked ->
            setStatus(isChecked)
        }
        addView(binding.root)
        turnOff()
    }

    fun turnOn() {
        binding.materialSwitch.isChecked = true
        setCardBackgroundColor(
            ContextCompat.getColor(context, R.color.oneui_switch_card_background_on)
        )
        binding.switchText.text = context.getString(R.string.switch_on)
        listener?.onCheckedChange(true)
    }

    fun turnOff() {
        binding.materialSwitch.isChecked = false
        setCardBackgroundColor(
            ContextCompat.getColor(context, R.color.oneui_switch_card_background_off)
        )
        binding.switchText.text = context.getString(R.string.switch_off)
        listener?.onCheckedChange(false)
    }

    fun setSwitchCardListener(listener: OneUISwitchCardListener) {
        this.listener = listener
    }

    fun setStatus(isChecked: Boolean) {
        if (isChecked) {
            turnOn()
        } else {
            turnOff()
        }
    }
}