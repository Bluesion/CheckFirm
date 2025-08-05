package com.illusion.checkfirm.features.search.ui

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.recyclerview.RecyclerViewHorizontalMarginDecorator
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.databinding.DialogSearchBinding
import com.illusion.checkfirm.features.main.ui.ReportActivity
import com.illusion.checkfirm.features.settings.help.FirmwareManualActivity
import com.illusion.checkfirm.features.sherlock.ui.SherlockActivity

// SearchDialog는 다른 Dialog와 다르게 디자인이 달라 CheckFirmBottomSheetDialog를 상속받지 않는다.
class SearchDialog(private val isOfficial: Boolean, private val i: Int) : BottomSheetDialogFragment() {

    private var binding: DialogSearchBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // SearchDialogTheme을 적용해준다.
        setStyle(STYLE_NO_FRAME, R.style.SearchDialogTheme)
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogSearchBinding.inflate(inflater)

        val contentPadding = Tools.dpToPx(requireContext(), 12f)
        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(contentPadding, contentPadding, contentPadding, contentPadding)
            insets
        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var copy = ""

        if (isOfficial) {
            binding!!.latestTitle.text = getString(R.string.official_latest)

            if (CheckFirm.firmwareItems[i].officialFirmwareItem.latestFirmware.isBlank()) {
                binding!!.copyButton.visibility = View.GONE
                binding!!.latestFirmware.text = getString(R.string.search_error)
                if (CheckFirm.firmwareItems[i].officialFirmwareItem.previousFirmware.isNotEmpty()) {
                    binding!!.dynamicButton.setOnClickListener {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                "https://doc.samsungmobile.com/${CheckFirm.searchModel[i]}/${CheckFirm.searchCSC[i]}/doc.html".toUri()
                            )
                        )
                    }
                } else {
                    binding!!.dynamicButton.visibility = View.GONE
                }
            } else {
                binding!!.dynamicButton.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            "https://doc.samsungmobile.com/${CheckFirm.searchModel[i]}/${CheckFirm.searchCSC[i]}/doc.html".toUri()
                        )
                    )
                }

                copy = CheckFirm.firmwareItems[i].officialFirmwareItem.latestFirmware
                binding!!.latestFirmware.text = CheckFirm.firmwareItems[i].officialFirmwareItem.latestFirmware
            }
        } else {
            binding!!.latestTitle.text = getString(R.string.test_latest)
            binding!!.dynamicButton.setImageResource(R.drawable.ic_sherlock)

            if (CheckFirm.firmwareItems[i].testFirmwareItem.latestFirmware.isBlank()) {
                binding!!.latestFirmware.text = getString(R.string.search_error)
                if (CheckFirm.firmwareItems[i].testFirmwareItem.previousFirmware.isNotEmpty() && Tools.isEncrypted(
                        CheckFirm.firmwareItems[i].testFirmwareItem.previousFirmware.firstKey()[0]
                    )
                ) {
                    binding!!.dynamicButton.setOnClickListener {
                        val intent = Intent(requireActivity(), SherlockActivity::class.java)
                        intent.putExtra("index", i)
                        if (CheckFirm.firmwareItems[i].officialFirmwareItem.latestFirmware.isBlank()) {
                            intent.putExtra("pro_mode", true)
                        }
                        startActivity(intent)
                        dismiss()
                    }
                } else {
                    binding!!.dynamicButton.visibility = View.GONE
                }
            } else {
                binding!!.latestFirmware.text = CheckFirm.firmwareItems[i].testFirmwareItem.latestFirmware
                copy = CheckFirm.firmwareItems[i].testFirmwareItem.latestFirmware
                if (Tools.isEncrypted(CheckFirm.firmwareItems[i].testFirmwareItem.latestFirmware[0])) {
                    binding!!.dynamicButton.setOnClickListener {
                        val intent = Intent(requireActivity(), SherlockActivity::class.java)
                        intent.putExtra("index", i)
                        if (CheckFirm.firmwareItems[i].officialFirmwareItem.latestFirmware.isBlank()) {
                            intent.putExtra("pro_mode", true)
                        }
                        startActivity(intent)
                        dismiss()
                    }
                } else {
                    binding!!.dynamicButton.visibility = View.GONE
                }
            }

            if (CheckFirm.firmwareItems[i].testFirmwareItem.decryptedFirmware.isNotBlank()) {
                copy = CheckFirm.firmwareItems[i].testFirmwareItem.decryptedFirmware
                binding!!.latestFirmware.text = CheckFirm.firmwareItems[i].testFirmwareItem.decryptedFirmware
            }

            if (CheckFirm.firmwareItems[i].testFirmwareItem.clue.isNotBlank()) {
                copy = CheckFirm.firmwareItems[i].testFirmwareItem.clue
                binding!!.latestFirmware.text = CheckFirm.firmwareItems[i].testFirmwareItem.clue
            }

            if (binding!!.latestFirmware.text == getString(R.string.search_error)) {
                binding!!.copyButton.visibility = View.GONE
            } else {
                binding!!.copyButton.visibility = View.VISIBLE
            }
        }

        initPreviousLayout()
        initSmartSearchLayout()

        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding!!.copyButton.setOnClickListener {
            val clip = ClipData.newPlainText("CheckFirm", copy)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireActivity(), R.string.clipboard, Toast.LENGTH_SHORT).show()
        }

        binding!!.reportButton.setOnClickListener {
            startActivity(Intent(requireContext(), ReportActivity::class.java).apply {
                putExtra("index", i)
            })
        }

        binding!!.ok.setOnClickListener {
            dismiss()
        }
    }

    private fun initPreviousLayout() {
        if (isOfficial) {
            binding!!.tabLayout.visibility = View.GONE
            binding!!.previousTitle.text = getString(R.string.official_previous)

            val previousArray = if (CheckFirm.firmwareItems[i].officialFirmwareItem.previousFirmware.isEmpty()) {
                arrayOf(getString(R.string.search_error))
            } else {
                CheckFirm.firmwareItems[i].officialFirmwareItem.previousFirmware.values.toTypedArray()
            }

            binding!!.dynamicRecyclerView.adapter = SearchDialogAdapter(previousArray)
        } else {
            val previousArray = if (CheckFirm.firmwareItems[i].testFirmwareItem.previousFirmware.isEmpty()) {
                arrayOf(getString(R.string.search_error))
            } else {
                CheckFirm.firmwareItems[i].testFirmwareItem.previousFirmware.values.toTypedArray()
            }
            binding!!.dynamicRecyclerView.adapter = SearchDialogAdapter(previousArray)

            if (CheckFirm.firmwareItems[i].testFirmwareItem.betaFirmware.isEmpty()) {
                binding!!.tabLayout.visibility = View.GONE
                binding!!.previousTitle.text = getString(R.string.test_previous)
            } else {
                binding!!.previousTitle.visibility = View.GONE

                binding!!.tabPreviousFirmware.setOnClickListener {
                    binding!!.tabPreviousFirmware.setTabSelected(true)
                    binding!!.tabBetaFirmware.setTabSelected(false)
                    binding!!.dynamicRecyclerView.adapter = SearchDialogAdapter(previousArray)
                }

                binding!!.tabBetaFirmware.setOnClickListener {
                    binding!!.tabPreviousFirmware.setTabSelected(false)
                    binding!!.tabBetaFirmware.setTabSelected(true)
                    binding!!.dynamicRecyclerView.adapter = SearchDialogAdapter(CheckFirm.firmwareItems[i].testFirmwareItem.betaFirmware.values.toTypedArray())
                }
            }
        }

        binding!!.dynamicRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding!!.dynamicRecyclerView.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            v.onTouchEvent(event)
            true
        }
    }

    private fun initSmartSearchLayout() {
        val smartSearchTitle = arrayOf(
            getString(R.string.smart_search_bootloader),
            getString(R.string.smart_search_major_version),
            getString(R.string.smart_search_build_date),
            getString(R.string.smart_search_minor_version)
        )
        val smartSearchValue: Array<String>
        val smartSearchDescription: Array<String>

        val officialFirmwareInfo = Tools.getBuildInfo(CheckFirm.firmwareItems[i].officialFirmwareItem.latestFirmware)

        if (isOfficial) {
            if (CheckFirm.firmwareItems[i].officialFirmwareItem.latestFirmware.isBlank()) {
                binding!!.smartSearchCard.visibility = View.GONE
                return
            }

            smartSearchValue = arrayOf(
                officialFirmwareInfo.substring(0, 2),
                officialFirmwareInfo.substring(2, 3),
                officialFirmwareInfo.substring(3, 5),
                officialFirmwareInfo.substring(5, 6)
            )

            smartSearchDescription = arrayOf(
                "",
                String.format(
                    getString(R.string.smart_search_android_version_format),
                    CheckFirm.firmwareItems[i].officialFirmwareItem.androidVersion
                ),
                getFirmwareDate(officialFirmwareInfo.substring(3, 5)),
                ""
            )
        } else {
            var testFirmwareInfo = CheckFirm.firmwareItems[i].testFirmwareItem.latestFirmware

            if (CheckFirm.firmwareItems[i].testFirmwareItem.decryptedFirmware.isNotBlank()) {
                testFirmwareInfo = Tools.getBuildInfo(CheckFirm.firmwareItems[i].testFirmwareItem.decryptedFirmware)
            }

            if (CheckFirm.firmwareItems[i].testFirmwareItem.clue.isNotBlank()) {
                testFirmwareInfo = Tools.getBuildInfo(CheckFirm.firmwareItems[i].testFirmwareItem.clue)
            }

            if (testFirmwareInfo.isBlank()) {
                binding!!.smartSearchCard.visibility = View.GONE
                return
            }

            smartSearchValue = arrayOf(
                testFirmwareInfo.substring(0, 2),
                testFirmwareInfo.substring(2, 3),
                testFirmwareInfo.substring(3, 5),
                testFirmwareInfo.substring(5, 6)
            )

            val bootloaderDescription = if (officialFirmwareInfo.substring(0, 2) == testFirmwareInfo.substring(0, 2)) {
                getString(R.string.smart_search_downgrade_possible)
            } else {
                getString(R.string.smart_search_downgrade_impossible)
            }

            val compareMajorVersion = officialFirmwareInfo[2].compareTo(testFirmwareInfo.substring(2, 3)[0])
            val majorVersionDescription = when {
                compareMajorVersion < 0 -> {
                    getString(R.string.smart_search_type_major)
                }

                compareMajorVersion > 0 -> {
                    getString(R.string.smart_search_type_rollback)
                }

                else -> {
                    getString(R.string.smart_search_type_minor)
                }
            }

            smartSearchDescription = arrayOf(
                bootloaderDescription,
                majorVersionDescription,
                getFirmwareDate(testFirmwareInfo.substring(3, 5)),
                ""
            )
        }

        binding!!.smartSearchRecyclerView.adapter = SmartSearchAdapter(smartSearchTitle, smartSearchValue, smartSearchDescription)
        binding!!.smartSearchRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding!!.smartSearchRecyclerView.addItemDecoration(
            RecyclerViewHorizontalMarginDecorator(
            Tools.dpToPx(requireContext(), 32f))
        )

        binding!!.smartSearchHelp.setOnClickListener {
            startActivity(Intent(requireActivity(), FirmwareManualActivity::class.java))
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
