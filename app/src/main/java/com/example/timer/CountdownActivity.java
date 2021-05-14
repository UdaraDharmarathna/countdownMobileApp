package com.example.timer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import cn.iwgang.countdownview.CountdownView;

public class CountdownActivity extends AppCompatActivity {

    AppConfig config;

    CountdownView countdownView;
    TextView topic, date_tring;
    private ProgressDialog progressDialog;
    AlertDialog.Builder alertDialog;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        topic = findViewById(R.id.title);
        date_tring = findViewById(R.id.dateString);

        countdownView = findViewById(R.id.countdown_view);
        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                alertDialog = new AlertDialog.Builder(CountdownActivity.this);
                alertDialog.setTitle("Countdown");
                alertDialog.setMessage("Time Over");
                alertDialog.show();
            }
        });

        config = new AppConfig();
        String serverURL = config.getServerurl();

        new requestCountdownData().execute(serverURL);
    }

    private class requestCountdownData extends AsyncTask<String,Integer,JSONObject> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CountdownActivity.this);
            progressDialog.setTitle("Login");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {

                String url = params[0]+"api/countdownController.php?view=1";

                HttpHandler httpHandler = new HttpHandler();
                JSONObject countdownData = httpHandler.getHandler(url);

                return countdownData;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {

                JSONObject response = jsonObject.getJSONObject("response");

                int statusCode = Integer.parseInt(response.getString("statusCode"));
                String message = response.getString("statusMessage");
                boolean success = Boolean.parseBoolean(response.getString("status"));

                if (statusCode == 200 && success == true) {

                    JSONObject countdownData = jsonObject.getJSONObject("data");

                    String title = countdownData.getString("title");
                    String target_date = countdownData.getString("target_date");
                    String target_time = countdownData.getString("target_time");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String target_date_time = target_date + " " + target_time;
                    Date now = new Date();

                    Date formatTargetDate = sdf.parse(target_date_time);
                    long currentTime = now.getTime();
                    long formatedDate = formatTargetDate.getTime();
                    long countMillis = formatedDate - currentTime;

                    String pattern = "H:mm a, dd MMM yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String readableFormat = simpleDateFormat.format(formatTargetDate);

                    topic.setText("Countdown for "+title);
                    date_tring.setText(readableFormat.toString());
                    countdownView.start(countMillis);

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                }else{

                }
            }catch (Exception e){

            }
        }
    }
}
