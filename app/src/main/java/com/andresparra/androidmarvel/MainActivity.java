package com.andresparra.androidmarvel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.net.URL;

import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLComponents;
import cafsoft.foundation.URLQueryItem;
import cafsoft.foundation.URLSession;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URLComponents comp = new URLComponents();

        comp.setScheme("https");
        comp.setHost("gateway.marvel.com");
        comp.setPath("/v1/public/characters");
        comp.setQueryItems(new URLQueryItem[]{
                new URLQueryItem("hash", "d69767aae7d31aa0d251bde8ac5d0765"),
                new URLQueryItem("ts", "6620"),
                new URLQueryItem("apikey", "2a98b6481603cfd522db9763d5adaf48"),
                new URLQueryItem("name", "thor")
        });
        Log.d("url", comp.getURL().toString());

        URL url = comp.getURL();

        URLSession.getShared().dataTask(url, (data, response, error) -> {
            //cuando ejecua este bloque de error es porque recibio una respuesta
            //ya sea el response(puede ser un status 200 - o errores de respuestastatus 400)
            // o un error(error de red - No Internet)
            HTTPURLResponse resp = (HTTPURLResponse) response;
            if (error == null){
                if(resp.getStatusCode()==200){
                    Log.d("resp", data.toText());
                }
            }else{
                Log.d("Error", "Error de red");
            }
        }).resume(); //con el resume se lanza la tarea en un hilo de ejecución
    }
}