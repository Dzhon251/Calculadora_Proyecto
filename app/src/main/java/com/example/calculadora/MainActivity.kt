package com.example.calculadora

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

enum class Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private var currentNumber = ""
    private var savedNumber = ""
    private var currentOperation: Operation? = null
    private var currentExpression = ""
    private var isCalculationDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tv_display)

        val btn0 = findViewById<Button>(R.id.btn_0)
        val btn1 = findViewById<Button>(R.id.btn_1)
        val btn2 = findViewById<Button>(R.id.btn_2)
        val btn3 = findViewById<Button>(R.id.btn_3)
        val btn4 = findViewById<Button>(R.id.btn_4)
        val btn5 = findViewById<Button>(R.id.btn_5)
        val btn6 = findViewById<Button>(R.id.btn_6)
        val btn7 = findViewById<Button>(R.id.btn_7)
        val btn8 = findViewById<Button>(R.id.btn_8)
        val btn9 = findViewById<Button>(R.id.btn_9)
        val btnSum = findViewById<Button>(R.id.btn_sum)
        val btnSubtract = findViewById<Button>(R.id.btn_subtract)
        val btnMultiply = findViewById<Button>(R.id.btn_multiply)
        val btnDivide = findViewById<Button>(R.id.btn_divide)
        val btnEquals = findViewById<Button>(R.id.btn_equals)
        val btnDot = findViewById<Button>(R.id.btn_dot)
        val btnClear = findViewById<Button>(R.id.btn_clear)
        val btnClearEntry = findViewById<Button>(R.id.btn_clear_entry)

        val btnPercentage = findViewById<Button>(R.id.btn_percentage)
        btnPercentage.setOnClickListener { calculatePercentage() }

        val btnSquare = findViewById<Button>(R.id.btn_square)
        btnSquare.setOnClickListener { calculateSquare() }

        btn0.setOnClickListener { appendNumber("0") }
        btn1.setOnClickListener { appendNumber("1") }
        btn2.setOnClickListener { appendNumber("2") }
        btn3.setOnClickListener { appendNumber("3") }
        btn4.setOnClickListener { appendNumber("4") }
        btn5.setOnClickListener { appendNumber("5") }
        btn6.setOnClickListener { appendNumber("6") }
        btn7.setOnClickListener { appendNumber("7") }
        btn8.setOnClickListener { appendNumber("8") }
        btn9.setOnClickListener { appendNumber("9") }

        btnSum.setOnClickListener { setOperation(Operation.ADD) }
        btnSubtract.setOnClickListener { setOperation(Operation.SUBTRACT) }
        btnMultiply.setOnClickListener { setOperation(Operation.MULTIPLY) }
        btnDivide.setOnClickListener { setOperation(Operation.DIVIDE) }

        btnEquals.setOnClickListener { calculateResult() }
        btnDot.setOnClickListener { addDecimalPoint() }
        btnClear.setOnClickListener { clear() }
        btnClearEntry.setOnClickListener { clearEntry() }
    }

    private fun appendNumber(number: String) {
        if (isCalculationDone) {
            resetCalculator()
        }

        if (currentOperation != null && currentNumber == savedNumber) {
            currentNumber = number
        } else {
            if (currentNumber == "0") {
                currentNumber = number
            } else {
                currentNumber += number
            }
        }

        updateDisplay()
    }

    private fun setOperation(operation: Operation) {
        savedNumber = currentNumber
        currentNumber = ""
        currentOperation = operation

        // Guardar la expresión actual
        currentExpression = "$savedNumber ${getOperationSymbol(operation)}"
        updateDisplay()
    }

    private fun calculateResult() {
        val num1 = savedNumber.toDoubleOrNull()
        val num2 = currentNumber.toDoubleOrNull()

        if (num1 != null && num2 != null && currentOperation != null) {
            val result = when (currentOperation) {
                Operation.ADD -> num1 + num2
                Operation.SUBTRACT -> num1 - num2
                Operation.MULTIPLY -> num1 * num2
                Operation.DIVIDE -> num1 / num2
                else -> throw IllegalStateException("Invalid operation")
            }

            val formattedResult = if (result % 1 == 0.0) {
                result.toLong().toString()
            } else {
                String.format("%.4f", result).replace(Regex("\\.?0+$"), "")
            }

            currentExpression = ""
            currentNumber = formattedResult
            updateDisplay()
            isCalculationDone = true
        }
    }

    private fun addDecimalPoint() {
        if (!currentNumber.contains(".")) {
            currentNumber += "."
            updateDisplay()
        }
    }

    private fun clear() {
        currentNumber = ""
        savedNumber = ""
        currentOperation = null

        // Reiniciar la expresión
        currentExpression = ""
        updateDisplay()
    }

    private fun clearEntry() {
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.dropLast(1)
        } else if (currentExpression.isNotEmpty()) {
            currentOperation = null
            currentExpression = currentExpression.dropLast(1)
        }
        updateDisplay()
    }

    private fun updateDisplay() {
        tvDisplay.text = if (currentExpression.isEmpty()) {
            currentNumber
        } else {
            "$currentExpression $currentNumber"
        }
    }

    private fun calculatePercentage() {
        val num = currentNumber.toDoubleOrNull()

        if (num != null) {
            val result = num / 100.0
            currentNumber = result.toString()
            updateDisplay()
        }
    }

    private fun calculateSquare() {
        val num = currentNumber.toDoubleOrNull()

        if (num != null) {
            val result = num * num
            currentNumber = result.toString()
            updateDisplay()
        }
    }

    private fun getOperationSymbol(operation: Operation): String {
        return when (operation) {
            Operation.ADD -> "+"
            Operation.SUBTRACT -> "-"
            Operation.MULTIPLY -> "x"
            Operation.DIVIDE -> "÷"
        }
    }

    private fun resetCalculator() {
        currentNumber = "0"
        savedNumber = ""
        currentOperation = null
        currentExpression = ""
        isCalculationDone = false
        updateDisplay()
    }
}
