package com.illusion.checkfirm.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.DialogSearchBinding
import com.illusion.checkfirm.etc.SherlockActivity
import com.illusion.checkfirm.etc.WebViewActivity
import com.illusion.checkfirm.utils.Tools

class SearchDialog(private val isOfficial: Boolean, private val i: Int) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogSearchBinding.inflate(inflater)

        val searchPrefs = requireActivity().getSharedPreferences("search", Context.MODE_PRIVATE)
        val officialLatest = searchPrefs.getString("official_latest_$i", "null")!!

        var copy: String

        if (isOfficial) {
            copy = officialLatest
            binding.latestTitle.text = getString(R.string.official_latest)
            binding.latestFirmware.text = officialLatest
            binding.previousTitle.text = getString(R.string.official_previous)
            binding.list.text = searchPrefs.getString("official_previous_$i", "null")!!

            val changelog = binding.dynamicButton
            changelog.text = getString(R.string.changelog)
            changelog.setOnClickListener {
                val model = searchPrefs.getString("search_model_$i", "null")!!
                val csc = searchPrefs.getString("search_csc_$i", "null")!!

                val link = "https://doc.samsungmobile.com/$model/$csc/doc.html"
                val intent = Intent(requireActivity(), WebViewActivity::class.java)
                intent.putExtra("url", link)
                intent.putExtra("number", 1)
                startActivity(intent)
            }
            changelog.visibility = View.VISIBLE

            if (officialLatest != getString(R.string.search_error)) {
                binding.smartSearch.visibility = View.VISIBLE

                val firmwareInfo = Tools.getFirmwareInfo(officialLatest)

                binding.bootloader.text = firmwareInfo.substring(0, 2)

                binding.majorVersion.text = firmwareInfo.substring(2, 3)
                val androidVersion = searchPrefs.getString("official_latest_android_version_$i", "")!!
                if (androidVersion == getString(R.string.unknown)) {
                    binding.majorVersionDescription.text = ""
                } else {
                    binding.majorVersionDescription.text =
                        String.format(getString(R.string.smart_search_android_version_format), searchPrefs.getString("official_latest_android_version_$i", "null"))
                }

                val dateString = firmwareInfo.substring(3, 5)
                binding.date.text = dateString
                binding.dateDescription.text = getFirmwareDate(dateString)

                binding.minorVersion.text = firmwareInfo.substring(5, 6)
            }
        } else {
            val testLatest = searchPrefs.getString("test_latest_$i", "null")!!
            val testDecrypted = searchPrefs.getString("test_decrypted_$i", "null")!!

            copy = testLatest

            binding.latestTitle.text = getString(R.string.test_latest)
            binding.latestFirmware.text = testLatest
            binding.previousTitle.text = getString(R.string.test_previous)
            binding.list.text = searchPrefs.getString("test_previous_$i", "null")!!

            val sherlock = binding.dynamicButton
            sherlock.text = getString(R.string.sherlock)

            if (officialLatest != getString(R.string.search_error)
                && testLatest != getString(R.string.search_error)) {
                if (testLatest.contains("/")) {
                    binding.smartSearch.visibility = View.VISIBLE

                    val firmwareInfo = Tools.getFirmwareInfo(testLatest)

                    binding.bootloader.text = firmwareInfo.substring(0, 2)
                    binding.bootloaderDescription.text = searchPrefs.getString("test_downgrade_$i", "null")!!

                    binding.majorVersion.text = firmwareInfo.substring(2, 3)
                    binding.majorVersionDescription.text = searchPrefs.getString("test_type_$i", "null")!!

                    val dateString = firmwareInfo.substring(3, 5)
                    binding.date.text = dateString
                    binding.dateDescription.text = getFirmwareDate(dateString)

                    binding.minorVersion.text = firmwareInfo.substring(5, 6)
                } else {
                    if (testDecrypted == "null") {
                        sherlock.visibility = View.VISIBLE
                        sherlock.setOnClickListener {
                            val intent = Intent(requireActivity(), SherlockActivity::class.java)
                            intent.putExtra("index", i)
                            startActivity(intent)
                            dismiss()
                        }
                    } else {
                        copy = testDecrypted

                        sherlock.visibility = View.GONE
                        binding.decryptedFirmware.text = testDecrypted
                        binding.watson.text = String.format(getString(R.string.sherlock_watson_format), searchPrefs.getString("test_watson_$i", "null")!!)
                        binding.original.visibility = View.VISIBLE
                        binding.decryptedLayout.visibility = View.VISIBLE

                        binding.smartSearch.visibility = View.VISIBLE

                        val officialFirmwareInfo = Tools.getFirmwareInfo(officialLatest)
                        val testFirmwareInfo = Tools.getFirmwareInfo(testDecrypted)

                        val bootloader = testFirmwareInfo.substring(0, 2)
                        binding.bootloader.text = bootloader
                        if (officialFirmwareInfo.substring(0, 2) == bootloader) {
                            binding.bootloaderDescription.text = getString(R.string.smart_search_downgrade_possible)
                        } else {
                            binding.bootloaderDescription.text = getString(R.string.smart_search_downgrade_impossible)
                        }

                        val majorVersion = testFirmwareInfo.substring(2, 3)
                        binding.majorVersion.text = majorVersion
                        val compareMajorVersion = officialFirmwareInfo[2].compareTo(majorVersion[0])
                        when {
                            compareMajorVersion < 0 -> {
                                binding.majorVersionDescription.text = getString(R.string.smart_search_type_major)
                            }
                            compareMajorVersion > 0 -> {
                                binding.majorVersionDescription.text = getString(R.string.smart_search_type_rollback)
                            }
                            else -> {
                                binding.majorVersionDescription.text = getString(R.string.smart_search_type_minor)
                            }
                        }

                        val dateString = testFirmwareInfo.substring(3, 5)
                        binding.date.text = dateString
                        binding.dateDescription.text = getFirmwareDate(dateString)

                        binding.minorVersion.text = testFirmwareInfo.substring(5, 6)
                    }
                }
            } else if (officialLatest == getString(R.string.search_error)
                && testLatest != getString(R.string.search_error)) {
                if (testLatest.contains("/")) {
                    binding.smartSearch.visibility = View.VISIBLE

                    val firmwareInfo = Tools.getFirmwareInfo(testLatest)

                    binding.bootloader.text = firmwareInfo.substring(0, 2)
                    binding.majorVersion.text = firmwareInfo.substring(2, 3)

                    val dateString = firmwareInfo.substring(3, 5)
                    binding.date.text = dateString
                    binding.dateDescription.text = getFirmwareDate(dateString)

                    binding.minorVersion.text = firmwareInfo.substring(5, 6)
                } else {
                    if (testDecrypted == "null") {
                        sherlock.visibility = View.VISIBLE
                        sherlock.setOnClickListener {
                            val intent = Intent(requireActivity(), SherlockActivity::class.java)
                            intent.putExtra("index", i)
                            intent.putExtra("pro_mode", true)
                            startActivity(intent)
                            dismiss()
                        }
                    } else {
                        copy = testDecrypted

                        sherlock.visibility = View.GONE
                        binding.decryptedFirmware.text = testDecrypted
                        binding.watson.text = String.format(getString(R.string.sherlock_watson_format), searchPrefs.getString("test_watson_$i", "null")!!)
                        binding.original.visibility = View.VISIBLE
                        binding.decryptedLayout.visibility = View.VISIBLE

                        binding.smartSearch.visibility = View.VISIBLE

                        val firmwareInfo = Tools.getFirmwareInfo(testDecrypted)

                        binding.bootloader.text = firmwareInfo.substring(0, 2)
                        binding.majorVersion.text = firmwareInfo.substring(2, 3)

                        val dateString = firmwareInfo.substring(3, 5)
                        binding.date.text = dateString
                        binding.dateDescription.text = getFirmwareDate(dateString)

                        binding.minorVersion.text = firmwareInfo.substring(5, 6)
                    }
                }
            }
        }

        val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding.copy.setOnClickListener {
            val clip = ClipData.newPlainText("checkfirmLatest", copy)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireActivity(), R.string.clipboard, Toast.LENGTH_SHORT).show()
        }

        binding.ok.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    private fun getFirmwareDate(date: String): String {
        val year = when (date.substring(0, 1)) {
            "A" -> getString(R.string.year_a)
            "B" -> getString(R.string.year_b)
            "C" -> getString(R.string.year_c)
            "D" -> getString(R.string.year_d)
            "E" -> getString(R.string.year_e)
            "F" -> getString(R.string.year_f)
            "G" -> getString(R.string.year_g)
            "H" -> getString(R.string.year_h)
            "I" -> getString(R.string.year_i)
            "J" -> getString(R.string.year_j)
            "K" -> getString(R.string.year_k)
            "L" -> getString(R.string.year_l)
            "M" -> getString(R.string.year_m)
            "N" -> getString(R.string.year_n)
            "O" -> getString(R.string.year_o)
            "P" -> getString(R.string.year_p)
            "Q" -> getString(R.string.year_q)
            "R" -> getString(R.string.year_r)
            "S" -> getString(R.string.year_s)
            "T" -> getString(R.string.year_t)
            "U" -> getString(R.string.year_u)
            "V" -> getString(R.string.year_v)
            "W" -> getString(R.string.year_w)
            "X" -> getString(R.string.year_x)
            "Y" -> getString(R.string.year_y)
            "Z" -> getString(R.string.year_z)
            else -> getString(R.string.unknown)
        }
        val month = when (date.substring(1, 2)) {
            "A" -> getString(R.string.january)
            "B" -> getString(R.string.february)
            "C" -> getString(R.string.march)
            "D" -> getString(R.string.april)
            "E" -> getString(R.string.may)
            "F" -> getString(R.string.june)
            "G" -> getString(R.string.july)
            "H" -> getString(R.string.august)
            "I" -> getString(R.string.september)
            "J" -> getString(R.string.october)
            "K" -> getString(R.string.november)
            "L" -> getString(R.string.december)
            else -> getString(R.string.unknown)
        }

        return String.format(getString(R.string.smart_search_date_format), year, month)
    }
}
