package com.sitimapps.ateple.ate.ateateple;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.sitimapps.ateple.ate.ateateple.LoginActivity.user_db.user;

public class OutMoneyActivity extends AppCompatActivity {

    private TextView out_money_count, btn_money_out;
    private EditText yandex, count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_money);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Ui();
    }

    private void send_out(){
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://ateple.com/out_money_user.php").newBuilder();
        urlBuilder.addQueryParameter("email", user.getEmail());
        urlBuilder.addQueryParameter("token", user.getToken());
        String temp_check_count = count.getText().toString();
        String temp_check_yandex = yandex.getText().toString();
        String numberOnlycount= temp_check_count.replaceAll("[^0-9]", "");
        String numberOnlyyandex= temp_check_yandex.replaceAll("[^0-9]", "");
        urlBuilder.addQueryParameter("count", numberOnlycount);
        urlBuilder.addQueryParameter("yandex", numberOnlyyandex);
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
                }
            }
        });
    }

    private void Ui() {
        out_money_count = (TextView) findViewById(R.id.out_money_count);
        btn_money_out = (TextView) findViewById(R.id.btn_money_out);
        yandex = (EditText) findViewById(R.id.yandex);
        count = (EditText) findViewById(R.id.count);

        out_money_count.setText(String.valueOf(user.getMoney()) + " руб.");
        btn_money_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp_check_count = count.getText().toString().replaceAll("[^0-9]", "");
                String temp_check_yandex = yandex.getText().toString().replaceAll("[^0-9]", "");
                if (temp_check_count.length() > 1 && temp_check_yandex.length() > 8 && temp_check_count.length() < 6 && temp_check_yandex.length() < 20 ) {
                    if (user.getWait_out() == 0) {
                        if (Integer.parseInt(temp_check_count) <= user.getMoney()) {
                            if (user.outMoney() && Integer.parseInt(temp_check_count)>=500) {
                                send_out();
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Запрос отправлен! Автуризуйтесь - чтобы обновить информацию.",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                                Intent intent = new Intent(OutMoneyActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Вывод от 500 руб!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Недостаточно средств!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Ваша прошлая заявка на выплату еще не выполнена, ожидайте!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }
}
