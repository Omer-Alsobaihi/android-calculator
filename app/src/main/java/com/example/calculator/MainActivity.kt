package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentInput = "0"
    private var firstOperand: Double? = null
    private var currentOperator: String? = null
    private var isNewInput = true

    private val decimalFormat = DecimalFormat("#.##########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        updateDisplay()
    }

    private fun setupClickListeners() {
        // Numbers
        val numberButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9
        )
        numberButtons.forEach { button ->
            button.setOnClickListener { onNumberClick((it as Button).text.toString()) }
        }

        // Operators
        binding.btnAdd.setOnClickListener { onOperatorClick("+") }
        binding.btnSubtract.setOnClickListener { onOperatorClick("−") }
        binding.btnMultiply.setOnClickListener { onOperatorClick("×") }
        binding.btnDivide.setOnClickListener { onOperatorClick("÷") }

        // Other
        binding.btnEquals.setOnClickListener { onEqualsClick() }
        binding.btnClear.setOnClickListener { onClearClick() }
        binding.btnDecimal.setOnClickListener { onDecimalClick() }
    }

    private fun onNumberClick(number: String) {
        if (isNewInput) {
            currentInput = number
            isNewInput = false
        } else {
            currentInput = if (currentInput == "0") number else currentInput + number
        }
        updateDisplay()
    }

    private fun onDecimalClick() {
        if (isNewInput) {
            currentInput = "0."
            isNewInput = false
        } else if (!currentInput.contains(".")) {
            currentInput += "."
        }
        updateDisplay()
    }

    private fun onOperatorClick(operator: String) {
        // If user presses operator right after another operator, replace it
        if (isNewInput && currentOperator != null) {
            currentOperator = operator
            updateExpressionDisplay()
            return
        }

        val inputValue = currentInput.toDoubleOrNull() ?: 0.0

        if (firstOperand == null) {
            firstOperand = inputValue
        } else if (currentOperator != null) {
            val result = calculate(firstOperand!!, inputValue, currentOperator!!)
            firstOperand = result
            currentInput = formatNumber(result)
        }

        currentOperator = operator
        isNewInput = true
        updateDisplay()
    }

    private fun onEqualsClick() {
        if (firstOperand == null || currentOperator == null) return

        val inputValue = currentInput.toDoubleOrNull() ?: 0.0
        val result = calculate(firstOperand!!, inputValue, currentOperator!!)

        currentInput = formatNumber(result)
        firstOperand = null
        currentOperator = null
        isNewInput = true
        updateDisplay()
    }

    private fun onClearClick() {
        currentInput = "0"
        firstOperand = null
        currentOperator = null
        isNewInput = true
        updateDisplay()
    }

    private fun calculate(a: Double, b: Double, operator: String): Double {
        return when (operator) {
            "+" -> a + b
            "−" -> a - b
            "×" -> a * b
            "÷" -> if (b == 0.0) Double.NaN else a / b
            else -> b
        }
    }

    private fun formatNumber(value: Double): String {
        if (value.isNaN()) return "Error"
        if (value.isInfinite()) return "Error"
        // If integer, show without decimal
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            decimalFormat.format(value)
        }
    }

    private fun updateDisplay() {
        binding.tvDisplay.text = currentInput
        updateExpressionDisplay()
    }

    private fun updateExpressionDisplay() {
        val expression = buildString {
            if (firstOperand != null && currentOperator != null) {
                append(formatNumber(firstOperand!!))
                append(" ")
                append(currentOperator)
            }
        }
        binding.tvExpression.text = expression
    }
}
