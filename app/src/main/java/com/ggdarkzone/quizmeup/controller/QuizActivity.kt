package com.ggdarkzone.quizmeup.controller

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.media.SoundPool
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.ggdarkzone.quizmeup.R
import com.ggdarkzone.quizmeup.model.Question
import com.ggdarkzone.quizmeup.model.QuizApiResponse
import com.ggdarkzone.quizmeup.model.TokenApiResponse
import com.ggdarkzone.quizmeup.util.*
import kotlinx.android.synthetic.main.activity_quiz.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Time
import java.util.concurrent.TimeUnit


class QuizActivity : AppCompatActivity() {

    private var quizJsonApi: QuizJsonApi? = null
    private var correctAnswer: String = ""
    private var correctAnswerIndex: Int = -1
    private var myCategoryList: ArrayList<String>? = null

    private lateinit var ifEmptyMyCategoryList: ArrayList<String>

    private var countDownTimer: CountDownTimer? = null

    private lateinit var prefs: Prefs

    private var userScore: Long = 0


    private var difficultySelected: String? = null
    private var tokenToSet: String? = null
    private var lastRefreshTimeToken: Long? = 0

    private var currentQuestionScore: Double = 0.0
    private var questionTimer: CountDownTimer? = null
    private var highestScore: Long = 0L

    private var mixedDifficulty: String? = null
    private var decreaseRateDifficulty: Double = 3.33

    private var lives: Int = 3

    private var isApplicationHavingSound = false
    private lateinit var soundPool: SoundPool
    private lateinit var soundPoolTickTack: SoundPool
    private var soundButtonGeneral: Int = 0
    private var soundCorrectAnswer: Int = 0
    private var soundWrongAnswer: Int = 0
    private var soundTickTack: Int = 0
    private var ticktackID: Int = 0
    private var leftVolume: Float = 0f
    private var rightVolume: Float = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        prefs = Prefs(this@QuizActivity)
        tokenToSet = prefs.getValidToken()
        difficultySelected = prefs.getDifficulty()
        lastRefreshTimeToken = prefs.getTokenLastRefresh()
        userScore = 0
        lives = 3
        prefs.setScoreCurrentSession(userScore)
        prefs.setCurrentLivesRemaining(lives)

        myCategoryList = intent.getSerializableExtra("categories") as ArrayList<String>?

        ifEmptyMyCategoryList = ArrayList()

        if (myCategoryList.isNullOrEmpty()) {
            myCategoryList?.add("All")
        } else {
            for (category in myCategoryList!!) {
                ifEmptyMyCategoryList.add(category)
            }
        }

        myCategoryList?.shuffle()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .writeTimeout(30,TimeUnit.SECONDS)
            .build();

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        quizJsonApi = retrofit.create(QuizJsonApi::class.java)
        soundPoolTickTack = Utils.createSoundPoolObjectTickTack()
        soundTickTack = soundPoolTickTack.load(this, R.raw.tick_tack_free, 1)
        getQuestionsWithSetDifficulty()
    }

    fun createButtonsSound() {
        soundPool = Utils.createSoundPoolObject()
        soundButtonGeneral = soundPool.load(this, R.raw.button_click_wet_for_the_rest, 1)
        soundCorrectAnswer = soundPool.load(this, R.raw.correct_answer, 1)
        soundWrongAnswer = soundPool.load(this, R.raw.wrong_louder, 1)

    }

    fun verifyToken() {
        if (tokenToSet.equals("no_token") || tokenToSet.isNullOrEmpty()) {
            getTokenForDifferentQuestions(false)
        } else if (System.currentTimeMillis() - lastRefreshTimeToken!! >= 21600000) {
            getTokenForDifferentQuestions(true)
        } else {
            getQuestions(myCategoryList?.get(0), difficultySelected, tokenToSet)
        }
    }

    fun getQuestionsWithSetDifficulty() {

        cancelCreatedThreadsAndTickTack()
        if(lives<0){
            createAlert("end_current_game_session")
        }
        setProgressBar(true)
        if (difficultySelected.equals("all")) {
            difficultySelected = null
        }
        beautifyButtonsBackground()
        verifyToken()
    }

    fun getTokenForDifferentQuestions(reset: Boolean) {
        checkForInternetConnection()
        var call: Call<TokenApiResponse>? = null
        if (reset == false) {
            call = quizJsonApi?.getToken()
        } else {
            call = quizJsonApi?.getResetToken("reset", tokenToSet)
        }

        call?.enqueue(object : Callback<TokenApiResponse?> {
            override fun onResponse(
                call: Call<TokenApiResponse?>, response: Response<TokenApiResponse?>
            ) {
                val tokenApiResponse: TokenApiResponse? = response.body()
                tokenToSet = tokenApiResponse?.token
                prefs.setTokenLastRefresh(System.currentTimeMillis())
                if (prefs.setValidToken(tokenToSet) == true) {
                    val intent = intent
                    finish()
                    startActivity(intent)
                } else {
                    finish()
                }
            }

            override fun onFailure(call: Call<TokenApiResponse?>, t: Throwable) {
                tokenToSet = null
            }
        })
    }

    fun getQuestions(category: String?, difficulty: String?, token: String?) {

        checkForInternetConnection()
        var localToken: String? = token
        if (token.equals("no_token") || tokenToSet.isNullOrEmpty() || tokenToSet.isNullOrBlank()) {
            getTokenForDifferentQuestions(false)
        }
        var call: Call<QuizApiResponse?>? = null
        when (category) {

            GENERAL_KNOWLEDGE_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 9, difficulty, null, null)
            }
            ENTERTAINMENT_BOOKS_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 10, difficulty, null, localToken)
            }
            ENTERTAINMENT_FILM_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 11, difficulty, null, localToken)
            }
            ENTERTAINMENT_MUSIC_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 12, difficulty, null, localToken)
            }
            ENTERTAINMENT_MUSICAL_THEATERS_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 13, difficulty, null, localToken)
            }
            ENTERTAINMENT_TELEVISION_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 14, difficulty, null, localToken)
            }
            ENTERTAINMENT_VIDEO_GAMES_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 15, difficulty, null, localToken)
            }
            ENTERTAINMENT_BOARD_GAMES_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 16, difficulty, null, localToken)
            }
            SCIENCE_NATURE_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 17, difficulty, null, localToken)
            }
            SCIENCE_COMPUTERS_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 18, difficulty, null, localToken)
            }
            SCIENCE_MATHEMATICS_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 19, difficulty, null, localToken)
            }
            MYTHOLOGY_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 20, difficulty, null, localToken)
            }
            SPORTS_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 21, difficulty, null, localToken)
            }
            GEOGRAPHY_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 22, difficulty, null, localToken)
            }
            HISTORY_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 23, difficulty, null, localToken)
            }
            POLITICS_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 24, difficulty, null, localToken)
            }
            ART_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 25, difficulty, null, localToken)
            }
            CELEBRITIES_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 26, difficulty, null, localToken)
            }
            ANIMALS_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 27, difficulty, null, localToken)
            }
            VEHICLES_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 28, difficulty, null, localToken)
            }
            ENTERTAINMENT_COMICS_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 29, difficulty, null, localToken)
            }
            SCIENCE_GADGETS_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 30, difficulty, null, localToken)
            }
            ENTERTAINMENT_JAPANESE_ANIME_MANGA_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 31, difficulty, null, localToken)
            }
            ENTERTAINMENT_CARTOON_ANIMATIONS_STRING -> {
                call = quizJsonApi?.getAllQuestions(1, 32, difficulty, null, localToken)
            }
            else -> {
                call = quizJsonApi?.getAllQuestions(1, null, null, null, localToken)
            }
        }
        call?.enqueue(object : Callback<QuizApiResponse?> {
            override fun onFailure(call: Call<QuizApiResponse?>, t: Throwable) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<QuizApiResponse?>,
                response: Response<QuizApiResponse?>
            ) {
                val quizApiResponse: QuizApiResponse? = response.body()
                if (quizApiResponse?.response_code == 1) {
                    finish()
                } else if (quizApiResponse?.response_code == 2) {
                    finish()
                } else if (quizApiResponse?.response_code == 3) {
                    getTokenForDifferentQuestions(false)
                } else if (quizApiResponse?.response_code == 4) {
                    myCategoryList?.remove(category)
                    if (myCategoryList?.size == 0) {
                        for (category in ifEmptyMyCategoryList) {
                            myCategoryList?.add(category)
                        }
                        createAlert("no_more_questions")
                    } else {

                        myCategoryList?.shuffle()
                        getQuestionsWithSetDifficulty()
                    }

                } else if (quizApiResponse?.response_code == 0) {
                    val questionsResponse: List<Question?>? = quizApiResponse?.results
                    if (questionsResponse != null) {
                        for (question: Question? in questionsResponse) {
                            //the data returned is encoded in base64
                            if (question != null) {
                                beautifyButtonsBackground()
                                val allAnswers: MutableList<String> = ArrayList()
                                val questionCategoryString =
                                    Utils.decodeFromBase64toString(question.category)
                                textView_question_category.text = category
                                val questionDifficultyString =
                                    Utils.decodeFromBase64toString(question.difficulty)
                                mixedDifficulty = questionDifficultyString
                                textView_difficulty.text =
                                    questionDifficultyString.substring(0, 1).toUpperCase()


                                val questionString =
                                    Utils.decodeFromBase64toString(question.questionText)
                                correctAnswer =
                                    Utils.decodeFromBase64toString(question.correct_answer)
                                val firstIncorrectAnswer =
                                    Utils.decodeFromBase64toString(question.incorrect_answers?.get(0))

                                if (question.incorrect_answers?.size == 1) {
                                    displayUITwoOptionQuestion(true)
                                    allAnswers.add(correctAnswer)
                                    allAnswers.add(firstIncorrectAnswer)

                                    allAnswers.shuffle()
                                    for (i in allAnswers.indices) {
                                        if (allAnswers[i] == correctAnswer) correctAnswerIndex = i
                                    }
                                    button_A.setText(allAnswers[0]).toString()
                                    button_B.setText(allAnswers[1]).toString()

                                } else if (question.incorrect_answers?.size == 3) {
                                    displayUITwoOptionQuestion(false)
                                    val secondIncorrectAnswer =
                                        Utils.decodeFromBase64toString(question.incorrect_answers[1])
                                    val thirdIncorrectAnswer =
                                        Utils.decodeFromBase64toString(question.incorrect_answers[2])
                                    allAnswers.add(correctAnswer)
                                    allAnswers.add(firstIncorrectAnswer)
                                    allAnswers.add(secondIncorrectAnswer)
                                    allAnswers.add(thirdIncorrectAnswer)

                                    allAnswers.shuffle()
                                    for (i in allAnswers.indices) {
                                        if (allAnswers[i] == correctAnswer) correctAnswerIndex = i
                                    }
                                    button_A.setText(allAnswers[0]).toString()
                                    button_B.setText(allAnswers[1]).toString()
                                    button_C.setText(allAnswers[2]).toString()
                                    button_D.setText(allAnswers[3]).toString()


                                }

                                questionText_textView.setText(questionString).toString()
                                setProgressBar(false)
                                getCurrentQuestionScore()
                                disableButtons(false)

                            } else questionText_textView.setText(getString(R.string.QuestionTextNull))
                                .toString()


                        }
//
                    }
                }
            }

        })
        myCategoryList?.shuffle()

    }


    fun answerAClicked(v: View) {
        specifyTheRightAndWrongAnswer(0)
        disableButtons(true)
        nextQuestion()
    }

    fun answerBClicked(v: View) {
        specifyTheRightAndWrongAnswer(1)
        disableButtons(true)
        nextQuestion()
    }

    fun answerCClicked(v: View) {
        specifyTheRightAndWrongAnswer(2)
        disableButtons(true)
        nextQuestion()
    }

    fun answerDClicked(v: View) {
        specifyTheRightAndWrongAnswer(3)
        disableButtons(true)
        nextQuestion()
    }

    fun backButtonClicked(v: View) {
        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
        onBackPressed()
    }

    private fun calculateScore() {
        when (mixedDifficulty) {
            MY_DIFFICULTY_EASY -> {
                if (currentQuestionScore > 100) {
                    currentQuestionScore = 100.0
                }
                userScore += currentQuestionScore.toInt()
            }
            MY_DIFFICULTY_MEDIUM -> {
                if (currentQuestionScore > 200) {
                    currentQuestionScore = 200.0
                }
                userScore += currentQuestionScore.toInt()
            }
            MY_DIFFICULTY_HARD -> {
                if (currentQuestionScore > 300) {
                    currentQuestionScore = 300.0
                }
                userScore += currentQuestionScore.toInt()
            }

        }
    }

    private fun specifyTheRightAndWrongAnswer(answerProvidedIndex: Int) {
        val gradientDrawableCorrect =
            Utils.gradientForBackground(this@QuizActivity, "#22f430", "#22f430")
        val gradientDrawableIncorrect =
            Utils.gradientForBackground(this@QuizActivity, "#ff0e4b", "#ff0e4b")

        if (answerProvidedIndex == correctAnswerIndex) {
            soundPool.play(soundCorrectAnswer, leftVolume, rightVolume, 0, 0, 1f)
            questionTimer?.cancel()
            soundPoolTickTack.stop(ticktackID)
            calculateScore()
            valueScore_textView.text = userScore.toString()
            shakeAnimation()
        } else {
            soundPool.play(soundWrongAnswer, leftVolume, rightVolume, 0, 0, 1f)

            fadeView()
            questionTimer?.cancel()
            soundPoolTickTack.stop(ticktackID)
            when (answerProvidedIndex) {
                0 -> {
                    button_A.background = gradientDrawableIncorrect
                }
                1 -> {
                    button_B.background = gradientDrawableIncorrect
                }
                2 -> {
                    button_C.background = gradientDrawableIncorrect
                }
                3 -> {
                    button_D.background = gradientDrawableIncorrect
                }
            }
            lives -= 1
            number_remaining_lives.text = lives.toString()

        }
        when (correctAnswerIndex) {
            0 -> {
                button_A.background = gradientDrawableCorrect

            }
            1 -> {
                button_B.background = gradientDrawableCorrect

            }
            2 -> {
                button_C.background = gradientDrawableCorrect

            }
            3 -> {
                button_D.background = gradientDrawableCorrect

            }
        }
    }

    private fun displayUITwoOptionQuestion(twoAnswerQuestion: Boolean) {
        if (twoAnswerQuestion) {
            button_C.visibility = View.INVISIBLE
            button_D.visibility = View.INVISIBLE
        } else if (twoAnswerQuestion == false) {
            button_C.visibility = View.VISIBLE
            button_D.visibility = View.VISIBLE
        }
    }

    private fun beautifyButtonsBackground() {
        number_remaining_lives.text = lives.toString()
        valueScore_textView.text = userScore.toString()
        val gradientDrowable: GradientDrawable =
            Utils.gradientForBackground(this@QuizActivity, "#ffb400", "#ff5a00")
        cardView.background = gradientDrowable
        button_A.background = gradientDrowable
        button_B.background = gradientDrowable
        button_C.background = gradientDrowable
        ViewCompat.setBackground(button_D, gradientDrowable)
    }

    private fun checkForInternetConnection(): Boolean {
        val connectivityMannager: ConnectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityMannager.activeNetworkInfo
        val isDeviceCoonectedToInternet = networkInfo != null && networkInfo.isConnectedOrConnecting
        if (isDeviceCoonectedToInternet) {
            return true
        } else {
            createAlert("internet_connection")
            return false
        }

    }


    private fun disableButtons(disable: Boolean) {
        if (disable == true) {
            button_A.isClickable = false
            button_B.isClickable = false
            button_C.isClickable = false
            button_D.isClickable = false
        } else if (disable == false) {
            button_A.isClickable = true
            button_B.isClickable = true
            button_C.isClickable = true
            button_D.isClickable = true
        }

    }

    private fun shakeAnimation() {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake_animation)
        cardView.startAnimation(shake)
        shake.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                val gradientDrowable: GradientDrawable =
                    Utils.gradientForBackground(this@QuizActivity, "#00ff00", "#00ff00")
                cardView.background = gradientDrowable
            }

            override fun onAnimationEnd(animation: Animation) {
                questionText_textView.setTextColor(
                    ContextCompat.getColor(
                        this@QuizActivity,
                        R.color.colorCustomBlue
                    )
                )
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun fadeView() {
        val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
        alphaAnimation.duration = 350
        alphaAnimation.repeatCount = 2
        alphaAnimation.repeatMode = Animation.REVERSE
        cardView.startAnimation(alphaAnimation)
        alphaAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                val gradientDrowable: GradientDrawable =
                    Utils.gradientForBackground(this@QuizActivity, "#FF0000", "#FF0000")
                cardView.background = gradientDrowable
                questionText_textView.setTextColor(Color.WHITE)
            }

            override fun onAnimationEnd(animation: Animation) {
                questionText_textView.setTextColor(
                    ContextCompat.getColor(
                        this@QuizActivity,
                        R.color.colorCustomBlue
                    )
                )
                if (lives == 0) {
                    createAlert("end_current_game_session")
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun nextQuestion() {

        countDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                textView_countDownTimer.setText("Next question in: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {

                textView_countDownTimer.setText("")
                getQuestionsWithSetDifficulty()
            }
        }.start()

    }

    private fun getCurrentQuestionScore() {
        countDownTimer?.cancel()
        questionTimer?.cancel()
        initializeValues()
        var timer = QUESTION_TIMER
        progressBar.max = timer
        progressBar.progress = timer + 1
        ticktackID =
            soundPoolTickTack.play(
                soundTickTack,
                leftVolume / 1.45f,
                rightVolume / 1.45f,
                0,
                -1,
                1f
            )
        questionTimer = object : CountDownTimer(16000, 1000) {
            override fun onTick(p0: Long) {
                currentQuestionScore -= decreaseRateDifficulty
                updateUIElements(timer)
                timer -= 1
            }

            override fun onFinish() {
                soundPoolTickTack.stop(ticktackID)
                progressBar.progress = 0
                timer_textView.setText("00:00")
            }
        }.start()
    }

    private fun initializeValues() {
        when (mixedDifficulty) {
            MY_DIFFICULTY_EASY -> {
                currentQuestionScore = QUESTION_MAX_VALUE_EASY
                decreaseRateDifficulty = 6.33
            }
            MY_DIFFICULTY_MEDIUM -> {
                currentQuestionScore = QUESTION_MAX_VALUE_MEDIUM
                decreaseRateDifficulty = 11.0
            }
            MY_DIFFICULTY_HARD -> {
                currentQuestionScore = QUESTION_MAX_VALUE_HARD
                decreaseRateDifficulty = 15.5
            }
            else -> {
            }
        }

    }

    private fun updateUIElements(timer: Int) {
        if (timer < 10) {
            timer_textView.text = "00:0" + timer.toString()
        } else {
            timer_textView.text = "00:" + timer.toString()
        }
        progressBar.progress = timer


    }


    private fun setProgressBar(show: Boolean) {
        if (show) {
            linearProgressBar.visibility = View.VISIBLE
            progressBarAnimation.visibility = View.VISIBLE//To show Progress Bar
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (!show) {
            linearProgressBar.visibility = View.GONE
            progressBarAnimation.visibility = View.GONE//To hide progress bar
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun createAlert(type: String) {
        when (type) {
            "back_button_alert" -> {
                val factory: LayoutInflater = LayoutInflater.from(this)
                val customDialogView = factory.inflate(R.layout.alert_dialog_custom, null)
                val customDialog: androidx.appcompat.app.AlertDialog =
                    androidx.appcompat.app.AlertDialog.Builder(this).create()
                customDialog.setView(customDialogView)
                customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                customDialogView.findViewById<TextView>(R.id.textView_customAlertTitle)
                    .setText("Are you sure you want to quit?")
                customDialogView.findViewById<TextView>(R.id.textView_customAlertMessage)
                    .setText("The current score progress will be lost.\nCurrent score: $userScore")
                customDialogView.findViewById<Button>(R.id.button_cancel).setText("Cancel")
                customDialogView.findViewById<Button>(R.id.button_okay).setText("Quit")

                customDialogView.findViewById<Button>(R.id.button_okay)
                    .setOnClickListener(View.OnClickListener {
                        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                        soundPoolTickTack.stop(ticktackID)
                        countDownTimer?.cancel()
                        questionTimer?.cancel()
                        val intent = Intent(this, SelectQuizCategoriesActivity::class.java)
                        startActivity(intent)
                        customDialog.dismiss()
                    })
                customDialogView.findViewById<Button>(R.id.button_cancel)
                    .setOnClickListener(View.OnClickListener {
                        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                        customDialog.dismiss()
                    })
                customDialog.show()
            }
            "internet_connection" -> {
                textView_progress_bar_loading.setText("Checking for Internet Connection...")
                val factory: LayoutInflater = LayoutInflater.from(this)
                val customDialogView = factory.inflate(R.layout.alert_dialog_custom, null)
                val customDialog: androidx.appcompat.app.AlertDialog =
                    androidx.appcompat.app.AlertDialog.Builder(this).create()
                customDialog.setView(customDialogView)
                customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                customDialogView.findViewById<TextView>(R.id.textView_customAlertTitle)
                    .setText("Network Error!")
                customDialogView.findViewById<TextView>(R.id.textView_customAlertMessage)
                    .setText("Please check for internet connection.")
                customDialogView.findViewById<Button>(R.id.button_cancel)
                    .setText("Turn on internet")
                customDialogView.findViewById<Button>(R.id.button_okay).setText("Retry")

                customDialogView.findViewById<Button>(R.id.button_okay)
                    .setOnClickListener(View.OnClickListener {
                        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                        cancelCreatedThreadsAndTickTack()
                        textView_progress_bar_loading.setText(R.string.loading_progress_bar_new_question)
                        getQuestionsWithSetDifficulty()
                        customDialog.dismiss()
                    })
                customDialogView.findViewById<Button>(R.id.button_cancel)
                    .setOnClickListener(View.OnClickListener {
                        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                        cancelCreatedThreadsAndTickTack()
                        startActivity(Intent(Settings.ACTION_SETTINGS))
                        customDialog.dismiss()
                    })
                customDialog.show()
                customDialog.setCancelable(false)
                customDialog.setCanceledOnTouchOutside(false)
            }
            "no_more_questions" -> {
                textView_progress_bar_loading.setText(R.string.loading_progress_bar_no_more_quesitons)
                countDownTimer?.cancel()
                val factory: LayoutInflater = LayoutInflater.from(this)
                val customDialogView = factory.inflate(R.layout.alert_dialog_custom, null)
                val customDialog: androidx.appcompat.app.AlertDialog =
                    androidx.appcompat.app.AlertDialog.Builder(this).create()
                customDialog.setView(customDialogView)
                customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                customDialogView.findViewById<TextView>(R.id.textView_customAlertTitle)
                    .setText("No new questions!")
                customDialogView.findViewById<TextView>(R.id.textView_customAlertMessage)
                    .setText("Please select more categories or select a different difficulty level.")
                customDialogView.findViewById<Button>(R.id.button_cancel).setText("Reset questions")
                customDialogView.findViewById<Button>(R.id.button_okay).setText("Back")

                customDialogView.findViewById<Button>(R.id.button_okay)
                    .setOnClickListener(View.OnClickListener {
                        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                        textView_progress_bar_loading.setText(R.string.loading_progress_bar_new_question)
                        cancelCreatedThreadsAndTickTack()
                        prefs.setHighestScore(userScore)
                        var intent = Intent(this, SelectQuizCategoriesActivity::class.java)
                        startActivity(intent)
                        customDialog.dismiss()
                    })
                customDialogView.findViewById<Button>(R.id.button_cancel)
                    .setOnClickListener(View.OnClickListener {
                        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                        userScore = 0
                        lives = 3
                        prefs.setHighestScore(userScore)
                        textView_progress_bar_loading.setText(R.string.loading_progress_bar_new_question)
                        getTokenForDifferentQuestions(true)
                        customDialog.dismiss()
                    })
                customDialog.show()
                customDialog.setCancelable(false)
                customDialog.setCanceledOnTouchOutside(false)
            }
            "end_current_game_session" -> {
                cancelCreatedThreadsAndTickTack()
                val factory: LayoutInflater = LayoutInflater.from(this)
                var customDialogView = factory.inflate(R.layout.alert_dialog_custom, null)
                if (userScore > prefs.getHighScore()) {
                    customDialogView = factory.inflate(R.layout.alert_dialog_new_highscore, null)
                } else {
                    customDialogView = factory.inflate(R.layout.alert_dialog_custom, null)
                }
                val customDialog: androidx.appcompat.app.AlertDialog =
                    androidx.appcompat.app.AlertDialog.Builder(this).create()
                customDialog.setView(customDialogView)
                customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                if (userScore > prefs.getHighScore()) {
                    customDialogView.findViewById<TextView>(R.id.textView_customAlertMessage)
                        .setText("Congratulations!!!\nYour new highest score is: $userScore")
                } else {
                    customDialogView.findViewById<TextView>(R.id.textView_customAlertMessage)
                        .setText("Score this round: $userScore")

                }
                customDialogView.findViewById<TextView>(R.id.textView_customAlertTitle)
                    .setText("Game over!")
                customDialogView.findViewById<Button>(R.id.button_cancel).setText("Back")
                customDialogView.findViewById<Button>(R.id.button_okay).setText("New round")

                customDialogView.findViewById<Button>(R.id.button_okay)
                    .setOnClickListener(View.OnClickListener {
                        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                        prefs.setHighestScore(userScore)
                        userScore = 0
                        lives = 3
                        highestScore_textView.text = "Highest score: ${prefs.getHighScore()}"
                        textView_countDownTimer.text = ""
                        getQuestionsWithSetDifficulty()
                        customDialog.dismiss()
                    })
                customDialogView.findViewById<Button>(R.id.button_cancel)
                    .setOnClickListener(View.OnClickListener {
                        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
                        var intent = Intent(this, SelectQuizCategoriesActivity::class.java)
                        startActivity(intent)
                        customDialog.dismiss()
                    })
                customDialog.show()
                customDialog.setCancelable(false)
                customDialog.setCanceledOnTouchOutside(false)
            }
        }
    }

    fun cancelCreatedThreadsAndTickTack() {
        soundPoolTickTack.stop(ticktackID)
        countDownTimer?.cancel()
        questionTimer?.cancel()
    }

    override fun onBackPressed() {
        createAlert("back_button_alert")
    }

    fun buttonToggleVolumeQuiz(view: View) {
        isApplicationHavingSound = !isApplicationHavingSound
        applicationSound()
        soundPool.play(soundButtonGeneral, leftVolume, rightVolume, 0, 0, 1f)
    }

    fun applicationSound() {
        if (isApplicationHavingSound == true) {
            soundPoolTickTack.setVolume(ticktackID, 0.69f, 0.69f)
            leftVolume = 1f
            rightVolume = 1f
            MusicBackgroundHandler.playSoundFile(this, null, true, isApplicationHavingSound)
            floatingActionButtonVolumeQuizActivity.setImageResource(R.drawable.volume_on_icon)
        } else if (isApplicationHavingSound == false) {
            soundPoolTickTack.setVolume(ticktackID, 0.0f, 0.0f)
            leftVolume = 0f
            rightVolume = 0f
            MusicBackgroundHandler.pauseSound()
            floatingActionButtonVolumeQuizActivity.setImageResource(R.drawable.volume_off_icon)
        }
    }

    override fun onResume() {
        super.onResume()
        if(lives<0){
            val intent = Intent(this,SelectQuizCategoriesActivity::class.java)
            startActivity(intent)
        }
        isApplicationHavingSound = prefs.getSoundSettings()
        beautifyButtonsBackground()
        highestScore = prefs.getHighScore()
        highestScore_textView.text = "Highest score: $highestScore"
        tokenToSet = prefs.getValidToken()
        userScore = prefs.getScoreCurrentSession()
        lives = prefs.getCurrentLivesRemaining()
        applicationSound()
        checkForInternetConnection()
        createButtonsSound()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onPause() {
        super.onPause()
        prefs.setHighestScore(userScore)
        prefs.setValidToken(tokenToSet)
        prefs.setScoreCurrentSession(userScore)
        prefs.setCurrentLivesRemaining(lives)
        prefs.setApplicationSound(isApplicationHavingSound)
        leftVolume = 0f
        rightVolume = 0f
        soundPoolTickTack.setVolume(ticktackID, 0f, 0f)
        MusicBackgroundHandler.pauseSound()


    }

    override fun onDestroy() {
        super.onDestroy()
        soundPoolTickTack?.release()

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        if(soundPool!=null){
            soundPool.release()
        }

    }
}