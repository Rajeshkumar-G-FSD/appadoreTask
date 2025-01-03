package com.example.quizgame
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.view.View
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.InputStream
import org.json.JSONArray

// Import your GameActivity, Question, and Country classes
import com.example.quizgame.MainActivity.Question
import junit.framework.TestCase.assertEquals
import java.util.logging.Handler

class GameActivityTest {

    @Mock
    lateinit var handler: Handler

    @Mock
    lateinit var resources: Resources

    lateinit var gameActivity: MainActivity
    lateinit var gameActivityTest: GameActivityTest
    lateinit var questions: List<Question>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        gameActivity = spy(MainActivity())
        gameActivityTest.handler = handler
        gameActivityTest.resources = resources
        gameActivity.currentIndex = 0

        // Load questions from JSON file
        questions = loadQuestionsFromJson()
        gameActivityTest.questions = this.questions
    }

    @Test
    fun testDisplayNextQuestion_GameOver() {
        `when`(questions.size).thenReturn(15)
        `when`(questions[0]).thenReturn(questions[0])
       // `when`(resources.getString(R.string.tv_gameover)).thenReturn("Game Over")

        gameActivity.displayNextQuestion()

        verify(gameActivity).saveCurrentIndex()
        assertEquals(View.GONE, gameActivity.questionAnswerlayout!!.visibility)
        assertEquals(View.VISIBLE, gameActivity.gameoverLabel!!.visibility)
        assertEquals("00 : 00", gameActivity.timerTextView!!.text)
        assertEquals(0, gameActivity.currentIndex)
        assertEquals(1, gameActivity.gameOver)
        assertEquals("Game Over", gameActivity.scheduleLabel!!.text)
    }

    @Test
    fun testDisplayNextQuestion_QuestionCountryNZ() {
        `when`(questions.size).thenReturn(15)
        `when`(questions[0]).thenReturn(questions[0])

        gameActivity.displayNextQuestion()

        assertEquals("1", gameActivity.tvquestion!!.text)
        assertEquals(1, gameActivity.tvQuestionNum)
    }

    private fun loadQuestionsFromJson(): List<Question> {
        val inputStream: InputStream = this::class.java.classLoader!!.getResourceAsStream("questions.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)
        val questions = mutableListOf<Question>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val question = Question(
                answerId = jsonObject.getInt("answer_id"),
                countryCode = jsonObject.getString("country_code"),
                countries = jsonObject.getJSONArray("countries").let { countriesArray ->
                    List(countriesArray.length()) { index ->
                        val countryObject = countriesArray.getJSONObject(index)
                        Country(
                            countryName = countryObject.getString("country_name"),
                            id = countryObject.getInt("id")
                        )
                    }
                }
            )
            questions.add(question)
        }

        return questions
    }
}
