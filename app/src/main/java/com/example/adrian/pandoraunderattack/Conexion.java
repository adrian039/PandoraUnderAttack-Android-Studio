package com.example.adrian.pandoraunderattack;

import android.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * Created by adrian on 9/09/15.
 */
public class Conexion {
    Socket sockete = null;
    BufferedReader lector = null;
    PrintWriter escritor = null;
    Thread principal=null;
    Gson gson=new Gson();
    public Conexion(){

    }
    public void Conectar(){
         principal = new Thread(new Runnable() {
            public void run() {
                try {
                    sockete = new Socket("172.26.34.133", 8080);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        principal.start();
    }
    public void Leer(){
        Thread leer_hilo=new Thread(new Runnable(){
            public void run(){
                try{
                    lector=new BufferedReader(new InputStreamReader(sockete.getInputStream()));
                    while(true){

                        JsonParser parser = new JsonParser();
                        String mensaje= lector.readLine();
                        JsonElement elemento = parser.parse(mensaje);
                        String mensaje_in=elemento.getAsJsonObject().get("mensaje").getAsString();
                    }

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

        });
        leer_hilo.start();

    }
    public void Escribir(String dato){

                try{
                    escritor= new PrintWriter(sockete.getOutputStream(), true);
                    escritor.println(dato);
                    System.out.println(dato);
                }catch(Exception ex){
                    ex.printStackTrace();
                }

    }


}
