package com.illusion.checkfirm.features.settings.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogLanguageBinding

class LanguageDialog : CheckFirmBottomSheetDialogFragment<DialogLanguageBinding>() {

    override fun onCreateView(inflater: LayoutInflater) = DialogLanguageBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        <?xml version="1.0" encoding="utf-8"?>
        <locale-config xmlns:android="http://schemas.android.com/apk/res/android">
            <locale android:name="af"/> <!-- Afrikaans -->
            <locale android:name="am"/> <!-- Amharic -->
            <locale android:name="ar"/> <!-- Arabic -->
            <locale android:name="as"/> <!-- Assamese -->
            <locale android:name="az"/> <!-- Azerbaijani -->
            <locale android:name="be"/> <!-- Belarusian -->
            <locale android:name="bg"/> <!-- Bulgarian -->
            <locale android:name="bn"/> <!-- Bengali -->
            <locale android:name="bs"/> <!-- Bosnian -->
            <locale android:name="ca"/> <!-- Catalan -->
            <locale android:name="cs"/> <!-- Czech -->
            <locale android:name="da"/> <!-- Danish -->
            <locale android:name="de"/> <!-- German -->
            <locale android:name="el"/> <!-- Greek -->
            <locale android:name="en-AU"/> <!-- English (Australia) -->
            <locale android:name="en-CA"/> <!-- English (Canada) -->
            <locale android:name="en-GB"/> <!-- English (United Kingdom) -->
            <locale android:name="en-IN"/> <!-- English (India) -->
            <locale android:name="en-US"/> <!-- English (United States) -->
            <locale android:name="es"/> <!-- Spanish (Spain) -->
            <locale android:name="es-US"/> <!-- Spanish (United States) -->
            <locale android:name="et"/> <!-- Estonian -->
            <locale android:name="eu"/> <!-- Basque -->
            <locale android:name="fa"/> <!-- Farsi -->
            <locale android:name="fi"/> <!-- Finnish -->
            <locale android:name="fil"/> <!-- Filipino -->
            <locale android:name="fr"/> <!-- French (France) -->
            <locale android:name="fr-CA"/> <!-- French (Canada) -->
            <locale android:name="gl"/> <!-- Galician -->
            <locale android:name="gu"/> <!-- Gujarati -->
            <locale android:name="hi"/> <!-- Hindi -->
            <locale android:name="hr"/> <!-- Croatian -->
            <locale android:name="hu"/> <!-- Hungarian -->
            <locale android:name="hy"/> <!-- Armenian -->
            <locale android:name="in"/> <!-- Indonesian -->
            <locale android:name="is"/> <!-- Icelandic -->
            <locale android:name="it"/> <!-- Italian -->
            <locale android:name="iw"/> <!-- Hebrew -->
            <locale android:name="ja"/> <!-- Japanese -->
            <locale android:name="ka"/> <!-- Georgian -->
            <locale android:name="kk"/> <!-- Kazakh -->
            <locale android:name="km"/> <!-- Khmer -->
            <locale android:name="kn"/> <!-- Kannada -->
            <locale android:name="ko"/> <!-- Korean -->
            <locale android:name="ky"/> <!-- Kyrgyz -->
            <locale android:name="lo"/> <!-- Lao -->
            <locale android:name="lt"/> <!-- Lithuanian -->
            <locale android:name="lv"/> <!-- Latvian -->
            <locale android:name="mk"/> <!-- Macedonian -->
            <locale android:name="ml"/> <!-- Malayalam -->
            <locale android:name="mn"/> <!-- Mongolian -->
            <locale android:name="mr"/> <!-- Marathi -->
            <locale android:name="ms"/> <!-- Malay -->
            <locale android:name="my"/> <!-- Burmese -->
            <locale android:name="nb"/> <!-- Norwegian -->
            <locale android:name="ne"/> <!-- Nepali -->
            <locale android:name="nl"/> <!-- Dutch -->
            <locale android:name="or"/> <!-- Odia -->
            <locale android:name="pa"/> <!-- Punjabi -->
            <locale android:name="pl"/> <!-- Polish -->
            <locale android:name="pt-BR"/> <!-- Portuguese (Brazil) -->
            <locale android:name="pt-PT"/> <!-- Portuguese (Portugal) -->
            <locale android:name="ro"/> <!-- Romanian -->
            <locale android:name="ru"/> <!-- Russian -->
            <locale android:name="si"/> <!-- Sinhala -->
            <locale android:name="sk"/> <!-- Slovak -->
            <locale android:name="sl"/> <!-- Slovenian -->
            <locale android:name="sq"/> <!-- Albanian -->
            <locale android:name="sr"/> <!-- Serbian (Cyrillic) -->
            <locale android:name="sr-Latn"/> <!-- Serbian (Latin) -->
            <locale android:name="sv"/> <!-- Swedish -->
            <locale android:name="sw"/> <!-- Swahili -->
            <locale android:name="ta"/> <!-- Tamil -->
            <locale android:name="te"/> <!-- Telugu -->
            <locale android:name="th"/> <!-- Thai -->
            <locale android:name="tr"/> <!-- Turkish -->
            <locale android:name="uk"/> <!-- Ukrainian -->
            <locale android:name="ur"/> <!-- Urdu -->
            <locale android:name="uz"/> <!-- Uzbek -->
            <locale android:name="vi"/> <!-- Vietnamese -->
            <locale android:name="zh-Hans"/> <!-- Chinese (Simplified) -->
            <locale android:name="zh-Hant"/> <!-- Chinese (Traditional) -->
            <locale android:name="zu"/> <!-- Zulu -->
        </locale-config>
        */

        val languageList = listOf(
            Pair(getString(R.string.settings_language_cs), "cs"),
            Pair(getString(R.string.settings_language_en), "en-US"),
            Pair(getString(R.string.settings_language_ja), "ja"),
            Pair(getString(R.string.settings_language_ko), "ko"),
            Pair(getString(R.string.settings_language_pt_rPT), "pt-PT"),
            Pair(getString(R.string.settings_language_pt_rBR), "pt-BR"),
            Pair(getString(R.string.settings_language_ro), "ro"),
            Pair(getString(R.string.settings_language_ru), "ru"),
            Pair(getString(R.string.settings_language_si), "si"),
            Pair(getString(R.string.settings_language_tr), "tr"),
            Pair(getString(R.string.settings_language_zh_rCN), "zh-Hans"),
            Pair(getString(R.string.settings_language_zh_rTW), "zh-Hant")
        )
        binding!!.recyclerView.adapter = LanguageAdapter(
            list = languageList,
            onItemClicked = {
                setLanguage(languageList[it].second)
            }
        )

        binding!!.ok.setOnClickListener {
            dismiss()
        }
    }

    private fun setLanguage(locale: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
        dismiss()
    }
}