package novtsm.com.stopwatch1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;

    private int seconds = 0; // Количество прошедших секунд со старта таймера
    private boolean isRunning = false; // Признак, работает ли секундомер в данный момент
    private boolean wasRunning = false; // Переменная для сохранения состояния таймера до метода onStop()

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        // Восстановление значений после поворота экрана
        if(savedInstanceState != null){
            // Восстановление значения seconds из Bundle
            seconds = savedInstanceState.getInt("seconds");
            // Восстановление значения isRunning из Bundle
            isRunning = savedInstanceState.getBoolean("isRunning");
            // Восстановление значения wasRunning из Bundle
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
    }

//    //-------------------------------------------------------------------------------------------
//    // (Переопределение: Ctrl + O)
//    @Override
//    protected void onStop() { // Таймер останавливается когда активность становится не видимой
//        super.onStop();
//        wasRunning = isRunning;
//        isRunning = false; // Остановка таймера при сокрытии экрана
//    }
//    // (Переопределение: Ctrl + O)
//    @Override
//    protected void onStart() {
//        super.onStart();
//        isRunning = wasRunning;
//    }
//    //-------------------------------------------------------------------------------------------

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = isRunning;
        isRunning = false; // Остановка таймера при сокрытии экрана
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        super.onStart();
        isRunning = wasRunning;
    }

    // Сохранение текущего состояния активности при повороте экрана (Переопределение: Ctrl + O)
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);            // Сохранение в Bundle переменной seconds
        outState.putBoolean("isRunning", isRunning);    // Сохранение в Bundle переменной isRunning
        outState.putBoolean("wasRunning", wasRunning);  // Сохранение в Bundle переменной wasRunning
    }

    public void onClickStartTimer(View view) {
        isRunning = true;
    }

    public void onClickPauseTimer(View view) {
        isRunning = false;
    }

    public void onClickResetTimer(View view) {
        isRunning = false;
        seconds = 0; // Обнуление таймера
    }

    // Метод для обновления показаний секундомера
    private void runTimer(){
        final Handler handler = new Handler(); // Класс для передачи в отдельный поток
        // Передача кода для выполнения в отдельном программном потоке
        // Код, который нужно выполнить запаковываеться в объект Runnable
        handler.post(new Runnable() {   // Здесь объект Runnable - анонимный класс
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                // Преобразование в строковый формат
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                textViewTimer.setText(time); // Передача времени (строки) в textView
                if (isRunning){
                    seconds++;
                    // handler.postDelayed(this, 1000); // TODO: Уточнить!
                }
                // Планирование выполнения кода каждую секунду
                handler.postDelayed(this, 1000);
            }
        });
        // Только главный поток может влиять на элементы пользовательского интерфейса
    }
    // Метод для отслеживания состояния секундомера
}