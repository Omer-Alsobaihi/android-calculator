package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var tvDisplay: TextView
    private lateinit var tvExpression: TextView

    private var currentInput = "0"
    private var firstOperand: Double? = null
    private var currentOperator: String? = null
    private var isNewInput = true
    private var lastOperand: Double? = null
    private var lastOperator: String? = null
    private var justEvaluated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.tvDisplay)
        tvExpression = findViewById(R.id.tvExpression)

        // Digit buttons
        val digitIds = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2",
            R.id.btn3 to "3", R.id.btn4 to "4", R.id.btn5 to "5",
            R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8",
            R.id.btn9 to "9"
        )

        digitIds.forEach { (id, digit) ->
            findViewById<MaterialButton>(id).setOnClickListener {
                onDigitPressed(digit)
            }
        }

        findViewById<MaterialButton>(R.id.btnDecimal).setOnClickListener { onDecimalPressed() }
        findViewById<MaterialButton>(R.id.btnClear).setOnClickListener { onClearPressed() }
        findViewById<MaterialButton>(R.id.btnSign).setOnClickListener { onSignPressed() }
        findViewById<MaterialButton>(R.id.btnPercent).setOnClickListener { onPercentPressed() }

        findViewById<MaterialButton>(R.id.btnAdd).setOnClickListener { onOperatorPressed("+") }
        findViewById<MaterialButton>(R.id.btnSubtract).setOnClickListener { onOperatorPressed("-") }
        findViewById<MaterialButton>(R.id.btnMultiply).setOnClickListener { onOperatorPressed("×") }
        findViewById<MaterialButton>(R.id.btnDivide).setOnClickListener { onOperatorPressed("÷") }

        findViewById<MaterialButton>(R.id.btnEquals).setOnClickListener { onEqualsPressed() }

        updateDisplay()
    }

    private fun onDigitPressed(digit: String) {
        if (justEvaluated) {
            currentInput = digit
            justEvaluated = false
            isNewInput = false
        } else if (isNewInput) {
            currentInput = digit
            isNewInput = false
        } else {
            if (currentInput == "0") {
                currentInput = digit
            } else {
                currentInput += digit
            }
        }
        lastOperator = null
        updateDisplay()
    }

    private fun onDecimalPressed() {
        if (justEvaluated) {
            currentInput = "0."
            justEvaluated = false
            isNewInput = false
        } else if (isNewInput) {
            currentInput = "0."
            isNewInput = false
        } else if (!currentInput.contains(".")) {
            currentInput += "."
        }
        updateDisplay()
    }

    private fun onClearPressed() {
        currentInput = "0"
        firstOperand = null
        currentOperator = null
        isNewInput = true
        lastOperand = null
        lastOperator = null
        justEvaluated = false
        tvExpression.text = ""
        updateDisplay()
    }

    private fun onSignPressed() {
        if (currentInput != "0") {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)
            } else {
                "-" + currentInput
            }
            updateDisplay()
        }
    }

    private fun onPercentPressed() {
        val value = currentInput.toDoubleOrNull() ?: return
        val result = value / 100.0
        currentInput = result.toString()
        updateDisplay()
    }

    private fun onOperatorPressed(op: String) {
        val currentValue = currentInput.toDoubleOrNull() ?: return

        if (firstOperand != null && currentOperator != null && !isNewInput) {
            val result = calculateResult(firstOperand!!, currentValue, currentOperator!!)
            firstOperand = result
            tvExpression.text = "$firstOperand $op"
            currentInput = result.toString()
        } else {
            firstOperand = currentValue
            tvExpression.text = "$firstOperand $op"
        }

        currentOperator = op
        isNewInput = true
        lastOperator = op
        lastOperand = currentValue
        justEvaluated = false
        updateDisplay()
    }

    private fun calculateResult(a: Double, b: Double, op: String): Double {
        return when (op) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            "÷" -> if (b != 0.0) a / b else Double.NaN
            else -> b
        }
    }

    private fun onEqualsPressed() {
        if (firstOperand != null && currentOperator != null) {
            val secondOperand = currentInput.toDoubleOrNull() ?: return
            val result = calculateResult(firstOperand!!, secondOperand, currentOperator!!)

            tvExpression.text = "$firstOperand ${currentOperator} $secondOperand ="

            if (result.isNaN()) {
                currentInput = "Error"
            } else if (result.isInfinite()) {
                currentInput = "Error"
            } else {
                currentInput = if (result == result.toLong().toDouble()) {
                    result.toLong().toString()
                } else {
                    String.format("%.10g", result).trimEnd('0').trimEnd('.')
                }
            }

            lastOperand = firstOperand
            lastOperator = currentOperator
            firstOperand = null
            currentOperator = null
            isNewInput = true
            justEvaluated = true
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        tvDisplay.text = currentInput
    }
}
