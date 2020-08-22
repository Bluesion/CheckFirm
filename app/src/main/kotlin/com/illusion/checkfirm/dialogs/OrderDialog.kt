package com.illusion.checkfirm.dialogs

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.DialogOrderBinding

class OrderDialog : BottomSheetDialogFragment(), CompoundButton.OnCheckedChangeListener {

    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var onBottomSheetCloseListener: OnBottomSheetCloseListener

    interface OnBottomSheetCloseListener {
        fun onBottomSheetClose()
    }

    fun setOnBottomSheetCloseListener(listener: OnBottomSheetCloseListener) {
        onBottomSheetCloseListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogOrderBinding.inflate(inflater)

        val device = binding.device
        val name = binding.name
        val time = binding.time

        val sharedPrefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        mEditor = sharedPrefs.edit()

        when (sharedPrefs.getString("bookmark_order_by", "time")) {
            "time" -> {
                time.isChecked = true
                device.isChecked = false
                name.isChecked = false
            }
            "device" -> {
                time.isChecked = false
                device.isChecked = true
                name.isChecked = false
            }
            "name" -> {
                time.isChecked = false
                device.isChecked = false
                name.isChecked = true
            }
            else -> {
                time.isChecked = true
                device.isChecked = false
                name.isChecked = false
            }
        }

        binding.descSwitch.isChecked = sharedPrefs.getBoolean("bookmark_order_by_desc", false)
        binding.descSwitch.setOnCheckedChangeListener(this)

        time.setOnClickListener {
            time.isChecked = true
            device.isChecked = false
            name.isChecked = false
            mEditor.putString("bookmark_order_by", "time")
            mEditor.apply()
        }

        device.setOnClickListener {
            time.isChecked = false
            device.isChecked = true
            name.isChecked = false
            mEditor.putString("bookmark_order_by", "device")
            mEditor.apply()
        }

        name.setOnClickListener {
            time.isChecked = false
            device.isChecked = false
            name.isChecked = true
            mEditor.putString("bookmark_order_by", "name")
            mEditor.apply()
        }

        binding.timeLayout.setOnClickListener {
            time.isChecked = true
            device.isChecked = false
            name.isChecked = false
            mEditor.putString("bookmark_order_by", "time")
            mEditor.apply()
        }

        binding.deviceLayout.setOnClickListener {
            time.isChecked = false
            device.isChecked = true
            name.isChecked = false
            mEditor.putString("bookmark_order_by", "device")
            mEditor.apply()
        }

        binding.nameLayout.setOnClickListener {
            time.isChecked = false
            device.isChecked = false
            name.isChecked = true
            mEditor.putString("bookmark_order_by", "name")
            mEditor.apply()
        }

        binding.descLayout.setOnClickListener {
            binding.descSwitch.toggle()
        }

        binding.ok.setOnClickListener {
            onBottomSheetCloseListener.onBottomSheetClose()
            dismiss()
        }

        return binding.root
    }

    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        when (p0!!.id) {
            R.id.desc_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("bookmark_order_by_desc", true)
                } else {
                    mEditor.putBoolean("bookmark_order_by_desc", false)
                }
            }
        }
        mEditor.apply()
    }
}
