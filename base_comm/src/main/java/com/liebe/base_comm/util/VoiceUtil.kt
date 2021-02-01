package com.liebe.base_comm.util

import android.speech.tts.TextToSpeech
import android.text.TextUtils
import com.dating.app.lib.extend.ifNull
import com.dating.app.lib.util.EventBusHelper
import com.liebe.base_lib.BaseApplication
import java.util.*


/**
 *Copyright:Copyright@2019成都捷德科技有限公司版权
 *
 * @Description 语音播报
 * @Author LiangZe
 * @Date 2020/4/23
 * @Version 2.0
 */
class VoiceUtil {
    private constructor()

    companion object {
        //能否播放中文语音
       private var couldSpeak: Boolean = true

        val instance: VoiceUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            VoiceUtil()
        }

        private var textToSpeech: TextToSpeech? = null

    }

    fun speak(content: String?) {
        if (TextUtils.isEmpty(content) ||!isOpenVoice()){
            return
        }
        textToSpeech?.let {
            if (couldSpeak) {
                it.speak(content, TextToSpeech.QUEUE_FLUSH, null)
                EventBusHelper.post(VoiceEvent())
            } else {
                showToast("语音播放不受支持")
            }
        }.ifNull {
            textToSpeech =
                TextToSpeech(BaseApplication.gContext, object : TextToSpeech.OnInitListener {
                    override fun onInit(status: Int) {
                        if (status == TextToSpeech.SUCCESS) {
                            textToSpeech?.let {
                                val result: Int = it.setLanguage(Locale.CHINA)
                                if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE) {
                                    showToast("语音播放不受支持")
                                    couldSpeak = false
                                }else{
                                    speak(content)
                                }
                            }
                        }
                    }
                })
        }
    }
}