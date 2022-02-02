package com.example.simplemathsquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.security.SecureRandom


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val UPPER_BOUND = 30
    private val TOTAL_QUESTIONS = 50
    private var randomEngine: SecureRandom
    private var totalQuestionsCorrect = 0

    private var questions: ArrayList<Question>
    private lateinit var questionsCountTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var previousQuestionButton: Button
    private lateinit var nextQuestionButton: Button
    private lateinit var firstAnswerButton: Button
    private lateinit var secondAnswerButton: Button
    private lateinit var thirdAnswerButton: Button
    private lateinit var questionsOutputTextView: TextView
    private var questionsIndex: Int = 0

    init {
        questions = Questions(UPPER_BOUND, TOTAL_QUESTIONS).getQuestions()
        randomEngine = SecureRandom()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialiseViews()
    }

    private fun initialiseViews() {
        questionsCountTextView = findViewById(R.id.questionsCountTextView)
        questionTextView = findViewById(R.id.questionTextView)
        questionsOutputTextView = findViewById(R.id.questionsAnsweredTextView)
        questionsOutputTextView.movementMethod = ScrollingMovementMethod()


        previousQuestionButton = findViewById(R.id.previousQuestionButton)
        nextQuestionButton = findViewById(R.id.nextQuestionButton)
        firstAnswerButton = findViewById(R.id.answer1Button)
        secondAnswerButton = findViewById(R.id.answer2Button)
        thirdAnswerButton = findViewById(R.id.answer3Button)
        initialiseQuestion()
        initialiseButtonsListeners()
        initialiseAnswersButtons()
    }

    private fun initialiseQuestion() {
        questionTextView.text = questions[questionsIndex].toString()
        val questionCounter = StringBuffer()
        questionCounter.append("Question ").append((questionsIndex+1).toString()).append("/")
                                           .append((questions.size).toString())
        questionsCountTextView.text = questionCounter.toString()
        initialiseNextButtonText()
    }

    private fun initialiseNextButtonText() {
        if(questionsIndex+1 == questions.size) {
            nextQuestionButton.setText(R.string.finish_text)
        } else {
            nextQuestionButton.setText(R.string.next_text)
        }
    }


    private fun initialiseAnswersButtons() {
        val randomButtonChoice = randomEngine.nextInt(3)
        val currentQuestion = questions[questionsIndex]
        var boolFlag = false
        for(i in 0..2) {
            if(randomButtonChoice != i && !boolFlag) {
                setTextOfAnswerButton(i, currentQuestion.answer.randomAnswer1)
                boolFlag = true
            } else {
                setTextOfAnswerButton(i, currentQuestion.answer.randomAnswer2)
            }
        }
        setTextOfAnswerButton(randomButtonChoice, currentQuestion.answer.correctAnswer)
    }

    private fun setTextOfAnswerButton(index: Int, text: String) {
        when(index) {
            0 -> firstAnswerButton.text = text
            1 -> secondAnswerButton.text= text
            2 -> thirdAnswerButton.text = text
        }
    }

    private fun initialiseButtonsListeners() {
        initialiseQuestionButtonsListeners()
        initialiseAnswerButtonsListeners()
    }

    private fun initialiseAnswerButtonsListeners() {
        firstAnswerButton.setOnClickListener(this)
        secondAnswerButton.setOnClickListener(this)
        thirdAnswerButton.setOnClickListener(this)
    }

    private fun initialiseQuestionButtonsListeners() {
        previousQuestionButton.setOnClickListener(this)
        nextQuestionButton.setOnClickListener(this)
    }


    private fun initialisePreviousQuestion() {
        questionsIndex = if(questionsIndex == 0) 0 else --questionsIndex
        initialiseQuestion()
        initialiseAnswersButtons()
    }

    private fun initialiseNextQuestion() {
        if(questionsIndex+1 == questions.size) {
            Toast.makeText(this, "You got $totalQuestionsCorrect out of ${questions.size} questions correct.", Toast.LENGTH_LONG).show()
        }
        questionsIndex = if(questionsIndex == questions.size-1) questions.size-1 else ++questionsIndex
        initialiseQuestion()
        initialiseAnswersButtons()
    }

    private fun appendAnswerInOutput(question: Question, userChoice: String, userAnsweredCorrectly: Boolean) {
        val outputBuffer = StringBuffer().append(questionsIndex+1)
                                         .append(") ").append(question.toString())
                                         .append(" = ").append(userChoice)
        if(userAnsweredCorrectly) {
            outputBuffer.append("? ✔\n")
        } else {
            outputBuffer.append("? ❌\n")
        }
        questionsOutputTextView.append(outputBuffer.toString())
    }

    private fun checkAnswerButtonClick(button: Button?) {
        val currentQuestion = questions[questionsIndex]
        val userSelectedAnswer = button?.text
        if(currentQuestion.alreadyAnswered) {
            Toast.makeText(this, "You already answered this question!", Toast.LENGTH_LONG).show()
        } else {
            if(currentQuestion.answer.correctAnswer.equals(userSelectedAnswer) && !currentQuestion.alreadyAnswered) {
                totalQuestionsCorrect++
                appendAnswerInOutput(currentQuestion, userSelectedAnswer.toString(), true)

            } else {
                appendAnswerInOutput(currentQuestion, userSelectedAnswer.toString(), false)
            }
        }
        initialiseNextQuestion()
        currentQuestion.alreadyAnswered = true
    }


    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.previousQuestionButton -> initialisePreviousQuestion()
            R.id.nextQuestionButton -> initialiseNextQuestion()
            R.id.answer1Button, R.id.answer2Button, R.id.answer3Button -> checkAnswerButtonClick(
                view as Button?)
        }
    }
}