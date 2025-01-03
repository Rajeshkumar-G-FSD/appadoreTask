package com.example.quizgame

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flagquiz.Utils.getTimeDuration
import com.example.flagquiz.Utils.showToast
import com.example.myquiztest.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity(), CountryAdapter.OnItemClickListener{
    private var hourEditText: EditText? = null
    private var hourEditText1: EditText? = null
    private var minuteEditText: EditText? = null
    private var minuteEditText1: EditText? = null
    private var secsEditText: EditText? = null
    private var secsEditText1: EditText? = null
    private var totalHour = ""
    private var totalMinutes = ""
    private var totalSecs = ""
    private var saveBtn: AppCompatButton? = null
    private var scheduleLabelLayout: LinearLayout? = null
    private var timerLayout: LinearLayout? = null
    var scheduleLabel: TextView? = null
    private var tvDisplayTime: TextView? = null
    private var runnable: Runnable? = null
    private var runnables: Runnable? = null
    var handler = Handler()
    var currentIndex = 0
    private var addScore = 0
    private val tvQuestionNumvalue = 0

    //  private List<String> countryNames = new ArrayList<>();
    private var recyclerView: RecyclerView? = null
    private var countryAdapter: CountryAdapter? = null
    private var countries: List<Country>? = null
    var questions: MutableList<Question>? = null
    var newcountries: List<Country> = ArrayList()
    private var question: Question? = null

    // Add countries to the list
    private val addcountries: List<Country>? = null
    private var countDownTimer: CountDownTimer? = null
    var timerTextView: TextView? = null
    var imgFlag: ImageView? = null
    var tvquestion: TextView? = null
    var tvQuestionNum = 0
    var questionAnswerlayout: LinearLayout? = null
    var gameoverLabel: TextView? = null
    private var tvscore: TextView? = null
    private var scoreUpdateLayout: LinearLayout? = null
    var gameOver = 0
    private var sharedPreferences: SharedPreferences? = null

    private val flagquestionCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setUI()



        hourEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed before text change
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed during text change
            }

            override fun afterTextChanged(s: Editable) {
                val input = s.toString()
                if (!input.isEmpty()) {
                    try {
                        val value = input.toInt()
                        if (value < MIN_HOUR_VALUE || value > MAX_HOUR_VALUE) {
                            showToast(this@MainActivity, "Please Enter Correct Hours")
                            //validationTextView.setText("Please enter a number between " + MIN_HOUR_VALUE + " and " + MAX_HOUR_VALUE);
                            //  validationTextView.setVisibility(TextView.VISIBLE);
                            hourEditText!!.setText("")
                            hourEditText!!.hint = "0"
                            hourEditText1!!.setText("")
                            hourEditText1!!.hint = "0"
                        } else {
                            Log.e("checkHour", "Please$s")
                            hourEditText1!!.requestFocus()
                        }
                    } catch (e: NumberFormatException) {
                        Log.e("checkHour", "Invalid")
                        // validationTextView.setText("Invalid number");
                        //  validationTextView.setVisibility(TextView.VISIBLE);
                    }
                } else {
                    Log.e("checkHour", "#####")
                    // validationTextView.setVisibility(TextView.GONE);
                }
            }
        })

        hourEditText1!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (hourEditText!!.text.toString() != "") {
                    val getHour =
                        (hourEditText!!.text.toString() + hourEditText1!!.text.toString()).toInt()
                    totalHour = getHour.toString()
                    if (getHour > MAX_HOURS_VALUE) {
                        hourEditText1!!.setText("")
                        hourEditText1!!.hint = "0"
                        showToast(this@MainActivity, "Please Enter Correct Hours")
                    }
                    minuteEditText!!.requestFocus()
                } else {
                    showToast(this@MainActivity, "Please Enter First Field")
                }
            }
        })

        minuteEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed before text change
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed during text change
                minuteEditText1!!.setText("")
                minuteEditText1!!.hint = "0"
            }

            override fun afterTextChanged(s: Editable) {
                Log.e("checkHour", "@@$s")
                if (hourEditText!!.text.isNotEmpty()) {
                    val input = s.toString()
                    if (!input.isEmpty()) {
                        try {
                            val value = input.toInt()
                            if (value > MIN_MINUTES_VALUE) {
                                showToast(this@MainActivity, "Please Enter Correct Hours")
                                //validationTextView.setText("Please enter a number between " + MIN_HOUR_VALUE + " and " + MAX_HOUR_VALUE);
                                //  validationTextView.setVisibility(TextView.VISIBLE);
                                setMinutesZero()
                            } else {
                                Log.e("checkHour", "Please$s")
                                minuteEditText1!!.requestFocus()
                            }
                        } catch (e: NumberFormatException) {
                            Log.e("checkHour", "Invalid")
                            // validationTextView.setText("Invalid number");
                            //  validationTextView.setVisibility(TextView.VISIBLE);
                        }
                    } else {
                        Log.e("checkHour", "#####")
                        // validationTextView.setVisibility(TextView.GONE);
                    }
                } else {
                    showToast(this@MainActivity, "Please Enter Correct Hours")
                    setMinutesZero()
                }
            }
        })
        minuteEditText1!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (minuteEditText!!.text.toString() != "") {
                    Log.e("targetTime>", "" + minuteEditText!!.text.toString())
                    val getHour =
                        (minuteEditText!!.text.toString() + minuteEditText1!!.text.toString()).toInt()
                    totalMinutes = if (getHour < 10) {
                        "0$getHour".toString()
                    } else {
                        getHour.toString()
                    }

                    secsEditText!!.requestFocus()
                    /*  if(getHour>MAX_HOURS_VALUE){
                        hourEditText1.setText("");
                        hourEditText1.setHint("0");
                        Utils.showToast(MainActivity.this,"Please Enter Correct Hours");
                    }*/
                } else {
                    showToast(this@MainActivity, "Please Enter Minutes Field")
                }
            }
        })
        secsEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed before text change
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed during text change
            }

            override fun afterTextChanged(s: Editable) {
                Log.e("checkHour", "@@$s")
                val input = s.toString()
                secsEditText1!!.requestFocus()
                if (!input.isEmpty()) {
                    try {
                        val value = input.toInt()
                        if (value > MIN_MINUTES_VALUE) {
                            showToast(this@MainActivity, "Please Enter Correct Secs")
                            //validationTextView.setText("Please enter a number between " + MIN_HOUR_VALUE + " and " + MAX_HOUR_VALUE);
                            //  validationTextView.setVisibility(TextView.VISIBLE);
                            setEmptySecs()
                        } else {
                            Log.e("checkHour", "Please$s")
                            secsEditText1!!.requestFocus()
                        }
                    } catch (e: NumberFormatException) {
                        Log.e("checkHour", "Invalid")
                        // validationTextView.setText("Invalid number");
                        //  validationTextView.setVisibility(TextView.VISIBLE);
                    }
                } else {
                    Log.e("checkHour", "#####")
                    // validationTextView.setVisibility(TextView.GONE);
                }
            }
        })
        secsEditText1!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed before text change
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed during text change
            }

            override fun afterTextChanged(s: Editable) {
                val input = s.toString()
                if (!input.isEmpty()) {
                    try {
                        var secs =
                            (secsEditText!!.text.toString() + secsEditText1!!.text.toString()).toInt()
                        if (secs == 0) {
                            secs = 11
                        }
                        totalSecs = secs.toString()
                    } catch (e: NumberFormatException) {
                        // validationTextView.setText("Invalid number");
                        //  validationTextView.setVisibility(TextView.VISIBLE);
                    }
                } else {
                    // validationTextView.setVisibility(TextView.GONE);
                }
            }
        })
        saveBtn!!.setOnClickListener {
            if (hourEditText!!.text.toString() != "") {
                updateTime()
                //
                val targetTime =
                    "$totalHour:$totalMinutes:$totalSecs" // Example target time in HH:mm:ss format
                Log.e(
                    "HourMinSecs",
                    "totalHour :" + totalHour + "totalMinutes :" + totalMinutes + "totalSecs :" + totalSecs
                )
                val result = getTimeDuration(targetTime)
                val showTime = result.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()


                val displayStartTime =
                    showTime[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                showToast(
                    this@MainActivity,
                    "Challenge will start in " + displayStartTime[1] + ":" + displayStartTime[2] + ":" + displayStartTime[3]
                )
                //
                val handler = Handler(Looper.getMainLooper())

                runnable = object : Runnable {
                    override fun run() {
                        updateTime()
                        handler.postDelayed(this, 1000)
                    }
                }

                handler.post(runnable as Runnable)
            } else {
                showToast(
                    this@MainActivity,
                    "Please set the Time"
                )
            }
        }
        recyclerView = findViewById(R.id.recyclerView)

        // Set the GridLayoutManager with 2 columns
    }

    fun getCountries(): List<Country> {
        val countries: MutableList<Country> = ArrayList()


        // Assume question is an object with a method getCountries()
        // which returns a list of Country objects

        // Simulate the question object for demonstration purposes
        for (i in question!!.countries.indices) {
            val country = question!!.countries[i]
            countries.add(country)
            //            countries.add(question.getAnswerId(),country);
            Log.e("ShowCtry", "ShowCtry: " + country.countryName)
        }

        return countries
    }

    private fun updateTime() {
        if (totalHour.length == 1) {
            totalHour = "0$totalHour"
        }
        val targetTime =
            "$totalHour:$totalMinutes:$totalSecs" // Example target time in HH:mm:ss format
        val result = getTimeDuration(targetTime)
        val showTime = result.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val displayStartTime = showTime[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        val getMinutes = displayStartTime[2].toInt()
        val getSecs = displayStartTime[3].toInt()
        val getHour = displayStartTime[2].toInt()
        if (getHour == 0 && getMinutes == 0 && getSecs < 20) {
            scheduleLabel!!.text = resources.getString(R.string.tv_challenge)
            timerLayout!!.visibility = View.GONE
            scheduleLabelLayout!!.visibility = View.VISIBLE
            tvDisplayTime!!.text = displayStartTime[2] + ":" + displayStartTime[3]
            saveBtn!!.visibility = View.GONE
        }
        if (getMinutes == 0 && getSecs == 0) {
            timerLayout!!.visibility = View.GONE
            scheduleLabelLayout!!.visibility = View.GONE
            questionAnswerlayout!!.visibility = View.VISIBLE
            loadNextQuestion()
        }
    }
    private fun setUI() {
        hourEditText = findViewById(R.id.hourEditText)
        hourEditText1 = findViewById(R.id.hourEditText1)
        minuteEditText = findViewById(R.id.minuteEditText)
        minuteEditText1 = findViewById(R.id.minuteEditText1)
        secsEditText = findViewById(R.id.secsEditText)
        secsEditText1 = findViewById(R.id.secsEditText1)
        saveBtn = findViewById(R.id.saveBtn)
        scheduleLabelLayout = findViewById(R.id.scheduleLabelLayout)
        scheduleLabel = findViewById(R.id.scheduleLabel)
        timerLayout = findViewById(R.id.timerLayout)
        tvDisplayTime = findViewById(R.id.tvDisplayTime)
        timerTextView = findViewById(R.id.timerTextView)
        imgFlag = findViewById(R.id.imgFlag)
        tvquestion = findViewById(R.id.tvquestion)
        gameoverLabel = findViewById(R.id.gameoverLabel)
        questionAnswerlayout = findViewById(R.id.questionAnswerlayout)
        tvscore = findViewById(R.id.tvscore)
        scoreUpdateLayout = findViewById(R.id.scoreUpdateLayout)
        sharedPreferences = getSharedPreferences("FlagQuizPrefs", MODE_PRIVATE)
    }


    private fun setEmptySecs() {
        secsEditText!!.setText("")
        secsEditText!!.hint = "0"
        secsEditText1!!.setText("")
        secsEditText1!!.hint = "0"
    }

    private fun setMinutesZero() {
        minuteEditText!!.setText("")
        minuteEditText!!.hint = "0"
        minuteEditText1!!.setText("")
        minuteEditText1!!.hint = "0"
    }


    private fun loadNextQuestion() {
        val jsonString = loadJSONFromAsset(this, "questions.json")
        if (jsonString != null) {
            try {
                val jsonObject = JSONObject(jsonString)
                val questionsArray = jsonObject.getJSONArray("questions")
                questions = ArrayList()
                for (i in 0 until questionsArray.length()) {
                    val questionObj = questionsArray.getJSONObject(i)
                    val answerId = questionObj.getInt("answer_id")
                    val countryCode = questionObj.getString("country_code")
                    val countriesArray = questionObj.getJSONArray("countries")
                    val countries: MutableList<Country> = ArrayList()
                    for (j in 0 until countriesArray.length()) {
                        val countryObj = countriesArray.getJSONObject(j)
                        val countryName = countryObj.getString("country_name")

                        val countryId = countryObj.getInt("id")
                        countries.add(Country(countryName, countryId))
                    }
                    (questions as ArrayList<Question>).add(Question(answerId, countries, countryCode))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            handler = Handler()
            runnable = object : Runnable {
                override fun run() {
                    if (gameOver == 0) {
                        displayNextQuestion()
                        handler.postDelayed(this, 10000) // 20 seconds
                    }
                }
            }
            //  updateAnswerTime("05");
            handler.post(runnable as Runnable)
        }
    }

     fun displayNextQuestion() {
        if (questions != null && !questions!!.isEmpty()) {
            question = questions!![currentIndex]
            saveCurrentIndex()
            /*   tvQuestionNum = tvQuestionNum+1;
            tvquestion.setText(""+tvQuestionNum);
*/
            Log.e("tvQuestionNum", tvQuestionNum.toString() + "::" + questions!!.size)
            if (tvQuestionNum > questions!!.size - 1) {
                questionAnswerlayout!!.visibility = View.GONE
                gameoverLabel!!.visibility = View.VISIBLE
                timerTextView!!.text = "00 : 00"
                handler.removeCallbacks(runnable!!)
                currentIndex = 0
                saveCurrentIndex()
                if (countDownTimer != null) {
                    countDownTimer!!.cancel()
                }
                gameOver = 1

                scheduleLabel!!.visibility = View.GONE
                scheduleLabel!!.text = resources.getString(R.string.tv_gameover)
                val handler = Handler()

                // Use postDelayed to update the TextView after 2 seconds (2000 milliseconds)
                handler.postDelayed({
                    questionAnswerlayout!!.visibility = View.GONE
                    scoreUpdateLayout!!.visibility = View.VISIBLE
                    gameoverLabel!!.text = "Score : " + addScore + "/" + questions!!.size
                    val sb = SpannableStringBuilder(gameoverLabel?.text)

// Span to set text color to some RGB value
                    val fcs = ForegroundColorSpan(resources.getColor(R.color.orange))

// Span to make text bold
                    val bss = StyleSpan(android.graphics.Typeface.BOLD)

// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

// Make them also bold
                    sb.setSpan(bss, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

// Make sure yourTextView is not null before setting the text
                    gameoverLabel?.let {
                        it.text = sb
                    } ?: run {
                        // Handle the case where yourTextView is null
                        Log.e("YourTag", "TextView is null")
                    }
                    timerTextView!!.text = "00 : 00"
                    saveBtn!!.visibility = View.GONE
                    // gameoverLabel.setVisibility(View.GONE);
                }, 2000)
            }
            if (question!!.countryCode == "NZ") {
                imgFlag!!.background = resources.getDrawable(R.drawable.nz)
                tvquestion!!.text = "1"
                tvQuestionNum = 1
                scheduleLabel!!.visibility = View.GONE
            } else if (question!!.countryCode == "AW") {
                tvquestion!!.text = "2"
                tvQuestionNum = 2
                imgFlag!!.background = resources.getDrawable(R.drawable.aw)
            } else if (question!!.countryCode == "EC") {
                tvquestion!!.text = "3"
                tvQuestionNum = 3
                imgFlag!!.background = resources.getDrawable(R.drawable.ec)
            } else if (question!!.countryCode == "PY") {
                tvquestion!!.text = "4"
                tvQuestionNum = 4
                imgFlag!!.background = resources.getDrawable(R.drawable.py)
            } else if (question!!.countryCode == "KG") {
                tvquestion!!.text = "5"
                tvQuestionNum = 5
                imgFlag!!.background = resources.getDrawable(R.drawable.kg)
            } else if (question!!.countryCode == "PM") {
                tvquestion!!.text = "6"
                tvQuestionNum = 6
                imgFlag!!.background = resources.getDrawable(R.drawable.pm)
            } else if (question!!.countryCode == "JP") {
                tvquestion!!.text = "7"
                tvQuestionNum = 7
                imgFlag!!.background = resources.getDrawable(R.drawable.jp)
            } else if (question!!.countryCode == "TM") {
                tvquestion!!.text = "8"
                tvQuestionNum = 8
                imgFlag!!.background = resources.getDrawable(R.drawable.tm)
            } else if (question!!.countryCode == "GA") {
                tvquestion!!.text = "9"
                tvQuestionNum = 9
                imgFlag!!.background = resources.getDrawable(R.drawable.ga)
            } else if (question!!.countryCode == "MQ") {
                tvquestion!!.text = "10"
                tvQuestionNum = 10
                imgFlag!!.background = resources.getDrawable(R.drawable.mq)
            } else if (question!!.countryCode == "BZ") {
                tvquestion!!.text = "11"
                tvQuestionNum = 11
                imgFlag!!.background = resources.getDrawable(R.drawable.bz)
            } else if (question!!.countryCode == "CZ") {
                tvquestion!!.text = "12"
                tvQuestionNum = 12
                imgFlag!!.background = resources.getDrawable(R.drawable.cz)
            } else if (question!!.countryCode == "AE") {
                tvquestion!!.text = "13"
                tvQuestionNum = 13
                imgFlag!!.background = resources.getDrawable(R.drawable.ae)
            } else if (question!!.countryCode == "JE") {
                tvquestion!!.text = "14"
                tvQuestionNum = 14
                imgFlag!!.background = resources.getDrawable(R.drawable.je)
            } else if (question!!.countryCode == "LS") {
                tvquestion!!.text = "15"
                tvQuestionNum = 15
                imgFlag!!.background = resources.getDrawable(R.drawable.ls)
            } else {
                imgFlag!!.background = resources.getDrawable(R.drawable.ic_launcher_background)
            }

            //  Log.e("ShowCtry", "ShowCtry: " + question.getCountries());
            for (i in question!!.countries.indices) {
                Log.e("ShowCtry", "ShowCtry: " + question!!.countries[i].countryName)
            }
            for (country in question!!.countries) {
                Log.e("TAG", "CounNamess " + country.countryName + ", ID: " + country.id)
            }

            recyclerView!!.layoutManager = GridLayoutManager(this, 2)
            countries = getCountries()
            countryAdapter = CountryAdapter(this, countries!!, this)
            recyclerView!!.adapter = countryAdapter
            currentIndex = (currentIndex + 1) % questions!!.size

            startCountdown()
        }
    }

    fun saveCurrentIndex() {
        val editor = sharedPreferences!!.edit()
        editor.putInt("currentIndex", currentIndex)
        editor.apply()
    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if((millisUntilFinished / 1000).toString().length==1){
                    timerTextView!!.text = "00 : "+"0" + (millisUntilFinished / 1000).toString()
                }else{
                    timerTextView!!.text = "00 : " + (millisUntilFinished / 1000).toString()
                }

            }

            override fun onFinish() {
                timerTextView!!.text = "00 : 00"
            }
        }.start()
    }

    private fun loadJSONFromAsset(context: Context, filename: String): String? {
        val jsonString = StringBuilder()
        try {
            val `is` = context.assets.open(filename)
            val reader = BufferedReader(InputStreamReader(`is`))
            var line: String?
            while ((reader.readLine().also { line = it }) != null) {
                jsonString.append(line)
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return jsonString.toString()
    }

    override fun onResume() {
        super.onResume()
        restoreState()
    }

    private fun restoreState() {
        currentIndex = sharedPreferences!!.getInt("currentIndex", 0)
        //tvQuestionNum
        if (currentIndex > 0) {
            timerLayout!!.visibility = View.GONE
            scheduleLabelLayout!!.visibility = View.GONE
            questionAnswerlayout!!.visibility = View.VISIBLE


            loadNextQuestion()
        }
    }


    override fun onItemClick(isCorrect: Boolean, newScore: Int) {
        addScore = addScore + newScore
        //  runnable?.let { handler.removeCallbacks(it) }
    }

    public class Question(val answerId: Int, val countries: List<Country>, val countryCode: String)

    companion object {
        private const val MIN_HOUR_VALUE = 0
        private const val MAX_HOUR_VALUE = 2
        private const val MAX_HOURS_VALUE = 23
        private const val MIN_MINUTES_VALUE = 6
    }
}