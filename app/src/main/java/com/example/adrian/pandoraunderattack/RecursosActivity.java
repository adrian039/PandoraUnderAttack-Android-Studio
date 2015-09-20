package com.example.adrian.pandoraunderattack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author Esteban Agüero Pérez
 */
public class RecursosActivity extends MapasActivity {
    private TextView Recurso1;
    private TextView Recurso2;
    private TextView Recurso3;
    private int valorR1;
    private int valorR2;
    private int valorR3;
    private Button actualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recursos_activity);

        //Iniciacion de los "labels"
        Recurso1=(TextView) findViewById(R.id.dato1);
        Recurso2=(TextView) findViewById(R.id.dato2);
        Recurso3=(TextView) findViewById(R.id.dato3);
        updateRecursos();
    }

    public void updateRecursos(){
        try{
            //valores de los recursos
            valorR1=MapasActivity.getRecurso1();
            valorR2=MapasActivity.getRecurso2();
            valorR3=MapasActivity.getRecurso3();
            //asignacion de los valores a sus resectivas textView
            Recurso1.setText(Integer.toString(valorR1));
            Recurso2.setText(Integer.toString(valorR2));
            Recurso3.setText(Integer.toString(valorR3));

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error al acualizar recursos", Toast.LENGTH_SHORT).show();
        }

    }//actualiza la lista de recursos

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recursos_activiy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
