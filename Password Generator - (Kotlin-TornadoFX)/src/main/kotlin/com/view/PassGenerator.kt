package com.view

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import java.util.*
import tornadofx.*

class MainView : View("Pass Generator") {

    private var lowerCaseLettersC = SimpleBooleanProperty(true)
    private var upperCaseLettersC = SimpleBooleanProperty(true)
    private var numbersC = SimpleBooleanProperty(true)
    private var symbolsC= SimpleBooleanProperty(true)
    private var lenPass: TextField by singleAssign()
    private var lenPassInt: Int = 10
    private var passWord: TextField by singleAssign()

    override val root = vbox {

        // Configurações iniciais:

        prefHeight = 500.0
        prefWidth = 350.0

        style {
            backgroundColor += Color.BLUEVIOLET
        }

        // Título:

        gridpane {
            textflow {
                text("PASSWORD\n") {
                    fill = Color.GHOSTWHITE
                    font = Font(24.0)
                }
                text("GENERATOR") {
                    fill = Color.GHOSTWHITE
                    font = Font(24.0)
                }
                gridpaneConstraints {
                    marginTop = 50.0
                    marginLeft = 110.0
                    marginBottom = 20.0
                }
            }
        }

        // Caixa de texto e botão copiar para a senha:

        gridpane {
            hbox {
                passWord = textfield{
                    prefWidth = 200.0
                    prefHeight = 20.0
                    isEditable = false

                    hboxConstraints {
                        marginRight = 3.0
                    }
                }
                button("[ ]]") {
                    prefWidth = 35.0
                    prefHeight = 10.0
                    textFill = Color.BLACK

                    style {
                        borderColor += box(top = Color.DEEPSKYBLUE, right = Color.DEEPSKYBLUE, left = Color.DEEPSKYBLUE,
                                bottom = Color.DEEPSKYBLUE)
                        fontWeight = FontWeight.SEMI_BOLD
                    }

                    tooltip("Copiar")

                    action{
                        copyPass(passWord.text)
                    }
                }

                gridpaneConstraints {
                    marginTop = 20.0
                    marginLeft = 70.0
                }
            }
        }

        //Checkboxes:

        gridpane {
            vbox {
                checkbox("Letras Minúsculas", lowerCaseLettersC) {
                    prefWidth = 200.0
                    prefHeight = 20.0
                    textFill = Color.GHOSTWHITE
                }
                checkbox("Letras Maiúsculas", upperCaseLettersC) {
                    prefWidth = 200.0
                    prefHeight = 20.0
                    textFill = Color.GHOSTWHITE
                }
                checkbox("Números", numbersC) {
                    prefWidth = 200.0
                    prefHeight = 20.0
                    textFill = Color.GHOSTWHITE
                }
                checkbox("Símbolos", symbolsC) {
                    prefWidth = 200.0
                    prefHeight = 20.0
                    textFill = Color.GHOSTWHITE
                }
                gridpaneConstraints {
                    marginLeft = 95.0
                    marginTop = 50.0
                }
            }
        }

        // Caixa de texto para quantidade de caracteres:

        gridpane {
            hbox {
                lenPass = textfield(lenPassInt.toString()) {
                    prefWidth = 30.0
                    prefHeight = 3.0
                    tooltip("A senha deve estar entre 4-99 caracteres!")
                    filterInput { it.controlNewText.isInt() }
                }
                label("  Quantidade de caracteres") {
                    textFill = Color.GHOSTWHITE
                }
                gridpaneConstraints {
                    marginBottom = 50.0
                    marginLeft = 95.0
                    marginTop = 25.0
                }
            }
        }

        // Botão para gerar uma senha:

        gridpane {
            button("Generate") {
                prefWidth = 200.0
                prefHeight = 30.0
                textFill = Color.PURPLE
                tooltip("Generate pass")
                gridpaneConstraints {
                    marginLeft = 80.0
                }
                action {
                    lenPassInt = convertStrInt(lenPass.text)
                    lenPass.text = lenPassInt.toString()
                    passWord.text = generatePass(lenPassInt, lowerCaseLettersC.value, upperCaseLettersC.value,
                            numbersC.value, symbolsC.value)
                }
            }
        }
    }

    private fun copyPass(str: String){
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val transfer = StringSelection(str)
        clipboard.setContents(transfer, null)
    }

    private fun generatePass(n: Int, c1: Boolean, c2: Boolean, c3: Boolean, c4: Boolean): String {

        val lower = lowerList()
        val upper = upperList()
        val numbers = numbersList()
        val symbols = symbolsList()
        val all = arrayListOf<String>()
        val passWord = arrayListOf<String>()
        val strPass: String

        var aux: Int = n
        var lenAll: Int = 0+0

        if (c1) {
            passWord.add(lower[(0..25).random()])
            aux -= 1
            all += lower
            lenAll += 26
        }

        if (c2) {
            passWord.add(upper[(0..25).random()])
            aux -= 1
            all += upper
            lenAll += 26
        }

        if (c3) {
            passWord.add(numbers[(0..9).random()])
            aux -= 1
            all += numbers
            lenAll += 10
        }

        if (c4) {
            passWord.add(symbols[(0..26).random()])
            aux -= 1
            all += symbols
            lenAll += 27
        }

        if (lenAll > 0){
            for (i in 0 until aux) {
                passWord.add(all[(0 until lenAll).random()])
            }
        }

        passWord.shuffle()
        strPass = passWord.joinToString("")

        return strPass

    }

    private fun IntRange.random() =
            Random().nextInt((endInclusive + 1) - start) +  start

    private fun convertStrInt(strPass: String): Int {

        var x: Int = strPass.toInt()
        if (x < 4) {
            x = 4
        }
        if (x > 99) {
            x = 99
        }
        return x
    }

    private fun lowerList(): ArrayList<String>{

        val lower = arrayListOf("a")
        for (i in 98..122) {
            lower.add(i.toChar().toString())
        }
        return lower
    }

    private fun upperList(): ArrayList<String>{

        val upper = arrayListOf("A")
        for (i in 66..90) {
            upper.add(i.toChar().toString())
        }
        return upper
    }

    private fun numbersList(): ArrayList<String>{
        val numbers = arrayListOf("0")
        for (i in 49..57) {
            numbers.add(i.toChar().toString())
        }
        return numbers
    }

    private fun symbolsList(): ArrayList<String>{

        val symbols = arrayListOf(" ")

        for (i in 33..47) {
            symbols.add(i.toChar().toString())
        }

        for (i in 58..64) {
            symbols.add(i.toChar().toString())
        }

        for (i in 123..126) {
            symbols.add(i.toChar().toString())
        }

        return symbols
    }
}
