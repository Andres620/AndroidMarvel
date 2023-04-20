package com.andresparra.androidmarvel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;

import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLComponents;
import cafsoft.foundation.URLQueryItem;
import cafsoft.foundation.URLSession;

public class MainActivity extends AppCompatActivity {

    private String superHero;
    private EditText inputSuperhero;
    private TextView dataSuperhero;
    private Button btnSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputSuperhero = findViewById(R.id.inputSuperhero);
        btnSearch = findViewById(R.id.btnSearch);
        dataSuperhero = findViewById(R.id.dataSuperhero);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                superHero = inputSuperhero.getText().toString();
                searchSuperhero(superHero);
            }
        });
    }

    public void searchSuperhero(String superHero){
        URLComponents comp = new URLComponents();

        comp.setScheme("https");
        comp.setHost("gateway.marvel.com");
        comp.setPath("/v1/public/characters");
        comp.setQueryItems(new URLQueryItem[]{
                new URLQueryItem("hash", "d69767aae7d31aa0d251bde8ac5d0765"),
                new URLQueryItem("ts", "6620"),
                new URLQueryItem("apikey", "2a98b6481603cfd522db9763d5adaf48"),
                new URLQueryItem("name", superHero)
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
                    dataSuperhero.setText(data.toText()); //temporalmente
                }
            }else{
                Log.d("Error", "Error de red");
            }
        }).resume(); //con el resume se lanza la tarea en un hilo de ejecución
    }
}