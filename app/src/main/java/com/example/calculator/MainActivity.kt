package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var firstOperand: Double = 0.0
    private var operator: String = ""
    private var isNewOperand = true
    private var hasDecimal = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val display = findViewById<TextView>(R.id.display)
        display.text = "0"

        val buttons = listOf(
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "C", "0", ".", "+",
            "="
        )

        buttons.forEach { value ->
            val btn = findViewById<Button>(resources.getIdentifier(value, "id", packageName))
            btn.setOnClickListener {
                handleButton(value, display)
            }
        }

        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            calculate(display)
        }
    }

    private fun handleButton(value: String, display: TextView) {
        when (value) {
            "C" -> {
                display.text = "0"
                firstOperand = 0.0
                operator = ""
                isNewOperand = true
                hasDecimal = false
            }
            in "0".."9" -> {
                if (isNewOperand) {
                    display.text = value
                    isNewOperand = false
                } else {
                    display.text = if (display.text == "0") value else display.text.toString() + value
                }
                hasDecimal = false
            }
            "." -> {
                if (!hasDecimal) {
                    display.text = display.text.toString() + "."
                    hasDecimal = true
                }
            }
            "+", "-", "*", "/" -> {
                firstOperand = display.text.toDoubleOrNull() ?: 0.0
                operator = value
                isNewOperand = true
            }
            "=" -> {
                calculate(display)
            }
        }
    }

    private fun calculate(display: TextView) {
        val secondOperand = display.text.toDoubleOrNull() ?: return
        val result = when (operator) {
            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand
            "*" -> firstOperand * secondOperand
            "/" -> if (secondOperand != 0.0) firstOperand / secondOperand else Double.POSITIVE_INFINITY
            else -> return
        }
        display.text = if (result.isNaN() || result.isInfinite()) "Error" else result.toString()
        operator = ""
        firstOperand = 0.0
        isNewOperand = true
        hasDecimal = false
    }
}
