package lucky.kotlinbeginner.kotlin_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import lucky.kotlinbeginner.kotlin_calculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {



    private lateinit var binding : ActivityMainBinding
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    fun numberAction(view: View) {
        if (view is Button) {

            if (view.text == ".") {
                if (canAddDecimal) {
                    binding.workingsTV.append(view.text)
                    canAddDecimal = false
                    canAddOperation = false
                }
            }
            else {
                binding.workingsTV.append(view.text)
                canAddOperation = true
            }

        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun backSpaceAction(view: View) {
        val length = binding.workingsTV.length()
        if (length > 0) {
            binding.workingsTV.text = binding.workingsTV.text.substring(0, length-1)
        }

    }

    fun allClearAction(view: View) {
        binding.resultsTV.text = ""
        binding.workingsTV.text = ""
    }

    fun equalsAction(view: View) {
        binding.resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String {

        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)

        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return  result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>) : Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit

                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList

        while (list.contains('X') || list.contains('/')) {
            list = calcTimesDiv(list)
        }

        return list

    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
                var newList = mutableListOf<Any>()
                var restartIndex = passedList.size

        for (i in passedList.indices){
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val previousDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float

                when(operator) {
                    'x' -> {
                        newList.add(previousDigit * nextDigit)
                        restartIndex = i + 1
                    }

                    '/' -> {
                        newList.add(previousDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(previousDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex)
                newList.add(passedList[i])
        }


        return newList
    }

    private fun digitsOperators() : MutableList<Any> {

        val list = mutableListOf<Any>()

        var currentDigit = ""

        for (character in binding.workingsTV.text){

            if (character.isDigit() || character == '.') {
                currentDigit += character
            } else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }

        }

        if (currentDigit != "") {
            list.add(currentDigit.toFloat())
        }

        return list
    }
}