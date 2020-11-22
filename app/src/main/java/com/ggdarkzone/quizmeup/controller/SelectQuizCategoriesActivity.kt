package com.ggdarkzone.quizmeup.controller

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.view.children
import com.ggdarkzone.quizmeup.R
import com.ggdarkzone.quizmeup.util.*

import kotlinx.android.synthetic.main.activity_select_quiz_categories.*
import kotlin.collections.ArrayList

class SelectQuizCategoriesActivity : AppCompatActivity() {

    val TAG = "Select_Activity"

   private var checkedGeneralKnowledge : Boolean = false
   private var checkedBooks = false
   private var checkedFilms = false
   private var checkedMusic = false
   private var checkedMusicalTheaters = false
   private var checkedTelevision = false
   private var checkedVideoGames = false
   private var checkedBoardGames = false
   private var checkedScienceNature = false
   private var checkedScienceComputers = false
   private var checkedScienceMathematics = false
   private var checkedMythology = false
   private var checkedSports = false
   private var checkedGeography = false
   private var checkedHistory = false
   private var checkedPolitics = false
   private var checkedArts = false
   private var checkedCelebrities = false
   private var checkedAnimals = false
   private var checkedVehicles = false
   private var checkedComics = false
   private var checkedScienceGadgets = false
   private var checkedJapaneseAnimeManga = false
   private var checkedCartoonAnimations = false
   private lateinit var categoriesSelected: ArrayList<String>
   private var category: String? = null

    private lateinit var prefs: Prefs

    private var countDownTimer: CountDownTimer? = null

    //for sounds
    private var isApplicationHavingSound = false
    private lateinit var soundPool: SoundPool
    private var soundButtonCategories:Int = 0
    private var soundButtonGeneral:Int = 0
    private var leftVolume: Float = 0f
    private var rightVolume: Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_quiz_categories)

        showCheckedTextImage()
        prefs = Prefs(this@SelectQuizCategoriesActivity)
     val gradientDrowable: GradientDrawable =
            Utils.gradientForBackground(this@SelectQuizCategoriesActivity, "#ffb400", "#ff5a00")
        buttons_start_quiz.background = gradientDrowable


    }

    fun createButtonsSound(){
        soundPool = Utils.createSoundPoolObject()
        soundButtonCategories = soundPool.load(this,R.raw.button_click_for_categories,1)
        soundButtonGeneral = soundPool.load(this, R.raw.button_click_wet_for_the_rest,1)
    }


    fun startGameActivity(v: View) {
        soundPool.play(soundButtonGeneral, leftVolume, rightVolume,0,0,1f)
        val intent = Intent(this, QuizActivity::class.java)
        if(categoriesSelected.isNullOrEmpty()){
            categoriesSelected.add(ANIMALS_STRING)
            categoriesSelected.add(ART_STRING)
            categoriesSelected.add(ENTERTAINMENT_BOARD_GAMES_STRING)
            categoriesSelected.add(ENTERTAINMENT_BOOKS_STRING)
            categoriesSelected.add(ENTERTAINMENT_CARTOON_ANIMATIONS_STRING)
            categoriesSelected.add(CELEBRITIES_STRING)
            categoriesSelected.add(ENTERTAINMENT_COMICS_STRING)
            categoriesSelected.add(ENTERTAINMENT_FILM_STRING)
            categoriesSelected.add(GENERAL_KNOWLEDGE_STRING)
            categoriesSelected.add(GEOGRAPHY_STRING)
            categoriesSelected.add(HISTORY_STRING)
            categoriesSelected.add(ENTERTAINMENT_JAPANESE_ANIME_MANGA_STRING)
            categoriesSelected.add(ENTERTAINMENT_MUSICAL_THEATERS_STRING)
            categoriesSelected.add(MYTHOLOGY_STRING)
            categoriesSelected.add(POLITICS_STRING)
            categoriesSelected.add(SCIENCE_COMPUTERS_STRING)
            categoriesSelected.add(SCIENCE_GADGETS_STRING)
            categoriesSelected.add(SCIENCE_MATHEMATICS_STRING)
            categoriesSelected.add(SCIENCE_NATURE_STRING)
            categoriesSelected.add(SPORTS_STRING)
            categoriesSelected.add(ENTERTAINMENT_TELEVISION_STRING)
            categoriesSelected.add(VEHICLES_STRING)
            categoriesSelected.add(ENTERTAINMENT_VIDEO_GAMES_STRING)
        }
        intent.putExtra("categories", categoriesSelected)
        startActivity(intent)
    }
    fun buttonOpenSettingsActivity(view: View) {
        soundPool.play(soundButtonGeneral, leftVolume, rightVolume,0,0,0.8f)
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
    fun buttonToggleVolume(view: View) {
        isApplicationHavingSound = !isApplicationHavingSound
        applicationSound()
        soundPool.play(soundButtonGeneral, leftVolume, rightVolume,0,0,0.8f)
    }


    fun applicationSound(){

        if(isApplicationHavingSound == true){
            leftVolume=1f
            rightVolume=1f
            floatingActionButtonVolume.setImageResource(R.drawable.volume_on_icon)
            MusicBackgroundHandler.playSoundFile(this, null, true, isApplicationHavingSound)
        }else if(isApplicationHavingSound == false){
            leftVolume=0f
            rightVolume=0f
            MusicBackgroundHandler.pauseSound()
            floatingActionButtonVolume.setImageResource(R.drawable.volume_off_icon)
        }
    }

    fun buttonClicked(view: View?) {
        when (view?.id) {

            R.id.button_c_general_knowledge -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,0.8f)
                if (checkedGeneralKnowledge == true) {
                    categoriesSelected.remove(GENERAL_KNOWLEDGE_STRING)
                    imageView_check_general_knowledge.visibility= View.INVISIBLE
                    checkedGeneralKnowledge = !checkedGeneralKnowledge

                } else if (checkedGeneralKnowledge == false) {
                    categoriesSelected.add(GENERAL_KNOWLEDGE_STRING)
                    imageView_check_general_knowledge.visibility= View.VISIBLE
                    checkedGeneralKnowledge = !checkedGeneralKnowledge
                }
            }
            R.id.button_c_ent_books -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,0.8f)
                if (checkedBooks == true) {
                    categoriesSelected.remove(ENTERTAINMENT_BOOKS_STRING)
                    imageView_check_books.visibility= View.INVISIBLE
                    checkedBooks = !checkedBooks

                } else if (checkedBooks == false) {
                    categoriesSelected.add(ENTERTAINMENT_BOOKS_STRING)
                    imageView_check_books.visibility= View.VISIBLE
                    checkedBooks = !checkedBooks
                }
            }

            R.id.button_c_ent_film -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,0.8f)
                if (checkedFilms == true) {
                    categoriesSelected.remove(ENTERTAINMENT_FILM_STRING)
                    imageView_check_films.visibility= View.INVISIBLE
                    checkedFilms = !checkedFilms

                } else if (checkedFilms == false) {
                    categoriesSelected.add(ENTERTAINMENT_FILM_STRING)
                    imageView_check_films.visibility= View.VISIBLE
                    checkedFilms = !checkedFilms
                }
            }

            R.id.button_c_ent_music -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,0.8f)
                if (checkedMusic == true) {
                    categoriesSelected.remove(ENTERTAINMENT_MUSIC_STRING)
                    imageView_check_music.visibility= View.INVISIBLE
                    checkedMusic = !checkedMusic

                } else if (checkedMusic == false) {
                    categoriesSelected.add(ENTERTAINMENT_MUSIC_STRING)
                    imageView_check_music.visibility= View.VISIBLE
                    checkedMusic = !checkedMusic
                }
            }

            R.id.button_c_ent_musical_theaters -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedMusicalTheaters == true) {
                    categoriesSelected.remove(ENTERTAINMENT_MUSICAL_THEATERS_STRING)
                    imageView_check_musical_theaters.visibility= View.INVISIBLE
                    checkedMusicalTheaters = !checkedMusicalTheaters

                } else if (checkedMusicalTheaters == false) {
                    categoriesSelected.add(ENTERTAINMENT_MUSICAL_THEATERS_STRING)
                    imageView_check_musical_theaters.visibility= View.VISIBLE
                    checkedMusicalTheaters = !checkedMusicalTheaters
                }
            }

            R.id.button_c_ent_television -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedTelevision == true) {
                    categoriesSelected.remove(ENTERTAINMENT_TELEVISION_STRING)
                    imageView_check_television.visibility= View.INVISIBLE
                    checkedTelevision = !checkedTelevision

                } else if (checkedTelevision == false) {
                    categoriesSelected.add(ENTERTAINMENT_TELEVISION_STRING)
                    imageView_check_television.visibility= View.VISIBLE
                    checkedTelevision = !checkedTelevision
                }
            }

            R.id.button_c_ent_video_games -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedVideoGames == true) {
                    categoriesSelected.remove(ENTERTAINMENT_VIDEO_GAMES_STRING)
                    imageView_check_video_games.visibility= View.INVISIBLE
                    checkedVideoGames = !checkedVideoGames

                } else if (checkedVideoGames == false) {
                    categoriesSelected.add(ENTERTAINMENT_VIDEO_GAMES_STRING)
                    imageView_check_video_games.visibility= View.VISIBLE
                    checkedVideoGames = !checkedVideoGames
                }
            }

            R.id.button_c_ent_board_games -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedBoardGames == true) {
                    categoriesSelected.remove(ENTERTAINMENT_BOARD_GAMES_STRING)
                    imageView_check_board_games.visibility= View.INVISIBLE
                    checkedBoardGames = !checkedBoardGames

                } else if (checkedBoardGames == false) {
                    categoriesSelected.add(ENTERTAINMENT_BOARD_GAMES_STRING)
                    imageView_check_board_games.visibility= View.VISIBLE
                    checkedBoardGames = !checkedBoardGames
                }
            }

            R.id.button_c_science_nature -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedScienceNature == true) {
                    categoriesSelected.remove(SCIENCE_NATURE_STRING)
                    imageView_check_science_nature.visibility= View.INVISIBLE
                    checkedScienceNature = !checkedScienceNature

                } else if (checkedScienceNature == false) {
                    categoriesSelected.add(SCIENCE_NATURE_STRING)
                    imageView_check_science_nature.visibility= View.VISIBLE
                    checkedScienceNature = !checkedScienceNature
                }
            }
            R.id.button_c_science_computers -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedScienceComputers == true) {
                    categoriesSelected.remove(SCIENCE_COMPUTERS_STRING)
                    imageView_check_science_computers.visibility= View.INVISIBLE
                    checkedScienceComputers = !checkedScienceComputers

                } else if (checkedScienceComputers == false) {
                    categoriesSelected.add(SCIENCE_COMPUTERS_STRING)
                    imageView_check_science_computers.visibility= View.VISIBLE
                    checkedScienceComputers = !checkedScienceComputers
                }
            }

            R.id.button_c_science_mathematics -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedScienceMathematics == true) {
                    categoriesSelected.remove(SCIENCE_MATHEMATICS_STRING)
                    imageView_check_science_mathematics.visibility= View.INVISIBLE
                    checkedScienceMathematics = !checkedScienceMathematics

                } else if (checkedScienceMathematics == false) {
                    categoriesSelected.add(SCIENCE_MATHEMATICS_STRING)
                    imageView_check_science_mathematics.visibility= View.VISIBLE
                    checkedScienceMathematics = !checkedScienceMathematics
                }
            }

            R.id.button_c_mythology -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedMythology == true) {
                    categoriesSelected.remove(MYTHOLOGY_STRING)
                    imageView_check_mythology.visibility= View.INVISIBLE
                    checkedMythology = !checkedMythology

                } else if (checkedMythology == false) {
                    categoriesSelected.add(MYTHOLOGY_STRING)
                    imageView_check_mythology.visibility= View.VISIBLE
                    checkedMythology = !checkedMythology
                }
            }

            R.id.button_c_sports -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedSports == true) {
                    categoriesSelected.remove(SPORTS_STRING)
                    imageView_check_sports.visibility= View.INVISIBLE
                    checkedSports = !checkedSports

                } else if (checkedSports == false) {
                    categoriesSelected.add(SPORTS_STRING)
                    imageView_check_sports.visibility= View.VISIBLE
                    checkedSports = !checkedSports
                }
            }

            R.id.button_c_geography -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedGeography == true) {
                    categoriesSelected.remove(GEOGRAPHY_STRING)
                    imageView_check_geography.visibility= View.INVISIBLE
                    checkedGeography = !checkedGeography

                } else if (checkedGeography == false) {
                    categoriesSelected.add(GEOGRAPHY_STRING)
                    imageView_check_geography.visibility= View.VISIBLE
                    checkedGeography = !checkedGeography
                }
            }

            R.id.button_c_history -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedHistory == true) {
                    categoriesSelected.remove(HISTORY_STRING)
                    imageView_check_history.visibility= View.INVISIBLE
                    checkedHistory = !checkedHistory

                } else if (checkedHistory == false) {
                    categoriesSelected.add(HISTORY_STRING)
                    imageView_check_history.visibility= View.VISIBLE
                    checkedHistory = !checkedHistory
                }
            }

            R.id.button_c_politics -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedPolitics == true) {
                    categoriesSelected.remove(POLITICS_STRING)
                    imageView_check_politics.visibility= View.INVISIBLE
                    checkedPolitics = !checkedPolitics

                } else if (checkedPolitics == false) {
                    categoriesSelected.add(POLITICS_STRING)
                    imageView_check_politics.visibility= View.VISIBLE
                    checkedPolitics = !checkedPolitics
                }
            }

            R.id.button_c_art -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedArts == true) {
                    categoriesSelected.remove(ART_STRING)
                    imageView_check_art.visibility= View.INVISIBLE
                    checkedArts = !checkedArts

                } else if (checkedArts == false) {
                    categoriesSelected.add(ART_STRING)
                    imageView_check_art.visibility= View.VISIBLE
                    checkedArts = !checkedArts
                }
            }

            R.id.button_c_celebrities -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedCelebrities == true) {
                    categoriesSelected.remove(CELEBRITIES_STRING)
                    imageView_check_celebrities.visibility= View.INVISIBLE
                    checkedCelebrities = !checkedCelebrities

                } else if (checkedCelebrities == false) {
                    categoriesSelected.add(CELEBRITIES_STRING)
                    imageView_check_celebrities.visibility= View.VISIBLE

                    checkedCelebrities = !checkedCelebrities
                }
            }
            R.id.button_c_animals -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedAnimals == true) {
                    categoriesSelected.remove(ANIMALS_STRING)
                    imageView_check_animals.visibility= View.INVISIBLE

                    checkedAnimals = !checkedAnimals

                } else if (checkedAnimals == false) {
                    categoriesSelected.add(ANIMALS_STRING)
                    imageView_check_animals.visibility= View.VISIBLE

                    checkedAnimals = !checkedAnimals
                }
            }

            R.id.button_c_vehicles -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedVehicles == true) {
                    categoriesSelected.remove(VEHICLES_STRING)
                    imageView_check_vehicles.visibility= View.INVISIBLE
                    checkedVehicles = !checkedVehicles

                } else if (checkedVehicles == false) {
                    categoriesSelected.add(VEHICLES_STRING)
                    imageView_check_vehicles.visibility= View.VISIBLE
                    checkedVehicles = !checkedVehicles
                }
            }

            R.id.button_c_ent_comics -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedComics == true) {
                    categoriesSelected.remove(ENTERTAINMENT_COMICS_STRING)
                    imageView_check_comics.visibility= View.INVISIBLE
                    checkedComics = !checkedComics

                } else if (checkedComics == false) {
                    categoriesSelected.add(ENTERTAINMENT_COMICS_STRING)
                    imageView_check_comics.visibility= View.VISIBLE
                    checkedComics = !checkedComics
                }
            }

            R.id.button_c_science_gadgets -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedScienceGadgets == true) {
                    categoriesSelected.remove(SCIENCE_GADGETS_STRING)
                    imageView_check_science_gadgets.visibility= View.INVISIBLE
                    checkedScienceGadgets = !checkedScienceGadgets

                } else if (checkedScienceGadgets == false) {
                    categoriesSelected.add(SCIENCE_GADGETS_STRING)
                    imageView_check_science_gadgets.visibility= View.VISIBLE
                    checkedScienceGadgets = !checkedScienceGadgets
                }
            }

            R.id.button_c_ent_anime_manga -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedJapaneseAnimeManga == true) {
                    categoriesSelected.remove(ENTERTAINMENT_JAPANESE_ANIME_MANGA_STRING)
                    imageView_check_anime_manga.visibility= View.INVISIBLE
                    checkedJapaneseAnimeManga = !checkedJapaneseAnimeManga

                } else if (checkedJapaneseAnimeManga == false) {
                    categoriesSelected.add(ENTERTAINMENT_JAPANESE_ANIME_MANGA_STRING)
                    imageView_check_anime_manga.visibility= View.VISIBLE
                    checkedJapaneseAnimeManga = !checkedJapaneseAnimeManga
                }
            }

            R.id.button_c_ent_cartoon_animation -> {
                soundPool.play(soundButtonCategories, leftVolume, rightVolume,0,0,1f)
                if (checkedCartoonAnimations == true) {
                    categoriesSelected.remove(ENTERTAINMENT_CARTOON_ANIMATIONS_STRING)
                    imageView_check_cartoon_animation.visibility= View.INVISIBLE
                    checkedCartoonAnimations = !checkedCartoonAnimations

                } else if (checkedCartoonAnimations == false) {
                    categoriesSelected.add(ENTERTAINMENT_CARTOON_ANIMATIONS_STRING)
                    imageView_check_cartoon_animation.visibility= View.VISIBLE
                    checkedCartoonAnimations = !checkedCartoonAnimations
                }
            }

            else -> {
            }
        }
    }

    private fun reorderButtons() {
        var allButtons: MutableList<Button> = ArrayList()
        for (childView in linear_layout_all_categories.children) {
            if (childView is Button) {
                allButtons.add(childView)
            }
        }
        allButtons.sortBy { it.text.toString() }
        linear_layout_all_categories.removeAllViews()
        for (c_button in allButtons) {
            linear_layout_all_categories.addView(c_button)
        }
    }

    private fun showCheckedTextImage(){
        if(checkedAnimals == true){
            imageView_check_animals.visibility = View.VISIBLE
            categoriesSelected.add(ANIMALS_STRING)
        }else{
            imageView_check_animals.visibility = View.INVISIBLE
        }

        if(checkedArts == true){
            imageView_check_art.visibility = View.VISIBLE
            categoriesSelected.add(ART_STRING)
        }else{
            imageView_check_art.visibility = View.INVISIBLE
        }

        if(checkedBoardGames == true){
            imageView_check_board_games.visibility = View.VISIBLE
            categoriesSelected.add(ENTERTAINMENT_BOARD_GAMES_STRING)
        }else{
            imageView_check_board_games.visibility = View.INVISIBLE
        }

        if(checkedBooks == true){
            imageView_check_books.visibility = View.VISIBLE
            categoriesSelected.add(ENTERTAINMENT_BOOKS_STRING)
        }else{
            imageView_check_books.visibility = View.INVISIBLE
        }

        if(checkedCartoonAnimations == true){
            imageView_check_cartoon_animation.visibility = View.VISIBLE
            categoriesSelected.add(ENTERTAINMENT_CARTOON_ANIMATIONS_STRING)
        }else{
            imageView_check_cartoon_animation.visibility = View.INVISIBLE
        }

        if(checkedCelebrities == true){
            imageView_check_celebrities.visibility = View.VISIBLE
            categoriesSelected.add(CELEBRITIES_STRING)
        }else{
            imageView_check_celebrities.visibility = View.INVISIBLE
        }

        if(checkedComics== true){
            imageView_check_comics.visibility = View.VISIBLE
            categoriesSelected.add(ENTERTAINMENT_COMICS_STRING)
        }else{
            imageView_check_comics.visibility = View.INVISIBLE
        }

        if(checkedFilms == true){
            imageView_check_films.visibility = View.VISIBLE
            categoriesSelected.add(ENTERTAINMENT_FILM_STRING)
        }else{
            imageView_check_films.visibility = View.INVISIBLE
        }

        if(checkedGeneralKnowledge == true){
            imageView_check_general_knowledge.visibility = View.VISIBLE
            categoriesSelected.add(GENERAL_KNOWLEDGE_STRING)
        }else{
            imageView_check_general_knowledge.visibility = View.INVISIBLE
        }

        if(checkedGeography == true){
            imageView_check_geography.visibility = View.VISIBLE
            categoriesSelected.add(GEOGRAPHY_STRING)
        }else{
            imageView_check_geography.visibility = View.INVISIBLE
        }

        if(checkedHistory == true){
            imageView_check_history.visibility = View.VISIBLE
            categoriesSelected.add(HISTORY_STRING)
        }else{
            imageView_check_history.visibility = View.INVISIBLE
        }

        if(checkedJapaneseAnimeManga == true){
            imageView_check_anime_manga.visibility = View.VISIBLE
            categoriesSelected.add(ENTERTAINMENT_JAPANESE_ANIME_MANGA_STRING)
        }else{
            imageView_check_anime_manga.visibility = View.INVISIBLE
        }

        if(checkedMusic == true){
            imageView_check_music.visibility = View.VISIBLE
            categoriesSelected.add(ENTERTAINMENT_MUSIC_STRING)
        }else{
            imageView_check_music.visibility = View.INVISIBLE
        }

        if(checkedMusicalTheaters == true){
            imageView_check_musical_theaters.visibility = View.VISIBLE
            categoriesSelected.add(ENTERTAINMENT_MUSICAL_THEATERS_STRING)
        }else{
            imageView_check_musical_theaters.visibility = View.INVISIBLE
        }

        if(checkedMythology == true){
            imageView_check_mythology.visibility = View.VISIBLE
            categoriesSelected.add(MYTHOLOGY_STRING)
        }else{
            imageView_check_mythology.visibility = View.INVISIBLE
        }

        if(checkedPolitics == true){
            imageView_check_politics.visibility = View.VISIBLE
            categoriesSelected.add(POLITICS_STRING)
        }else{
            imageView_check_politics.visibility = View.INVISIBLE
        }

        if(checkedScienceComputers == true){
            imageView_check_science_computers.visibility = View.VISIBLE
            categoriesSelected.add(SCIENCE_COMPUTERS_STRING)
        }else{
            imageView_check_science_computers.visibility = View.INVISIBLE
        }

        if(checkedScienceGadgets == true){
            imageView_check_science_gadgets.visibility = View.VISIBLE
            categoriesSelected.add(SCIENCE_GADGETS_STRING)
        }else{
            imageView_check_science_gadgets.visibility = View.INVISIBLE
        }

        if(checkedScienceMathematics == true){
            imageView_check_science_mathematics.visibility = View.VISIBLE
            categoriesSelected.add(SCIENCE_MATHEMATICS_STRING)
        }else{
            imageView_check_science_mathematics.visibility = View.INVISIBLE
        }

        if(checkedScienceNature == true){
            imageView_check_science_nature.visibility = View.VISIBLE
            categoriesSelected.add(SCIENCE_NATURE_STRING)
        }else{
            imageView_check_science_nature.visibility = View.INVISIBLE
        }

        if(checkedSports == true){
            imageView_check_sports.visibility = View.VISIBLE
            categoriesSelected.add(SPORTS_STRING)
        }else{
            imageView_check_sports.visibility = View.INVISIBLE
        }

        if(checkedTelevision == true){
            imageView_check_television.visibility = View.VISIBLE
            categoriesSelected.add(ENTERTAINMENT_TELEVISION_STRING)
        }else{
            imageView_check_television.visibility = View.INVISIBLE
        }

        if(checkedVehicles == true){
            imageView_check_vehicles.visibility = View.VISIBLE
            categoriesSelected.add(VEHICLES_STRING)
        }else{
            imageView_check_vehicles.visibility = View.INVISIBLE
        }

        if(checkedVideoGames == true){
            imageView_check_video_games.visibility = View.VISIBLE
            categoriesSelected.add(ENTERTAINMENT_VIDEO_GAMES_STRING)
        }else{
            imageView_check_video_games.visibility = View.INVISIBLE
        }
    }

    private fun saveCheckedCategoriesPreferences(){
        prefs.setCheckedAnimals(checkedAnimals)
        prefs.setCheckedArt(checkedArts)
        prefs.setCheckedBoardGames(checkedBoardGames)
        prefs.setCheckedBooks(checkedBooks)
        prefs.setCheckedCartoonsAnimation(checkedCartoonAnimations)
        prefs.setCheckedCelebrities(checkedCelebrities)
        prefs.setCheckedComics(checkedComics)
        prefs.setCheckedFilms(checkedFilms)
        prefs.setCheckedGeneralKnowledge(checkedGeneralKnowledge)
        prefs.setCheckedGeography(checkedGeography)
        prefs.setCheckedHistory(checkedHistory)
        prefs.setCheckedAnimeManga(checkedJapaneseAnimeManga)
        prefs.setCheckedMusic(checkedMusic)
        prefs.setCheckedMusicalTheaters(checkedMusicalTheaters)
        prefs.setCheckedMythology(checkedMythology)
        prefs.setCheckedPolitics(checkedPolitics)
        prefs.setCheckedScienceComputers(checkedScienceComputers)
        prefs.setCheckedScienceGadgets(checkedScienceGadgets)
        prefs.setCheckedScienceMathematics(checkedScienceMathematics)
        prefs.setCheckedScienceNature(checkedScienceNature)
        prefs.setCheckedSports(checkedSports)
        prefs.setCheckedTelevision(checkedTelevision)
        prefs.setCheckedVehicles(checkedVehicles)
        prefs.setCheckedVideoGames(checkedVideoGames)
    }
    private fun loadCheckedCategoriesPreferences(){
        checkedAnimals = prefs.getCheckedAnimals()
        checkedArts= prefs.getCheckedArt()
        checkedBoardGames = prefs.getCheckedBoardGames()
        checkedBooks = prefs.getCheckedBooks()
        checkedCartoonAnimations = prefs.getCheckedCartoonsAnimation()
        checkedCelebrities = prefs.getCheckedCelebrities()
        checkedComics = prefs.getCheckedComics()
        checkedFilms = prefs.getCheckedFilms()
        checkedGeneralKnowledge = prefs.getCheckedGeneralKnowledge()
        checkedGeography = prefs.getCheckedGeography()
        checkedHistory = prefs.getCheckedHistory()
        checkedJapaneseAnimeManga = prefs.getCheckedAnimeManga()
        checkedMusic = prefs.getCheckedMusic()
        checkedMusicalTheaters = prefs.getCheckedMusicalTheaters()
        checkedMythology = prefs.getCheckedMythology()
        checkedPolitics = prefs.getCheckedPolitics()
        checkedScienceComputers = prefs.getCheckedScienceComputers()
        checkedScienceGadgets = prefs.getCheckedScienceGadgets()
        checkedScienceMathematics = prefs.getCheckedScienceMathematics()
        checkedScienceNature = prefs.getCheckedScienceNature()
        checkedSports = prefs.getCheckedSports()
        checkedTelevision = prefs.getCheckedTelevision()
        checkedVehicles = prefs.getCheckedVehicles()
        checkedVideoGames = prefs.getCheckedVideoGames()
    }

    override fun onResume() {
        super.onResume()
        categoriesSelected = ArrayList<String>()
        loadCheckedCategoriesPreferences()
        showCheckedTextImage()
        isApplicationHavingSound=prefs.getSoundSettings()
        applicationSound()
        createButtonsSound()
          }

    override fun onStop() {
        super.onStop()
        if(soundPool!=null){
            soundPool.release()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        saveCheckedCategoriesPreferences()
        prefs.setSoundSettings(isApplicationHavingSound)
        MusicBackgroundHandler.pauseSound()
       // soundPool.release()
    }

    private fun startMusicThread(){
        countDownTimer = object : CountDownTimer(500, 250) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                applicationSound()
            }
        }.start()
    }

}