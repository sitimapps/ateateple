package com.sitimapps.ateple.ate.ateateple;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.sitimapps.ateple.ate.ateateple.LoginActivity.user_db.user;

public class MenuActivity extends AppCompatActivity {

    private TextView account_name, account_email;
    private TextView statistics_view_today, statistics_click_today;
    private TextView statistics_view, statistics_click;
    private TextView balans_hold, balans_money;
    private TextView job_url;
    private TextView ref_code;
    private TextView job_scr, job_feed, ref_scr, ref_feed;
    private TextView go_to_out_money;
    private TextView inform_ate, inform_help, inform_feed, inform_rules;
    private TextView balans_wait;

    private ImageView account_photo;

    String json_text="";

    private boolean end_load = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        makePost();
        while (!end_load) {}
        Ui();
    }


    private void makePost() {

        OkHttpClient client = new OkHttpClient();


        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://ateple.com/login_user.php").newBuilder();
        urlBuilder.addQueryParameter("email", user.getEmail());
        urlBuilder.addQueryParameter("token", user.getToken());
        if (user.getCode_ref_id()>0) {
            urlBuilder.addQueryParameter("code", String.valueOf(user.getCode_ref_id()));
        }
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (Exception e) {}

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {

                    json_text = response.body().string();
                    Parse(json_text);
                }
            }
        });


    }

    private void Parse(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(jsonString);
        JsonObject rootObject = jsonElement.getAsJsonObject();
        user.setId(rootObject.get("id_user").getAsString());
        user.setMoney(Integer.parseInt(rootObject.get("money").getAsString()));
        user.setHold(Integer.parseInt(rootObject.get("hold_money").getAsString()));
        user.setWait_out(Integer.parseInt(rootObject.get("wait_out").getAsString()));
        user.setView_today(Integer.parseInt(rootObject.get("count_view_day").getAsString()));
        user.setClick_today(Integer.parseInt(rootObject.get("count_click_day").getAsString()));
        user.setView_a(Integer.parseInt(rootObject.get("count_view").getAsString()));
        user.setClick_a(Integer.parseInt(rootObject.get("count_click").getAsString()));
        end_load = true;
    }

    private void Ui() {
        account_name = (TextView) findViewById(R.id.account_name);
        account_email = (TextView) findViewById(R.id.account_email);
        account_photo = (ImageView) findViewById(R.id.account_photo);
        statistics_view_today = (TextView) findViewById(R.id.statistics_view_today);
        statistics_click_today = (TextView) findViewById(R.id.statistics_click_today);
        statistics_view = (TextView) findViewById(R.id.statistics_view);
        statistics_click = (TextView) findViewById(R.id.statistics_click);
        balans_hold = (TextView) findViewById(R.id.balans_hold);
        balans_money = (TextView) findViewById(R.id.balans_money);
        job_url = (TextView) findViewById(R.id.job_url);
        ref_code = (TextView) findViewById(R.id.ref_code);
        job_scr = (TextView) findViewById(R.id.job_scr);
        job_feed = (TextView) findViewById(R.id.job_feed);
        ref_scr = (TextView) findViewById(R.id.ref_scr);
        ref_feed = (TextView) findViewById(R.id.ref_feed);
        go_to_out_money = (TextView) findViewById(R.id.go_to_out_money) ;
        inform_ate = (TextView) findViewById(R.id.inform_ate);
        inform_help = (TextView) findViewById(R.id.inform_help);
        inform_feed = (TextView) findViewById(R.id.inform_feed);
        inform_rules = (TextView) findViewById(R.id.inform_rules);
        balans_wait = (TextView) findViewById(R.id.balans_wait);

        account_name.setText(user.getName());
        account_email.setText(user.getEmail());
        if (user.getPhoto()!=null) {
            Glide.with(this).load(user.getPhoto()).into(account_photo);
        }
        statistics_view_today.setText(String.valueOf(user.getView_today()));
        statistics_click_today.setText(String.valueOf(user.getClick_today()));
        statistics_view.setText(String.valueOf(user.getView_a()));
        statistics_click.setText(String.valueOf(user.getClick_a()));
        balans_hold.setText(String.valueOf(user.getHold()) + " руб.");
        balans_money.setText(String.valueOf(user.getMoney()) + " руб.");
        job_url.setText("http://ateple.com/?u=" + String.valueOf(user.getId()));
        ref_code.setText("RG" + String.valueOf(user.getId()));
        balans_wait.setText(String.valueOf(user.getWait_out()) + " руб.");


        final ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        job_scr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData clip = ClipData.newPlainText("Скопировано!", "http://ateple.com/?u=" + String.valueOf(user.getId()));
                clipboard.setPrimaryClip(clip);
            }
        });
        job_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("plain/text");
                intent.putExtra("android.intent.extra.TEXT", "Привет! Знаешь про новый каталог проверенных магазинов? \n http://ateple.com/?u=" + user.getId());
                startActivity(Intent.createChooser(intent, "Поделится ^-^"));
            }
        });
        ref_scr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData clip = ClipData.newPlainText("Скопировано!", "RG" + String.valueOf(user.getId()));
                clipboard.setPrimaryClip(clip);
            }
        });
        ref_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("plain/text");
                intent.putExtra("android.intent.extra.TEXT", "Работа в интернете! Скачай мобильное приложение https://play.google.com/store/apps/details?id=com.sitimapps.ateple.ate.ateateple, введи код: RG" + user.getId() + " и получишь 100 рублей сразу на счет!" );
                startActivity(Intent.createChooser(intent, "Поделится ^-^"));
            }
        });
        go_to_out_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, OutMoneyActivity.class);
                startActivity(intent);
            }
        });
        inform_ate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
                intent.putExtra("select", "ate");
                startActivity(intent);
            }
        });
        inform_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
                intent.putExtra("select", "help");
                startActivity(intent);
            }
        });
        inform_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
                intent.putExtra("select", "feed");
                startActivity(intent);
            }
        });
        inform_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
                intent.putExtra("select", "rules");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {}

}
