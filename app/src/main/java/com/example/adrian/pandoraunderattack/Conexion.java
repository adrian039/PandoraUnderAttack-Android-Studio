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
    public static String mensaje;
    Socket sockete = null;
    BufferedReader lector = null;
    PrintWriter escritor = null;
    Thread principal=null;
    String mensaje_in=null;
    Gson gson=new Gson();
    public Conexion(){
    }
    public void Conectar(){
         principal = new Thread(new Runnable() {
            public void run() {
                try {
                    sockete = new Socket("192.168.0.124", 8080);
                    Leer();
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
                    System.out.println(lector.toString());
                    while(true) {
                        if (lector == null) {

                        } else {
                            String valor = lector.readLine();
                            Conexion.mensaje = valor.toString();
                            System.out.print(Conexion.mensaje);

                        }
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
    public String Entrada(){
        System.out.println(Conexion.mensaje);
        return Conexion.mensaje;
    }


}
