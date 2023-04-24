package com.andresparra.androidmarvel;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andresparra.androidmarvel.service.marvel.model.Result;
import com.andresparra.androidmarvel.service.marvel.model.Root;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

import cafsoft.foundation.Data;
import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLComponents;
import cafsoft.foundation.URLQueryItem;
import cafsoft.foundation.URLSession;

public class MainActivity extends AppCompatActivity {

    private String superHero;
    private EditText inputSuperhero;
    private ImageView resultImage;
    private TextView resultDescription;
    private Button btnSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputSuperhero = findViewById(R.id.inputSuperhero);
        btnSearch = findViewById(R.id.btnSearch);
        resultImage = findViewById(R.id.resultImage);
        resultDescription = findViewById(R.id.resultDescription);

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
                    //Log.d("resp", data.toText());
                    Gson gson = new Gson();
                    Root root = gson.fromJson(data.toText(), Root.class);

                    runOnUiThread(()->{
                        showInfo(root);
                    });

                }else{
                    Log.d("Message ", "Server error " + resp.getStatusCode());
                }
            }else{
                Log.d("Message", "Network error");
            }
        }).resume(); //con el resume se lanza la tarea en un hilo de ejecución
    }

    public void showInfo(Root root){
        if(root != null){
            if (root.data.results.size() > 0){
                Result result = root.data.results.get(0);
                Log.d("Message", result.description);

                resultDescription.setText(result.description);

                String strImageURL = result.thumbnail.path + "." + result.thumbnail.extension;
                strImageURL = strImageURL.replace("http:", "https:");
                URL url = null;

                try {
                    url = new URL(strImageURL);
                } catch ( IOException e){
                }

                URLSession.getShared().dataTask(url, (data, response, error) -> {
                    HTTPURLResponse resp = (HTTPURLResponse) response;
                    if (error == null){
                        if(resp.getStatusCode()==200){
                            final Bitmap image = dataToImage(data);

                            runOnUiThread(()->{
                                showImage(image);
                            });

                        }else{
                            Log.d("Message ", "Server error " + resp.getStatusCode());
                        }
                    }else{
                        Log.d("Message", "Network error");
                    }
                }).resume();
            }
        }
    }

    public Bitmap dataToImage(Data data){
        Bitmap bitmap = BitmapFactory.decodeByteArray(data.toBytes(),0, data.length());

        return bitmap;
    }

    public void showImage(Bitmap bitmap){
        resultImage.setImageBitmap(bitmap);
    }

}