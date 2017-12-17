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
    internal lateinit var Obraz: TextView
    internal lateinit var Cislo1: TextView
    internal lateinit var Cislo2: TextView
    internal lateinit var Operace: TextView

    /**
     * Privátní proměnné C1,C2,Vys slouží k ukládání čísel.
     * Proměnná metoda slouží pro uchování záznamu s jakou metodou chce uživatel počítat.
     * Boolean proměnné prosel a C1Nastaven jsou pro řídící účeli aplikace. Pomocí nich ošetřujeme možné problémy se zadáváním čísel a ujištění se, že máme zadáno C1 když chceme počítat dál.
     */
    private var prosel = false
    private var C1Nastaven = false

    private var C1: Double = 0.toDouble()
    private var C2: Double = 0.toDouble()
    private var Vys: Double = 0.toDouble()
    private var metoda = 0

    /**
     * V onCreate provedeme inicializaci objektů TextView (pro zobrazování dat).
     * @param savedInstanceState parametr je při prvním spuštění aplikace null. Slouží pro ukládání dat, například při změně orientace.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        Obraz = findViewById(R.id.Obrazovka) as TextView
        Obraz.text = "0"

        Cislo1 = findViewById(R.id.Cislo1) as TextView
        Cislo1.text = "NaN"

        Cislo2 = findViewById(R.id.Cislo2) as TextView
        Cislo2.text = "NaN"

        Operace = findViewById(R.id.Znamenko) as TextView

        Operace.text = "..."
    }

    /**
     * Logika této metody je popsána v textech "Android programování 2 Calc" kapitola 1.2 Analýza a návrh aplikace. Detailní algoritmus se nachází na obrázku č. 9-Diagram připisování čísel
     * @param sender díky tomuto parametru dostaneme ID tlačítka na které uživatel kliknul.
     */
    fun ZapisCislo(sender: View) {
        val tlac = sender as Button
        val number = java.lang.Float.parseFloat(tlac.text.toString())

        val pom = Obraz.length()
        if (pom >= 9) {

            if (pom >= 9 && prosel == false) {
                if (number == 0f) {
                    Obraz.text = "0"
                    return
                } else {
                    Obraz.text = tlac.text
                    prosel = true
                }
            }
            return
        }
        if (prosel == false) {

            if (number == 0f) {
                return
            } else {
                Obraz.text = tlac.text
                prosel = true
            }
        } else {
            Obraz.append(tlac.text)
        }
    }

    /**
     * Metoda se ukončí, pokud je na obrazovce už více než 8 znaků.
     * Pokud se "." na obrazovce nenachází, přidá ji na konec řetězce a nastaví true u řídící proměnné prosel.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    fun ZapisCarku(v: View) {
        if (Obraz.text.length >= 8) {
            return
        }
        val test = Obraz.text.toString()
        val hled = "."
        if (test.indexOf(hled) < 0) {
            Obraz.append(".")
            prosel = true
        }

    }

    /**
     * Metoda která vynuluje všechny použité proměnné i zobrazovací textView.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    fun VymazVse(v: View) {
        prosel = false
        C1 = 0.0
        C2 = 0.0
        Vys = 0.0
        metoda = 5

        Obraz.text = "0"
        Cislo1.text = "NaN"
        Cislo2.text = "NaN"
        Operace.text = "..."
    }

    /**
     * Na základě délky řetězce buď smaže poslední znak, anebo nastaví textView Obraz na "0".
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    fun VymazJeden(v: View) {
        val delka = Obraz.length()

        if (delka > 1) {
            val puvodnistring = Obraz.text.toString()
            val substring = puvodnistring.substring(0, delka - 1)
            Obraz.setText(substring)
        } else if (delka > 0) {
            Obraz.text = "0"
            prosel = false
        }
    }

    /** Metoda na tlačítku +/-
     * Pokud je na obrazovce 0, nestane se nic.
     * Našte řetězec a zjistí na jaké pozici je "-". Pokud výsledek není -1, nahradíme znak "-" prázdným řetězcem "".
     * Pokud jsou souřadnice znaménka "-" <=8, připíšeme "-" na začátek řetězce.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    fun Zapor(v: View) {
        val k = java.lang.Float.parseFloat(Obraz.text.toString())
        if (k == 0f) {
            return
        }
        var test = Obraz.text.toString()
        val hled = "-"

        if (test.indexOf(hled) != -1) {
            test = test.replace("-", "")
            Obraz.text = test
            return
        } else {
            if (test.length <= 8) {
                test = "-" + test
                Obraz.text = test
            }
        }
    }

    /**
     * Protože budeme odmocňovat, je třeba vymazat všechny proměnné.
     * Pokud bude výsledek delší než 9 znaků, vytvoříme substring prvních 9 čísel. Pomocí podmínek ošetříme, aby se nezobrazovaly nežádoucí čísla.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    fun Odmocni(v: View) {
        val number = Math.sqrt(java.lang.Double.parseDouble(Obraz.text.toString()))
        VymazVse(v)

        if (number >= 0) {
            val Vysledek = number.toString()

            if (Vysledek.length >= 9) {
                val substring = Vysledek.substring(0, 9)
                if (substring == "1.0000000" || substring == "0.9999998" || substring == "0.9999999") {
                    Obraz.text = "1"
                    return
                } else {
                    Obraz.setText(substring)
                    return
                }
            }
            if (number != 0.0) {
                val Zbav = java.lang.Double.parseDouble(Vysledek)
                val Zbav2 = Math.round(Zbav).toInt()
                Obraz.text = Zbav2.toString()
            } else {
                Obraz.text = "0"
            }
        } else {
            Toast.makeText(this, "Nelze odmocnit záporné číslo!", Toast.LENGTH_LONG).show()
            VymazVse(v)
        }
    }

    /**
     * Následující metody Secti, odecti, vynasob, vydel budou mít stejný princip.
     * Uloží do globální proměnné C1 číslo na obrazovce a z metodu (číslo 1-4). Také nastaví potřebné řídící proměnné.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    fun Secti(v: View) {
        C1 = java.lang.Double.parseDouble(Obraz.text.toString())

        metoda = 1
        Obraz.text = "0"
        prosel = false
        C1Nastaven = true

        Cislo1.text = C1.toString()
        Operace.text = "+"
    }

    fun Odecti(v: View) {
        C1 = java.lang.Double.parseDouble(Obraz.text.toString())

        metoda = 2
        Obraz.text = "0"
        prosel = false
        C1Nastaven = true

        Cislo1.text = C1.toString()
        Operace.text = "-"
    }

    fun Vynasob(V: View) {
        C1 = java.lang.Double.parseDouble(Obraz.text.toString())

        metoda = 3
        Obraz.text = "0"
        prosel = false
        C1Nastaven = true

        Cislo1.text = C1.toString()
        Operace.text = "*"
    }

    fun Vydel(V: View) {
        C1 = java.lang.Double.parseDouble(Obraz.text.toString())

        metoda = 4
        Obraz.text = "0"
        prosel = false
        C1Nastaven = true

        Cislo1.text = C1.toString()
        Operace.text = "/"

    }

    /**
     * Pokud se metoda nerovná 5, znamená to, že je nastavená a lze načíst do paměti C2.
     * Podle toho o jakou metodu se jedná se provede patřičná operace pomocí switch case.
     * Při dělení vznikají dlouhé řetězce čísel, proto je třeba po počítání zjistit, na jakém místě je desetinná čárka a podle toho zaokrouhlit.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    fun Vypocti(v: View) {

        if (C1Nastaven == true) {
            if (metoda != 5) {
                C2 = java.lang.Double.parseDouble(Obraz.text.toString())
                Cislo2.text = C2.toString()
            }
            when (metoda) {
                1 -> {
                    Vys = C1 + C2
                    metoda = 5
                }

                2 -> {
                    Vys = C1 - C2
                    metoda = 5
                }

                3 -> {
                    Vys = C1 * C2
                    metoda = 5
                }

                4 -> {

                    if (C2 != 0.0) {
                        Vys = C1 / C2
                        metoda = 5
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

            if (Vys == 0.0) {
                Obraz.text = "0"
            } else {

                val DF = DecimalFormat("#.########")
                var Vysl = DF.format(Vys)
                Vysl = Vysl.replace(",", ".")

                val poz = Vysl.indexOf(".")
                if (poz >= 8) {
                    Toast.makeText(this, "Byl překročen limit 9 čísel", Toast.LENGTH_LONG).show()
                    VymazVse(v)
                    return
                }

                if (Vysl.length >= 10) {
                    val substring = Vysl.substring(0, 9)
                    Obraz.setText(substring)
                    return
                } else {
                    Obraz.text = Vysl
                }

                C1Nastaven = false
                prosel = false
            }
        }
    }
}
