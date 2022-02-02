package com.example.simplemathsquiz
import java.util.ArrayList
import java.security.SecureRandom

class QuestionGenerator() {
    private val TOKEN = arrayListOf('+', '-', '*', '/')
    private val random = SecureRandom()

    fun generateQuestion(upperBound: Int) : Question {
        val operand = TOKEN[random.nextInt(TOKEN.size)]
        val secondInt = random.nextInt(upperBound) + 1
        val firstInt = if(operand == '/') random.nextInt(20) * secondInt else random.nextInt(upperBound) + 1
        val result = parseQuestion(firstInt, operand, secondInt)
        val answer = Answer(result.toString(), generateRandomAnswer(result), generateRandomAnswer(result))
        val question = Question(firstInt.toString(), operand.toString(), secondInt.toString(), answer)
        return question
    }

    private fun parseQuestion(num1: Int, operand: Char, num2: Int) : Int {
        when(operand) {
            '+' -> return num1 + num2
            '-' -> return num1 - num2
            '*' -> return num1 * num2
            '/' -> return num1 / num2
        }
        return -1
    }

    private fun generateRandomAnswer(currentAnswer: Int): String {
        var num: Int = currentAnswer
        do {
            num = random.nextInt(100) + 1
        } while(num == currentAnswer)
        return num.toString()
    }
}

class Question(val firstNum: String, val operand: String, val secondNum: String, val answer: Answer) {
    var alreadyAnswered = false
    override fun toString(): String {
        return "What is $firstNum $operand $secondNum?"
    }
}
data class Answer(val correctAnswer: String, val randomAnswer1: String, val randomAnswer2: String)


class Questions(upperBound: Int, questionsCount: Int) {
    private val questions = arrayListOf<Question>()
    private val questionGenerator = QuestionGenerator()

    init {
        for(i in 1..questionsCount) {
            val question = questionGenerator.generateQuestion(upperBound)
            questions.add(question)
        }
    }

    fun getQuestions(): ArrayList<Question> {
        return questions
    }
}