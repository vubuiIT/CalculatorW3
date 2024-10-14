package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.CalculatorLinearBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bindingView: CalculatorLinearBinding
    private var isNewOperation: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingView = CalculatorLinearBinding.inflate(layoutInflater)
        setContentView(bindingView.root)

        setSupportActionBar(bindingView.toolbar)
        bindingView.toolbar.setNavigationIcon(R.drawable.logopic)

        // Nút số
        bindingView.num0.setOnClickListener { appendToExpression("0") }
        bindingView.num1.setOnClickListener { appendToExpression("1") }
        bindingView.num2.setOnClickListener { appendToExpression("2") }
        bindingView.num3.setOnClickListener { appendToExpression("3") }
        bindingView.num4.setOnClickListener { appendToExpression("4") }
        bindingView.num5.setOnClickListener { appendToExpression("5") }
        bindingView.num6.setOnClickListener { appendToExpression("6") }
        bindingView.num7.setOnClickListener { appendToExpression("7") }
        bindingView.num8.setOnClickListener { appendToExpression("8") }
        bindingView.num9.setOnClickListener { appendToExpression("9") }
        bindingView.numDot.setOnClickListener { appendToExpression(".") }

        // Nút phép toán
        bindingView.actionAdd.setOnClickListener { appendToExpression("+") }
        bindingView.actionMinus.setOnClickListener { appendToExpression("-") }
        bindingView.actionMultiply.setOnClickListener { appendToExpression("*") }
        bindingView.actionDivide.setOnClickListener { appendToExpression("/") }

        // Clear
        bindingView.clear.setOnClickListener {
            bindingView.answer.text = ""
            isNewOperation = true
        }

        // Xóa ký tự cuối
        bindingView.C.setOnClickListener {
            val currentExpression = bindingView.answer.text.toString()
            if (currentExpression.isNotEmpty()) {
                bindingView.answer.text = currentExpression.dropLast(1)
            }
        }

        // Nút =
        bindingView.actionEquals.setOnClickListener {
            calculateResult()
        }
    }

    // Thêm chuỗi biểu thức
    private fun appendToExpression(value: String) {
        if (isNewOperation) {
            bindingView.answer.text = ""
        }
        bindingView.answer.append(value)
        isNewOperation = false
    }

    // Tính toán
    private fun calculateResult() {
        try {
            val expressionText = bindingView.answer.text.toString()
            val finalResult = evaluateExpression(expressionText)
            bindingView.answer.text = finalResult.toString()
        } catch (e: Exception) {
            bindingView.answer.text = "Error"
        }
        isNewOperation = true
    }

    // Tính biểu thức
    private fun evaluateExpression(expression: String): Int {
        val numberList = mutableListOf<Int>()
        val operatorList = mutableListOf<Char>()

        var currentNum = ""

        // Tách số và phép toán ra
        for (char in expression) {
            if(char.isDigit() || char == '.') {
                currentNum += char
            } else {
                numberList.add(currentNum.toInt())
                currentNum = ""
                operatorList.add(char)
            }
        }

        // Thêm số cuối cùng vào danh sách
        if (currentNum.isNotEmpty()) {
            numberList.add(currentNum.toInt())
        }

        // Xử lý nhân và chia trước
        var i = 0
        while(i < operatorList.size) {
            if(operatorList[i] == '*' || operatorList[i] == '/') {
                val leftNum = numberList[i]
                val rightNum = numberList[i+1]
                val partialResult = if (operatorList[i] == '*') leftNum * rightNum else leftNum / rightNum

                // Thay thế số ở vị trí hiện tại bằng kết quả và xóa số tiếp theo
                numberList[i] = partialResult
                numberList.removeAt(i+1) // Xóa số đã tính
                operatorList.removeAt(i) // Xóa phép tính đã tính
                i-- // lùi
            }
            i++
        }

        // Xử lý cộng và trừ
        var finalResult = numberList[0]
        i = 0
        while(i < operatorList.size) {
            when (operatorList[i]) {
                '+' -> finalResult += numberList[i+1]
                '-' -> finalResult -= numberList[i+1]
            }
            i++
        }

        return finalResult
    }
}
