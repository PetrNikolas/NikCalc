package cz.petrnikolas.nikcalc;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    /**
     * Deklarace potřebných objektů TextView (pro zobrazování dat).
     */
    TextView Obraz;
    TextView Cislo1;
    TextView Cislo2;
    TextView Operace;

    /**
     * V onCreate provedeme inicializaci objektů TextView (pro zobrazování dat).
     * @param savedInstanceState parametr je při prvním spuštění aplikace null. Slouží pro ukládání dat, například při změně orientace.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        Obraz=(TextView)findViewById(R.id.Obrazovka);
        Obraz.setText("0");

        Cislo1=(TextView)findViewById(R.id.Cislo1);
        Cislo1.setText("NaN");

        Cislo2=(TextView)findViewById(R.id.Cislo2);
        Cislo2.setText("NaN");

        Operace=(TextView)findViewById(R.id.Znamenko);

        Operace.setText("...");
    }

    /**
     *Privátní proměnné C1,C2,Vys slouží k ukládání čísel.
     *Proměnná metoda slouží pro uchování záznamu s jakou metodou chce uživatel počítat.
     *Boolean proměnné prosel a C1Nastaven jsou pro řídící účeli aplikace. Pomocí nich ošetřujeme možné problémy se zadáváním čísel a ujištění se, že máme zadáno C1 když chceme počítat dál.
     */
    private boolean prosel = false;
    private boolean C1Nastaven = false;

    private double C1;
    private double C2;
    private double Vys;
    private int metoda=0;

    /**
     * Logika této metody je popsána v textech "Android programování 2 Calc" kapitola 1.2 Analýza a návrh aplikace. Detailní algoritmus se nachází na obrázku č. 9-Diagram připisování čísel
     * @param sender díky tomuto parametru dostaneme ID tlačítka na které uživatel kliknul.
     */
    public void ZapisCislo(View sender){
        Button tlac = (Button)sender;
        float number = Float.parseFloat(tlac.getText().toString());

        int pom = Obraz.length();
        if(pom>=9){

            if(pom>=9 && prosel==false){
                if(number==0){
                    Obraz.setText("0");
                    return;
                }
                else{
                    Obraz.setText(tlac.getText());
                    prosel = true;
                }
            }
            return;
        }
        if(prosel == false){

            if(number==0){
                return;
            }
            else{
                Obraz.setText(tlac.getText());
                prosel = true;
            }
        }
        else {
            Obraz.append(tlac.getText());
        }
    }

    /**
     * Metoda se ukončí, pokud je na obrazovce už více než 8 znaků.
     * Pokud se "." na obrazovce nenachází, přidá ji na konec řetězce a nastaví true u řídící proměnné prosel.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    public void ZapisCarku(View v){
        if (Obraz.getText().length()>=8){
            return;
        }
        String test = Obraz.getText().toString();
        String hled=".";
        if (test.indexOf(hled)<0){
            Obraz.append(".");
            prosel = true;
        }

    }

    /**
     * Metoda která vynuluje všechny použité proměnné i zobrazovací textView.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    public void VymazVse(View v){
        prosel = false;
        C1=0;
        C2=0;
        Vys=0;
        metoda=5;

        Obraz.setText("0");
        Cislo1.setText("NaN");
        Cislo2.setText("NaN");
        Operace.setText("...");
    }

    /**
     * Na základě délky řetězce buď smaže poslední znak, anebo nastaví textView Obraz na "0".
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    public void VymazJeden(View v){
        int delka = Obraz.length();

        if (delka >1){
            String puvodnistring = Obraz.getText().toString();
            String substring = puvodnistring.substring(0, delka-1);
            Obraz.setText(substring);
        }

        else if (delka >0){
            Obraz.setText("0");
            prosel = false;
        }
    }

    /** Metoda na tlačítku +/-
     * Pokud je na obrazovce 0, nestane se nic.
     * Našte řetězec a zjistí na jaké pozici je "-". Pokud výsledek není -1, nahradíme znak "-" prázdným řetězcem "".
     * Pokud jsou souřadnice znaménka "-" <=8, připíšeme "-" na začátek řetězce.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    public void Zapor(View v){
        float k=Float.parseFloat(Obraz.getText().toString());
        if (k==0){
            return;
        }
        String test = Obraz.getText().toString();
        String hled="-";

        if (test.indexOf(hled)!=-1){
            test=test.replace("-","");
            Obraz.setText(test);
            return;
        }
        else{
            if (test.length()<=8){
                test="-"+test;
                Obraz.setText(test);
            }
        }
    }

    /**
     * Protože budeme odmocňovat, je třeba vymazat všechny proměnné.
     * Pokud bude výsledek delší než 9 znaků, vytvoříme substring prvních 9 čísel. Pomocí podmínek ošetříme, aby se nezobrazovaly nežádoucí čísla.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    public void Odmocni(View v){
        double number = Math.sqrt(Double.parseDouble(Obraz.getText().toString()));
        VymazVse(v);

        if (number>=0){
            String Vysledek = (String.valueOf(number));

            if (Vysledek.length()>=9){
                String substring = Vysledek.substring(0, 9);
                if (substring.equals("1.0000000")||substring.equals("0.9999998")||substring.equals("0.9999999")){
                    Obraz.setText("1");
                    return;
                }
                else{
                    Obraz.setText(substring);
                    return;
                }
            }
            if (number!=0){
                Double Zbav = Double.parseDouble(Vysledek);
                int Zbav2 = (int)Math.round(Zbav);
                Obraz.setText(String.valueOf(Zbav2));
            }
            else {
                Obraz.setText("0");
            }
        }
        else {
            Toast.makeText(this, "Nelze odmocnit záporné číslo!", Toast.LENGTH_LONG).show();
            VymazVse(v);
        }
    }

    /**
     * Následující metody Secti, odecti, vynasob, vydel budou mít stejný princip.
     * Uloží do globální proměnné C1 číslo na obrazovce a z metodu (číslo 1-4). Také nastaví potřebné řídící proměnné.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    public void Secti(View v){
        C1 = Double.parseDouble(Obraz.getText().toString());

        metoda = 1;
        Obraz.setText("0");
        prosel = false;
        C1Nastaven = true;

        Cislo1.setText(String.valueOf(C1));
        Operace.setText("+");
    }

    public void Odecti(View v){
        C1 = Double.parseDouble(Obraz.getText().toString());

        metoda = 2;
        Obraz.setText("0");
        prosel = false;
        C1Nastaven = true;

        Cislo1.setText(String.valueOf(C1));
        Operace.setText("-");
    }

    public void Vynasob(View V){
        C1 = Double.parseDouble(Obraz.getText().toString());

        metoda = 3;
        Obraz.setText("0");
        prosel = false;
        C1Nastaven = true;

        Cislo1.setText(String.valueOf(C1));
        Operace.setText("*");
    }

    public void Vydel(View V){
        C1 = Double.parseDouble(Obraz.getText().toString());

        metoda = 4;
        Obraz.setText("0");
        prosel = false;
        C1Nastaven = true;

        Cislo1.setText(String.valueOf(C1));
        Operace.setText("/");

    }

    /**
     * Pokud se metoda nerovná 5, znamená to, že je nastavená a lze načíst do paměti C2.
     * Podle toho o jakou metodu se jedná se provede patřičná operace pomocí switch case.
     * Při dělení vznikají dlouhé řetězce čísel, proto je třeba po počítání zjistit, na jakém místě je desetinná čárka a podle toho zaokrouhlit.
     * @param v Zajistí nám viditelnost této metody, abychom k ní mohli přistoupit z xml, tzn. metoda onclick
     */
    public void Vypocti(View v){

        if(C1Nastaven==true){
            if (metoda!=5){
                C2 = Double.parseDouble(Obraz.getText().toString());
                Cislo2.setText(String.valueOf(C2));
            }
            switch (metoda){
                case 1:{
                    Vys = C1 + C2;
                    metoda=5;
                    break;
                }

                case 2:{
                    Vys = C1 - C2;
                    metoda=5;
                    break;
                }

                case 3:{
                    Vys = C1 * C2;
                    metoda=5;
                    break;
                }

                case 4:{

                    if (C2!=0){
                        Vys = C1 / C2;
                        metoda=5;
                        break;
                    }

                    else{
                        Toast.makeText(this, "Nelze dělit nulou!", Toast.LENGTH_LONG).show();
                        VymazVse(v);
                        break;
                    }
                }
                case 5:{
                    break;
                }
            }

            if (Vys==0){
                Obraz.setText("0");
            }

            else{

                DecimalFormat DF = new DecimalFormat("#.########");
                String Vysl=DF.format(Vys);
                Vysl=Vysl.replace(",",".");

                int poz=Vysl.indexOf(".");
                if (poz >= 8) {
                    Toast.makeText(this, "Byl překročen limit 9 čísel", Toast.LENGTH_LONG).show();
                    VymazVse(v);
                    return;
                }

                if (Vysl.length()>=10){
                    String substring = Vysl.substring(0, 9);
                    Obraz.setText(substring);
                    return;
                }
                else {
                    Obraz.setText(Vysl);
                }

                C1Nastaven=false;
                prosel = false;
            }
        }
    }
}
