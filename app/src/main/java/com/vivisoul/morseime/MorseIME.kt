package com.vivisoul.morseime

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import java.util.ArrayList

/**
 * Created by Hughie on 2017/6/10.
 */
class MorseIME : InputMethodService() , KeyboardView.OnKeyboardActionListener{
    private var keyboardView: KeyboardView? = null // 对应keyboard.xml中定义的KeyboardView
    private var keyboard: Keyboard? = null  // 对应qwerty.xml中定义的Keyboard
    private var candidateView: CandidateView? = null

    private var m_inputMethodManager: InputMethodManager? = null

    private val m_composeString = StringBuilder()

    override fun onCreate() {
        Log.d(this.javaClass.toString(), "MorseIME.onCreate: ")
        super.onCreate()
        m_inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onInitializeInterface() {
        Log.d(this.javaClass.toString(), "MorseIME.onInitializeInterface: ")
    }

    override fun onCreateInputView(): View {
        Log.d(this.javaClass.toString(), "MorseIME.onCreateInputView: ")
        // keyboard被创建后，将调用onCreateInputView函数
        keyboardView = layoutInflater.inflate(R.layout.keyboard, null) as KeyboardView  // 此处使用了keyboard.xml
        keyboard = Keyboard(this, R.xml.qwerty)  // 此处使用了qwerty.xml
        keyboardView!!.setKeyboard(keyboard)
        keyboardView!!.setOnKeyboardActionListener(this)
        return keyboardView as KeyboardView
    }

    override fun onCreateCandidatesView(): View {
        Log.d(this.javaClass.toString(), "MorseIME.onCreateCandidatesView: ")
        candidateView = CandidateView(this)
        return candidateView as CandidateView
    }

    override fun onStartInput(editorInfo: EditorInfo, restarting: Boolean) {
        super.onStartInput(editorInfo, restarting)
        Log.d(this.javaClass.toString(), "MorseIME.onStartInput: ")

        m_composeString.setLength(0)
        updateCandidates()

    }

    private fun updateCandidates() {
        Log.d(this.javaClass.toString(), "MorseIME.updateCandidates: ")
        if (m_composeString.length > 0) {
            setCandidatesViewShown(true)
        } else {
            setCandidatesViewShown(false)
        }
        if (candidateView != null) {
            val list = ArrayList<String>()
            list.add(m_composeString.toString())
            candidateView!!.setSuggestions(list)
        }
    }

    private fun playClick(keyCode: Int) {
        // 点击按键时播放声音，在onKey函数中被调用
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when (keyCode) {
            32 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
            Keyboard.KEYCODE_DONE, 10 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
            Keyboard.KEYCODE_DELETE -> am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)
            else -> am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        }
    }

    override fun swipeRight() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPress(primaryCode: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.d(this.javaClass.toString(), "MorseIME.onPress key = " + primaryCode)
    }

    override fun onRelease(primaryCode: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        Log.d(this.javaClass.toString(), "MorseIME.onRelease key = " + primaryCode)

    }

    override fun swipeLeft() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun swipeUp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun swipeDown() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
     override fun onKey(primaryCode: Int, keyCodes: IntArray) {
        val ic = currentInputConnection
        playClick(primaryCode)
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> ic.deleteSurroundingText(1, 0)
            Keyboard.KEYCODE_DONE -> ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            else -> {
                val code = primaryCode.toChar()
                if (code == ' ') {
                    if (
                    m_composeString.isNotEmpty()) {
                        ic.commitText(m_composeString, m_composeString.length)
                        m_composeString.setLength(0)
                    } else {
                        ic.commitText(" ", 1)
                    }
                } else {
                    m_composeString.append(code)
                    ic.setComposingText(m_composeString, 1)
                }
                updateCandidates()
            }
        }
    }
    override fun onText(text: CharSequence?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}