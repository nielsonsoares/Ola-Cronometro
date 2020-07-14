package com.example.nlink.hellocronometro;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean isRunning;

    private TextView btnCronometro;
    private Button btnStartStop;
    private Button btnZerar;

    private int minuto;
    private int segundo;
    private int mili;

    private CronometroTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRunning = false;

        task = new CronometroTask();

        btnCronometro = findViewById(R.id.txtCronometro);
        String[] dados = btnCronometro.getText().toString().split(":");
        minuto = Integer.parseInt(dados[0]);
        segundo = Integer.parseInt(dados[1]);
        mili = Integer.parseInt(dados[2]);

        btnStartStop = findViewById(R.id.btnStartStop);

        btnStartStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(isRunning){
                    btnStartStop.setText("INICIAR");
                    isRunning =  false;
                    task.cancel(true);
                    task = new CronometroTask();
                }else{
                    String[] dados = btnCronometro.getText().toString().split(":");
                    minuto = Integer.parseInt(dados[0]);
                    segundo = Integer.parseInt(dados[1]);
                    mili = Integer.parseInt(dados[2]);

                    btnStartStop.setText("PARAR");
                    isRunning = true;
                    task.execute();
                }
            }
        });

        btnZerar = findViewById(R.id.btnZerar);
        btnZerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minuto = 0;
                segundo = 0;
                mili = 0;
                btnCronometro.setText("00:00:00");
            }
        });

    }

    private class CronometroTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (true){
                if(isCancelled())
                    break;

                mili += 1;

                if(mili > 99) {
                    mili = 0;
                    segundo += 1;
                }

                if(segundo > 59){
                    segundo = 0;
                    minuto += 1;
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate(voids);
            String m;
            String s;
            String ms;
            if(minuto < 10)
                m = "0"+minuto;
            else
                m = String.valueOf(minuto);
            if(segundo < 10)
                s = "0"+segundo;
            else
                s = String.valueOf(segundo);
            if(mili < 10)
                ms = "0"+mili;
            else
                ms = String.valueOf(mili);
            btnCronometro.setText(m + ":" + s + ":" + ms);
            Log.i("Cronometro", s + ":" + ms);
        }
    }
}
