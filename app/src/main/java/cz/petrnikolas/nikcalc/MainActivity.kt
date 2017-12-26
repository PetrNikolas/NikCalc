package cz.petrnikolas.nikcalc

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    /**
     * Deklarace potřebných objektů TextView (pro zobrazování dat).
     */
    internal lateinit var Screen: TextView
    internal lateinit var FirstNumber: TextView
    internal lateinit var SecondNumber: TextView
    internal lateinit var Operations: TextView

    /**
     * Privátní proměnné Number1,Number2,Result slouží k ukládání čísel.
     * Proměnná Method slouží pro uchování záznamu s jakou metodou chce uživatel počítat.
     * Boolean proměnné valid a SetFirstNumber jsou pro řídící účeli aplikace. Pomocí nich ošetřujeme možné problémy se zadáváním čísel a ujištění se, že máme zadáno Number1 když chceme počítat dál.
     */
    private var valid = false
    private var SetFirstNumber = false

    private var Number1: Double = 0.toDouble()
    private var Number2: Double = 0.toDouble()
    private var Result: Double = 0.toDouble()
    private var Method = 0

    /**
     * V onCreate provedeme inicializaci objektů TextView (pro zobrazování dat).
     * @param savedInstanceState parametr je při prvním spuštění aplikace null. Slouží pro ukládání dat, například při změně orientace.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        Screen = findViewById(R.id.Obrazovka) as TextView
        Screen.text = "0"

        FirstNumber = findViewById(R.id.Cislo1) as TextView
        FirstNumber.text = ""

        SecondNumber = findViewById(R.id.Cislo2) as TextView
        SecondNumber.text = ""

        Operations = findViewById(R.id.Znamenko) as TextView

        Operations.text = ""
    }

    /**
     * Logika této metody je popsána v textech "Android programování 2 Calc" kapitola 1.2 Analýza a návrh aplikace. Detailní algoritmus se nachází na obrázku č. 9-Diagram připisování čísel
     * @param sender díky tomuto parametru dostaneme ID tlačítka na které uživatel kliknul.
     */
    fun ZapisCislo(sender: View) {
        val btn = sender as Button
        val number = java.lang.Float.parseFloat(btn.text.toString())

        val pom = Screen.length()
        if (pom >= 9) {

            if (pom >= 9 && valid === false) {
                if (number == 0f) {
                    Screen.text = "0"
                    return
                } else {
                    Screen.text = btn.text
                    valid = true
                }
            }
            return
        }
        if (valid === false) {

            if (number == 0f) {
                return
            } else {
                Screen.text = btn.text
                valid = true
            }
        } else {
            Screen.append(btn.text)
        }
    }

    /**
     * Metoda se ukončí, pokud je na obrazovce už více než 8 znaků.
     * Pokud se "." na obrazovce nenachází, přidá ji na konec řetězce a nastaví true u řídící proměnné valid.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. Method onclick
     */
    fun ZapisCarku(v: View) {
        if (Screen.text.length >= 8) {
            return
        }
        val test = Screen.text.toString()
        val symbol = "."
        if (test.indexOf(symbol) < 0) {
            Screen.append(".")
            valid = true
        }

    }

    /**
     * Metoda která vynuluje všechny použité proměnné i zobrazovací textView.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. Method onclick
     */
    fun VymazVse(v: View) {
        valid = false
        Number1 = 0.0
        Number2 = 0.0
        Result = 0.0
        Method = 5

        Screen.text = "0"
        FirstNumber.text = ""
        SecondNumber.text = ""
        Operations.text = ""
    }

    /**
     * Na základě délky řetězce buď smaže poslední znak, anebo nastaví textView Screen na "0".
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. Method onclick
     */
    fun VymazJeden(v: View) {
        val lenght = Screen.length()

        if (lenght > 1) {
            val previousString = Screen.text.toString()
            val substring = previousString.substring(0, lenght - 1)
            Screen.setText(substring)
        } else if (lenght > 0) {
            Screen.text = "0"
            valid = false
        }
    }

    /** Metoda na tlačítku +/-
     * Pokud je na obrazovce 0, nestane se nic.
     * Našte řetězec a zjistí na jaké pozici je "-". Pokud výsledek není -1, nahradíme znak "-" prázdným řetězcem "".
     * Pokud jsou souřadnice znaménka "-" <=8, připíšeme "-" na začátek řetězce.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. Method onclick
     */
    fun Zapor(v: View) {
        val k = java.lang.Float.parseFloat(Screen.text.toString())
        if (k == 0f) {
            return
        }
        var test = Screen.text.toString()
        val symbol = "-"

        if (test.indexOf(symbol) != -1) {
            test = test.replace("-", "")
            Screen.text = test
            return
        } else {
            if (test.length <= 8) {
                test = "-" + test
                Screen.text = test
            }
        }
    }

    /**
     * Protože budeme odmocňovat, je třeba vymazat všechny proměnné.
     * Pokud bude výsledek delší než 9 znaků, vytvoříme substring prvních 9 čísel. Pomocí podmínek ošetříme, aby se nezobrazovaly nežádoucí čísla.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. Method onclick
     */
    fun Odmocni(v: View) {
        val number = Math.sqrt(java.lang.Double.parseDouble(Screen.text.toString()))
        VymazVse(v)

        if (number >= 0) {
            val result = number.toString()

            if (result.length >= 9) {
                val substring = result.substring(0, 9)
                if (substring == "1.0000000" || substring == "0.9999998" || substring == "0.9999999") {
                    Screen.text = "1"
                    return
                } else {
                    Screen.setText(substring)
                    return
                }
            }
            if (number != 0.0) {
                val Zbav = java.lang.Double.parseDouble(result)
                val Zbav2 = Math.round(Zbav).toInt()
                Screen.text = Zbav2.toString()
            } else {
                Screen.text = "0"
            }
        } else {
            Toast.makeText(this, "Nelze odmocnit záporné číslo!", Toast.LENGTH_LONG).show()
            VymazVse(v)
        }
    }

    /**
     * Následující metody Secti, odecti, vynasob, vydel budou mít stejný princip.
     * Uloží do globální proměnné Number1 číslo na obrazovce a z metodu (číslo 1-4). Také nastaví potřebné řídící proměnné.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. Method onclick
     */
    fun Secti(v: View) {
        Number1 = java.lang.Double.parseDouble(Screen.text.toString())

        Method = 1
        Screen.text = "0"
        valid = false
        SetFirstNumber = true

        FirstNumber.text = Number1.toString()
        Operations.text = "+"
    }

    fun Odecti(v: View) {
        Number1 = java.lang.Double.parseDouble(Screen.text.toString())

        Method = 2
        Screen.text = "0"
        valid = false
        SetFirstNumber = true

        FirstNumber.text = Number1.toString()
        Operations.text = "-"
    }

    fun Vynasob(V: View) {
        Number1 = java.lang.Double.parseDouble(Screen.text.toString())

        Method = 3
        Screen.text = "0"
        valid = false
        SetFirstNumber = true

        FirstNumber.text = Number1.toString()
        Operations.text = "*"
    }

    fun Vydel(V: View) {
        Number1 = java.lang.Double.parseDouble(Screen.text.toString())

        Method = 4
        Screen.text = "0"
        valid = false
        SetFirstNumber = true

        FirstNumber.text = Number1.toString()
        Operations.text = "/"

    }

    /**
     * Pokud se Method nerovná 5, znamená to, že je nastavená a lze načíst do paměti Number2.
     * Podle toho o jakou metodu se jedná se provede patřičná operace pomocí switch case.
     * Při dělení vznikají dlouhé řetězce čísel, proto je třeba po počítání zjistit, na jakém místě je desetinná čárka a podle toho zaokrouhlit.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. Method onclick
     */
    fun Vypocti(v: View) {

        if (SetFirstNumber == true) {
            if (Method != 5) {
                Number2 = java.lang.Double.parseDouble(Screen.text.toString())
                SecondNumber.text = Number2.toString()
            }
            when (Method) {
                1 -> {
                    Result = Number1 + Number2
                    Method = 5
                }

                2 -> {
                    Result = Number1 - Number2
                    Method = 5
                }

                3 -> {
                    Result = Number1 * Number2
                    Method = 5
                }

                4 -> {

                    if (Number2 != 0.0) {
                        Result = Number1 / Number2
                        Method = 5
                        // break
                    } else {
                        Toast.makeText(this, "Nelze dělit nulou!", Toast.LENGTH_LONG).show()
                        VymazVse(v)
                        // break
                    }
                }
                5 -> {
                }
            }

            if (Result == 0.0) {
                Screen.text = "0"
            } else {

                val DF = DecimalFormat("#.########")
                var result = DF.format(Result)
                result = result.replace(",", ".")

                val poz = result.indexOf(".")
                if (poz >= 8) {
                    Toast.makeText(this, "Byl překročen limit 9 čísel", Toast.LENGTH_LONG).show()
                    VymazVse(v)
                    return
                }

                if (result.length >= 10) {
                    val substring = result.substring(0, 9)
                    Screen.setText(substring)
                    return
                } else {
                    Screen.text = result
                }

                SetFirstNumber = false
                valid = false
            }
        }
    }
}
