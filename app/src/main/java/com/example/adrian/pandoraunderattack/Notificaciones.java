package com.example.adrian.pandoraunderattack;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**

/**
 * Created by adrian on 22/09/15.
 */
public class Notificaciones extends MainActivity{
    public Notificaciones(){

    }
    public void Verificar(String usuario){
        JsonParser parser = new JsonParser();
        JsonObject o = new JsonObject();
        o.addProperty("tipo","HayNotificaciones");
        o.addProperty("usuario",usuario);
        String solicitud = gson.toJson(o);
        conectar.Escribir(solicitud);
        }

}
