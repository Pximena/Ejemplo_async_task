package com.example.equipo14.ejemplo_async_task;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final boolean cancelar=false;
    private final String tag = "text";
    private TextView mensaje2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mensaje2 = (TextView) findViewById(R.id.mensaje);
        Button probar = (Button) findViewById(R.id.btn_1);
        probar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(tag,"haciendo otra cosa sobre el HILO PRINCIPAL a la vez que carga");
                Toast toast = Toast.makeText(getApplicationContext(),"hacinedo otra cosa sobre el hilo principal a la vez que craga",Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        DescargarImagenes miTareaAsincrona = new DescargarImagenes(cancelar);
        miTareaAsincrona.execute();

    }
    private class DescargarImagenes extends AsyncTask <String, Float, Integer> {

        private boolean cancelarSiHayMas100Archivos;
        private ProgressBar miBarraDeProgreso;

        public DescargarImagenes(boolean cancelarSiHayMas100Archivos) {
            this.cancelarSiHayMas100Archivos = cancelarSiHayMas100Archivos;
        }

        @Override
        protected void onPreExecute() {
            mensaje2.setText("ANTES de EMPEZAR la descarga. Hilo PRINCIPAL");
            Log.v(tag, "ANTES de EMPEZAR la descarga. Hilo PRINCIPAL");

            miBarraDeProgreso = (ProgressBar) findViewById(R.id.pb_1);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            int cantidadImagenesDescargadas = 0;
            float progreso = 0.0f;
            while (!isCancelled() && cantidadImagenesDescargadas<200){
                cantidadImagenesDescargadas++;
                Log.v(tag, "Imagen descargada número "+cantidadImagenesDescargadas+". Hilo en SEGUNDO PLANO");
                try {

                    Thread.sleep((long) (Math.random()*10));
                } catch (InterruptedException e) {
                    cancel(true);
                    e.printStackTrace();
                }
                progreso+=0.5;
                publishProgress(progreso);
                if (cancelarSiHayMas100Archivos && cantidadImagenesDescargadas>100){
                    cancel(true);
                }
            }

            return cantidadImagenesDescargadas;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            mensaje2.setText("DESPUÉS de TERMINAR la descarga. Se han descarcado "+integer+" imágenes. Hilo PRINCIPAL");
            Log.v(tag, "DESPUÉS de TERMINAR la descarga. Se han descarcado "+integer+" imágenes. Hilo PRINCIPAL");
            mensaje2.setTextColor(Color.GREEN);
            //super.onPostExecute(integer);
        }

        @Override
        protected void onProgressUpdate(Float... porcentaje) {
            mensaje2.setText("Progreso descarga: "+porcentaje[0]+"%. Hilo PRINCIPAL");
            Log.v(tag, "Progreso descarga: "+porcentaje[0]+"%. Hilo PRINCIPAL");

            miBarraDeProgreso.setProgress( Math.round(porcentaje[0]) );
        }
        @Override
        protected void onCancelled(Integer cantidadprocesados) {
            mensaje2.setText("DESPUÉS de CANCELAR la descarga. Se han descarcado "+cantidadprocesados+" imágenes. Hilo PRINCIPAL");
            Log.v(tag, "DESPUÉS de CANCELAR la descarga. Se han descarcado "+cantidadprocesados+" imágenes. Hilo PRINCIPAL");

            mensaje2.setTextColor(Color.RED);
        }
        }
    }




