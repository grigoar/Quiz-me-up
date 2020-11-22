package com.ggdarkzone.quizmeup.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


class Prefs
    (activity: Activity) {

    private var preferences: SharedPreferences

    init {
        this.preferences = activity.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
    }

    fun setScoreCurrentSession(scoreCurrent: Long){
        preferences.edit().putLong(MY_SCORE_CURRENT_SESSION, scoreCurrent).commit()
    }
    fun getScoreCurrentSession(): Long{
        return preferences.getLong(MY_SCORE_CURRENT_SESSION,0)
    }

    fun setCurrentLivesRemaining(currentLives: Int){
        preferences.edit().putInt(MY_LIVES_CURRENT_SESSION,currentLives).commit()
    }
    fun getCurrentLivesRemaining():Int{
        return preferences.getInt(MY_LIVES_CURRENT_SESSION,0)
    }

    fun setApplicationSound(sound: Boolean){
        preferences.edit().putBoolean(MY_APP_SOUND_SETTINGS, sound).apply()
    }
    fun getApplicationSound(){
        preferences.getBoolean(MY_APP_SOUND_SETTINGS, true)
    }

    fun setHighestScore( score: Long = 0){
        val currentScore: Long = score
        val lastScore: Long = preferences.getLong(MY_HIGH_SCORE, 0)
            if(currentScore > lastScore){
                    preferences.edit()?.putLong(MY_HIGH_SCORE, currentScore)?.apply()
                }
    }

    fun getHighScore(): Long{
        return preferences.getLong(MY_HIGH_SCORE,0)
    }

    fun setState(index: Int){
        preferences.edit()?.putInt(MY_INDEX_STATE, index)?.apply()
    }

    fun getState(): Int?{
        return preferences.getInt(MY_INDEX_STATE,0)
    }

    fun setDificulty(difficulty: String):Boolean?{
        val result:Boolean? = preferences.edit()?.putString(MY_DIFFICULTY_MODE, difficulty)?.commit()
        return result
    }

    fun getDifficulty():String? {
        return preferences.getString(MY_DIFFICULTY_MODE, MY_DIFFICULTY_ALL)
    }

    fun setFirstRun(first_run:Boolean){
        preferences.edit()?.putBoolean(MY_APP_FIRST_RUN, first_run)?.apply()
    }

    fun getFirstRun():Boolean {
        return preferences.getBoolean(MY_APP_FIRST_RUN, false)
    }

    fun setSoundSettings(sound: Boolean){
        preferences.edit().putBoolean(MY_APP_SOUND_SETTINGS, sound).apply()
    }

    fun getSoundSettings(): Boolean{
        return preferences.getBoolean(MY_APP_SOUND_SETTINGS, true)
    }

    fun setValidToken(token: String?):Boolean{
        val result= preferences.edit().putString(MY_USER_TOKEN_VALID, token).commit()
        return result
    }

    fun getValidToken(): String? {
        return preferences.getString(MY_USER_TOKEN_VALID, "no_token")

    }

    fun setTokenLastRefresh(time: Long):Boolean{
        val result =preferences.edit().putLong(MY_TOKEN_LAST_REFRESH_TIME, time).commit()
        return result

    }
    fun getTokenLastRefresh(): Long{
        return preferences.getLong(MY_TOKEN_LAST_REFRESH_TIME, -1L)
    }
    fun getCheckedAnimals():Boolean {
        return preferences.getBoolean(MY_CHECKED_ANIMALS, false)
    }

    fun setCheckedAnimals(checkedAnimal: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_ANIMALS, checkedAnimal).apply()
    }

    fun getCheckedArt():Boolean {
        return preferences.getBoolean(MY_CHECKED_ART, false)
    }

    fun setCheckedArt(checkedArt: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_ART, checkedArt).apply()
    }

    fun getCheckedBoardGames():Boolean {
        return preferences.getBoolean(MY_CHECKED_BOARD_GAMES, false)
    }

    fun setCheckedBoardGames(checkedBoardGames: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_BOARD_GAMES, checkedBoardGames).apply()
    }

    fun getCheckedBooks():Boolean {
        return preferences.getBoolean(MY_CHECKED_BOOKS, false)
    }

    fun setCheckedBooks(checkedBooks: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_BOOKS, checkedBooks).apply()
    }
    fun getCheckedCartoonsAnimation():Boolean {
        return preferences.getBoolean(MY_CHECKED_CARTOON_ANIMATIONS, false)
    }

    fun setCheckedCartoonsAnimation(checkedCartoonsAnimation: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_CARTOON_ANIMATIONS, checkedCartoonsAnimation).apply()
    }

    fun getCheckedCelebrities():Boolean {
        return preferences.getBoolean(MY_CHECKED_CELEBRITIES, false)
    }

    fun setCheckedCelebrities(checkedCelebrities: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_CELEBRITIES, checkedCelebrities).apply()
    }

    fun getCheckedComics():Boolean {
        return preferences.getBoolean(MY_CHECKED_COMICS, false)
    }

    fun setCheckedComics(checkedComics: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_COMICS, checkedComics).apply()
    }
    fun getCheckedFilms():Boolean {
        return preferences.getBoolean(MY_CHECKED_FILMS, false)
    }

    fun setCheckedFilms(checkedFilms: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_FILMS, checkedFilms).apply()
    }
    fun getCheckedGeneralKnowledge():Boolean {
        return preferences.getBoolean(MY_CHECKED_GENERAL_KNOWLEDGE, false)
    }

    fun setCheckedGeneralKnowledge(checkedGeneralKnowledge: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_GENERAL_KNOWLEDGE, checkedGeneralKnowledge).apply()
    }
    fun getCheckedGeography():Boolean {
        return preferences.getBoolean(MY_CHECKED_GEOGRAPHY, false)
    }

    fun setCheckedGeography(checkedGeography: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_GEOGRAPHY, checkedGeography).apply()
    }
    fun getCheckedHistory():Boolean {
        return preferences.getBoolean(MY_CHECKED_HISTORY, false)
    }

    fun setCheckedHistory(checkedHistory: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_HISTORY, checkedHistory).apply()
    }
    fun getCheckedAnimeManga():Boolean {
        return preferences.getBoolean(MY_CHECKED_ANIME_MANGA, false)
    }

    fun setCheckedAnimeManga(checkedAmimeManga: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_ANIME_MANGA, checkedAmimeManga).apply()
    }
    fun getCheckedMusic():Boolean {
        return preferences.getBoolean(MY_CHECKED_MUSIC, false)
    }

    fun setCheckedMusic(checkedMusic: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_MUSIC, checkedMusic).apply()
    }
    fun getCheckedMusicalTheaters():Boolean {
        return preferences.getBoolean(MY_CHECKED_MUSICAL_THEATERS, false)
    }

    fun setCheckedMusicalTheaters(checkedMusicalTheaters: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_MUSICAL_THEATERS, checkedMusicalTheaters).apply()
    }
    fun getCheckedMythology():Boolean {
        return preferences.getBoolean(MY_CHECKED_MYTHOLOGY, false)
    }

    fun setCheckedMythology(checkedMythology: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_MYTHOLOGY, checkedMythology).apply()
    }
    fun getCheckedPolitics():Boolean {
        return preferences.getBoolean(MY_CHECKED_POLITICS, false)
    }

    fun setCheckedPolitics(checkedPolitics: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_POLITICS, checkedPolitics).apply()
    }
    fun getCheckedScienceComputers():Boolean {
        return preferences.getBoolean(MY_CHECKED_SCIENCE_COMPUTERS, false)
    }

    fun setCheckedScienceComputers(checkedScienceComputers: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_SCIENCE_COMPUTERS, checkedScienceComputers).apply()
    }
    fun getCheckedScienceGadgets():Boolean {
        return preferences.getBoolean(MY_CHECKED_SCIENCE_GADGETS, false)
    }

    fun setCheckedScienceGadgets(checkedScienceGadgets: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_SCIENCE_GADGETS, checkedScienceGadgets).apply()
    }
    fun getCheckedScienceMathematics():Boolean {
        return preferences.getBoolean(MY_CHECKED_SCIENCE_MATHEMATICS, false)
    }

    fun setCheckedScienceMathematics(checkedScienceMathematics: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_SCIENCE_MATHEMATICS, checkedScienceMathematics).apply()
    }
    fun getCheckedScienceNature():Boolean {
        return preferences.getBoolean(MY_CHECKED_SCIENCE_NATURE, false)
    }

    fun setCheckedScienceNature(checkedScienceNature: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_SCIENCE_NATURE, checkedScienceNature).apply()
    }
    fun getCheckedSports():Boolean {
        return preferences.getBoolean(MY_CHECKED_SPORTS, false)
    }

    fun setCheckedSports(checkedSports: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_SPORTS, checkedSports).apply()
    }
    fun getCheckedTelevision():Boolean {
        return preferences.getBoolean(MY_CHECKED_TELEVISION, false)
    }

    fun setCheckedTelevision(checkedTelevision: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_TELEVISION, checkedTelevision).apply()
    }
    fun getCheckedVehicles():Boolean {
        return preferences.getBoolean(MY_CHECKED_VEHICLES, false)
    }

    fun setCheckedVehicles(checkedVehicles: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_VEHICLES, checkedVehicles).apply()
    }
    fun getCheckedVideoGames():Boolean {
        return preferences.getBoolean(MY_CHECKED_VIDEO_GAMES, false)
    }

    fun setCheckedVideoGames(checkedVideoGames: Boolean){
        preferences.edit().putBoolean(MY_CHECKED_VIDEO_GAMES, checkedVideoGames).apply()
    }


}