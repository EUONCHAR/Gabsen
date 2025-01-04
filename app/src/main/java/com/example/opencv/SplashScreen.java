package com.example.opencv;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    // Durasi tampilan splash screen (dalam milidetik)
    private static final int SPLASH_SCREEN_DELAY = 3000; // 3 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Handler untuk menunda pindah ke aktivitas berikutnya
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Menjalankan aktivitas utama (MainActivity)
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish(); // Mengakhiri aktivitas splash screen agar tidak bisa kembali
            }
        }, SPLASH_SCREEN_DELAY);
    }
}
