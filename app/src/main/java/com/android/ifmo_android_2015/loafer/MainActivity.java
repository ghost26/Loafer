package com.android.ifmo_android_2015.loafer;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity{
    public static int MAIN_REQUEST = 0;
    public static int LAST_ANSWER_FROM_SERVICE;
    private static boolean UPDATE_IN_PROGRESS;
    private static boolean IMAGE_LOADER_IS_EXIST;
    public String currentCity;
    public String currentCityId;
    private AlertDialog dialogForUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (UPDATE_IN_PROGRESS) {
            createDialogForUpdate();
            updateDialogForUpdate(LAST_ANSWER_FROM_SERVICE);
        }

        if (!IMAGE_LOADER_IS_EXIST) {
            createImageLoader();
        }
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, DataBaseInitService.class);
        switch (v.getId()) {
            case R.id.stavropol :
                currentCity = "Ставрополь";
                currentCityId = "0";
                break;
            case R.id.moscow :
                currentCity = "Москва";
                currentCityId = "1";
                break;
            case R.id.stPetersburg :
                currentCity = "Санкт-Петербург";
                currentCityId = "2";
                break;
            case R.id.saratov:
                currentCity = "Саратов";
                currentCityId = "3";
                break;
            case R.id.rostovNaDony:
                currentCity = "Ростов-на-Дону";
                currentCityId = "4";
                break;
            case R.id.krasnodar:
                currentCity = "Краснодар";
                currentCityId = "5";
                break;
        }

        createDialogForUpdate();
        PendingIntent pi = createPendingResult(MAIN_REQUEST, new Intent(), 0);
        intent.putExtra(DataBaseInitService.PENDING_INTENT, pi);
        intent.putExtra(DataBaseInitService.FROM, DataBaseInitService.FROM_MAIN);
        intent.putExtra(DataBaseInitService.CITY, currentCity);
        intent.putExtra(DataBaseInitService.CITY_ID, currentCityId);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAIN_REQUEST) {
            if (resultCode == DataBaseInitService.DONE) {
                Toast.makeText(getApplicationContext(), "Готово", Toast.LENGTH_SHORT).show();
                hideDialogForUpdate();
                Intent intent = new Intent(this, TabsActivity.class);
                intent.putExtra(DataBaseInitService.CITY, currentCity);
                intent.putExtra(DataBaseInitService.CITY_ID, currentCityId);
                Calendar calendar = Calendar.getInstance();
                String curData = calendar.getTime().toString();
                intent.putExtra(DataBaseInitService.CURRENT_DATA,curData);
                startActivity(intent);
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

    private void createImageLoader() {
        IMAGE_LOADER_IS_EXIST = true;
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        config.threadPoolSize(3);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.FIFO);
        config.memoryCache(new LruMemoryCache(10 * 1024 * 1024)); // 10 MiB
        ImageLoader.getInstance().init(config.build());
    }

    private void hideDialogForUpdate() {
        UPDATE_IN_PROGRESS = false;
        dialogForUpdate.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MAIN", "onDestroy");
        Intent stopIntent = new Intent(this, DataBaseInitService.class);
        stopService(stopIntent);
    }
}
