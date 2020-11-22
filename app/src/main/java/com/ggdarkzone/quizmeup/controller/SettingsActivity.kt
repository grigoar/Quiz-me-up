package com.ggdarkzone.quizmeup.controller

import android.content.Intent
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ggdarkzone.quizmeup.R
import com.ggdarkzone.quizmeup.util.*

import com.ggdarkzone.quizmeup.util.Prefs


import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    val TAG = "Settings_Activity"
    private var selectedEasyMode = false
    private var selectedMediumMode = false
    private var selectedHardMode = false
    private var selectedAllMode = false

    private lateinit var difficultySelectedList: ArrayList<String>

    private lateinit var prefs: Prefs

    private var isApplicationHavingSound = false
    private lateinit var soundPool: SoundPool
    private var soundButtonGeneral: Int = 0
    private var leftVolume: Float = 0f
    private var rightVolume: Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        difficultySelectedList = ArrayList()
        prefs = Prefs(this@SettingsActivity)
        var sharedDifficulty: String? = prefs.getDifficulty()
        if (sharedDifficulty != null) {
            difficultySelectedList.add(sharedDifficulty)
        } else difficultySelectedList.add(MY_DIFFICULTY_ALL)

        textViewSelectionButtons(difficultySelectedList[difficultySelectedList.size - 1])


    }

    fun createButtonsSound() {
        soundPool = Utils.createSoundPoolObject()
        soundButtonGeneral = soundPool.load(this, R.raw.button_click_wet_for_the_rest, 1)
    }

    fun applicationSound() {
        if (isApplicationHavingSound == true) {
            leftVolume = 1f
            rightVolume = 1f
            MusicBackgroundHandler.playSoundFile(this, null, true, isApplicationHavingSound)
        } else if (isApplicationHavingSound == false) {
            leftVolume = 0f
            rightVolume = 0f
            MusicBackgroundHandler.pauseSound()
        }
    }


    fun soundOFFClicked(v: View) {
        isApplicationHavingSound = false
        applicationSound()
        imageViewSoundButtons()
        MusicBackgroundHandler.pauseSound()

    }

    fun soundONClicked(v: View) {
        isApplicationHavingSound = true
        applicationSound()
        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
        imageViewSoundButtons()
        MusicBackgroundHandler.playSoundFile(
            this@SettingsActivity,
            null,
            true,
            isApplicationHavingSound
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    fun buttonDifficultySelected(viewButtonDifficulty: View) {
        when (viewButtonDifficulty.id) {
            R.id.button_settings_easy -> {
                soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                if (selectedEasyMode == false) {
                    difficultySelectedList.add(MY_DIFFICULTY_EASY)
                    textViewSelectionButtons(MY_DIFFICULTY_EASY)
                    selectedEasyMode = !selectedEasyMode

                } else if (selectedEasyMode == true) {
                    difficultySelectedList.add(MY_DIFFICULTY_EASY)
                    textViewSelectionButtons(MY_DIFFICULTY_EASY)
                }
            }
            R.id.button_settings_medium -> {
                soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                if (selectedMediumMode == false) {
                    difficultySelectedList.add(MY_DIFFICULTY_MEDIUM)
                    textViewSelectionButtons(MY_DIFFICULTY_MEDIUM)
                    selectedMediumMode = !selectedMediumMode
                } else if (selectedMediumMode == true) {
                    difficultySelectedList.add(MY_DIFFICULTY_MEDIUM)
                    textViewSelectionButtons(MY_DIFFICULTY_MEDIUM)
                }
            }
            R.id.button_settings_hard -> {
                soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                if (selectedHardMode == false) {
                    difficultySelectedList.add(MY_DIFFICULTY_HARD)
                    textViewSelectionButtons(MY_DIFFICULTY_HARD)
                    selectedHardMode = !selectedHardMode
                } else if (selectedHardMode == true) {
                    difficultySelectedList.add(MY_DIFFICULTY_HARD)
                    textViewSelectionButtons(MY_DIFFICULTY_HARD)
                }
            }
            R.id.button_settings_all -> {
                soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                if (selectedAllMode == false) {
                    difficultySelectedList.add(MY_DIFFICULTY_ALL)
                    textViewSelectionButtons(MY_DIFFICULTY_ALL)
                    selectedAllMode = !selectedAllMode
                } else if (selectedAllMode == true) {
                    difficultySelectedList.add(MY_DIFFICULTY_ALL)
                    textViewSelectionButtons(MY_DIFFICULTY_ALL)
                }
            }
        }
    }

    fun aboutButtonClicked(view: View) {
        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
    }

    private fun textViewSelectionButtons(difficulty: String?) {
        when (difficulty) {
            MY_DIFFICULTY_EASY -> {
                imageView_check_easy.visibility = View.VISIBLE
                imageView_check_medium.visibility = View.INVISIBLE
                imageView_check_hard.visibility = View.INVISIBLE
                imageView_check_all.visibility = View.INVISIBLE
            }
            MY_DIFFICULTY_MEDIUM -> {
                imageView_check_easy.visibility = View.INVISIBLE
                imageView_check_medium.visibility = View.VISIBLE
                imageView_check_hard.visibility = View.INVISIBLE
                imageView_check_all.visibility = View.INVISIBLE
            }
            MY_DIFFICULTY_HARD -> {
                imageView_check_easy.visibility = View.INVISIBLE
                imageView_check_medium.visibility = View.INVISIBLE
                imageView_check_hard.visibility = View.VISIBLE
                imageView_check_all.visibility = View.INVISIBLE
            }
            MY_DIFFICULTY_ALL -> {
                imageView_check_easy.visibility = View.INVISIBLE
                imageView_check_medium.visibility = View.INVISIBLE
                imageView_check_hard.visibility = View.INVISIBLE
                imageView_check_all.visibility = View.VISIBLE
            }
            else -> {
                imageView_check_easy.visibility = View.VISIBLE
                imageView_check_medium.visibility = View.VISIBLE
                imageView_check_hard.visibility = View.VISIBLE
                imageView_check_all.visibility = View.VISIBLE
            }
        }
    }

    private fun imageViewSoundButtons() {
        if (isApplicationHavingSound) {
            imageView_sound_off.visibility = View.INVISIBLE
            imageView_sound_on.visibility = View.VISIBLE
        } else if (!isApplicationHavingSound) {
            imageView_sound_off.visibility = View.VISIBLE
            imageView_sound_on.visibility = View.INVISIBLE
        }

    }


    override fun onPause() {
        super.onPause()
        prefs.setSoundSettings(isApplicationHavingSound)
        prefs.setDificulty(difficultySelectedList[difficultySelectedList.size - 1])
        MusicBackgroundHandler.pauseSound()
    }

    override fun onResume() {
        super.onResume()
        isApplicationHavingSound = prefs.getSoundSettings()
        applicationSound()
        if (prefs.getDifficulty() != null) {
            difficultySelectedList.add(prefs.getDifficulty()!!)
        } else difficultySelectedList.add(MY_DIFFICULTY_EASY)

        MusicBackgroundHandler.playSoundFile(this, null, true, isApplicationHavingSound)
        imageViewSoundButtons()
        createButtonsSound()
    }

    override fun onStop() {
        super.onStop()
        if(soundPool!=null){
            soundPool.release()
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun backButtonClickedToSelectCategories(view: View) {
        soundPool?.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
        intent = Intent(this, SelectQuizCategoriesActivity::class.java)
        startActivity(intent)
    }


}