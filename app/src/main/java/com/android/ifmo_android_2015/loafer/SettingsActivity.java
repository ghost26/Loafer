package com.android.ifmo_android_2015.loafer;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private String city;
    private String city_id;
    private AlertDialog dialogForUpdate;

    public static int SETTINGS_REQUEST = 0;
    public static boolean UPDATE_IN_PROGRESS;

    public static int LAST_ANSWER_FROM_SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (UPDATE_IN_PROGRESS) {
            createDialogForUpdate();
            updateDialogForUpdate(LAST_ANSWER_FROM_SERVICE);;
        }
        Intent intent = getIntent();
        city = intent.getStringExtra("CITY");
        city_id = intent.getStringExtra("CITY_ID");
        TextView textView = (TextView) findViewById(R.id.update_time);
        textView.setText(intent.getStringExtra(DataBaseInitService.CURRENT_DATA));
    }

    public void onClick(View view) {
        createDialogForUpdate();
        Intent intent = new Intent(this, DataBaseInitService.class);
        intent.putExtra(DataBaseInitService.FROM, DataBaseInitService.FROM_SETTINGS);
        intent.putExtra(DataBaseInitService.CITY, city);
        intent.putExtra(DataBaseInitService.CITY_ID, city_id);
        PendingIntent pi = createPendingResult(SETTINGS_REQUEST, new Intent(), 0);
        intent.putExtra(DataBaseInitService.PENDING_INTENT, pi);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST) {
            if (resultCode == DataBaseInitService.DONE) {
                TextView textView = (TextView) findViewById(R.id.update_time);
                textView.setText(data.getStringExtra(DataBaseInitService.CURRENT_DATA));
                Toast.makeText(getApplicationContext(), "Готово", Toast.LENGTH_SHORT).show();
                hideDialogForUpdate();
            } else if (resultCode == DataBaseInitService.ERROR) {
                Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                hideDialogForUpdate();
            } else {
                LAST_ANSWER_FROM_SERVICE = resultCode;
                updateDialogForUpdate(resultCode);
            }
        }

    }

    private void createDialogForUpdate() {
        UPDATE_IN_PROGRESS = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_for_update, null));

        builder.setCancelable(false);

        dialogForUpdate = builder.create();
        dialogForUpdate.show();
    }

    private void updateDialogForUpdate(int resultCode) {
        TextView status = (TextView) dialogForUpdate.findViewById(R.id.status);
        if (resultCode == DataBaseInitService.TRY_TO_TAKE_TIMEPAD_DATA) {
            status.setText("Получаем данные с TimePad");
        }
        if (resultCode == DataBaseInitService.UPDATE_DB) {
            status.setText("Обновляем базу");
        }
        if (resultCode == DataBaseInitService.GET_FROM_DB) {
            status.setText("Обновляем данные для списка и карты");
        }
    }

    private void hideDialogForUpdate() {
        UPDATE_IN_PROGRESS = false;
        dialogForUpdate.cancel();
    }
}
