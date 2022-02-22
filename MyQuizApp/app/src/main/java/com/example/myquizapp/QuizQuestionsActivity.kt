package com.example.myquizapp

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private var mCurrentPosition: Int = 1
    private var mQuestionList: MutableList<Question>? = null

    private var mSelectedOptionPosition: Int = 0

    private var buttonSubmit: Button? = null
    private var progressBar: ProgressBar? = null
    private var tvProgress: TextView? = null
    private var tvQuestion: TextView? = null
    private var ivImage: ImageView? = null
    private var tvOptionOne: TextView? = null
    private var tvOptionTwo: TextView? = null
    private var tvOptionThree: TextView? = null
    private var tvOptionFour: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mQuestionList = Constant.getQuestions()
        Log.e("Question Size", "${mQuestionList!!.size}")

        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tv_progress)
        tvQuestion = findViewById(R.id.tv_question)
        ivImage = findViewById(R.id.iv_image)
        tvOptionOne = findViewById(R.id.tv_option_one)
        tvOptionTwo = findViewById(R.id.tv_option_two)
        tvOptionThree = findViewById(R.id.tv_option_three)
        tvOptionFour = findViewById(R.id.tv_option_four)
        buttonSubmit = findViewById(R.id.btn_submit)

        setQuestion()

        tvOptionOne?.setOnClickListener(this)
        tvOptionTwo?.setOnClickListener(this)
        tvOptionThree?.setOnClickListener(this)
        tvOptionFour?.setOnClickListener(this)
        buttonSubmit?.setOnClickListener(this)
    }


    private fun setQuestion() {
        val question: Question = mQuestionList!![mCurrentPosition - 1]

        defaultOptionView()
        buttonSubmit?.text = if (mCurrentPosition == mQuestionList!!.size) "FINISH" else "SUBMIT"

        progressBar?.progress = mCurrentPosition
        tvProgress?.text = "$mCurrentPosition/${progressBar?.max}"

        tvQuestion?.text = question.question
        ivImage?.setImageResource(question.image)
        tvOptionOne?.text = question.optionOne
        tvOptionTwo?.text = question.optionTwo
        tvOptionThree?.text = question.optionThree
        tvOptionFour?.text = question.optionFour
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv_option_one -> {
                tvOptionOne?.let {
                    selectedOption(it, 1)
                }
            }
            R.id.tv_option_two -> {
                tvOptionTwo?.let {
                    selectedOption(it, 2)
                }
            }
            R.id.tv_option_three -> {
                tvOptionThree?.let {
                    selectedOption(it, 3)
                }
            }
            R.id.tv_option_four -> {
                tvOptionFour?.let {
                    selectedOption(it, 4)
                }
            }


            R.id.btn_submit -> {
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++
                    when {
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            Toast.makeText(
                                this,
                                "You have successfully complete the quiz.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_boarder_bg)
                    }

                    answerView(question.correctAnswer, R.drawable.correct_option_boarder_bg)

                    buttonSubmit?.text =
                        if (mCurrentPosition == mQuestionList!!.size) "Finish" else "GO TO NEXT QUESTION"

                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawbleView: Int) {
        when (answer) {
            1 -> {
                tvOptionOne?.background = ContextCompat.getDrawable(this, drawbleView)
            }
            2 -> {
                tvOptionTwo?.background = ContextCompat.getDrawable(this, drawbleView)
            }
            3 -> {
                tvOptionThree?.background = ContextCompat.getDrawable(this, drawbleView)
            }
            4 -> {
                tvOptionFour?.background = ContextCompat.getDrawable(this, drawbleView)
            }
        }
    }

    private fun selectedOption(tv: TextView, selectedOptionNum: Int) {
        defaultOptionView()

        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this, R.drawable.selected_option_border_bg
        )
    }

    private fun defaultOptionView() {

        val options = ArrayList<TextView>()

        tvOptionOne?.let {
            options.add(0, it)
        }

        tvOptionTwo?.let {
            options.add(1, it)
        }
        tvOptionThree?.let {
            options.add(2, it)
        }
        tvOptionFour?.let {
            options.add(3, it)
        }

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this, R.drawable.default_option_border_bg
            )
        }


    }
}