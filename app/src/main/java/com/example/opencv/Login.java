package com.example.opencv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    // Mendeklarasikan variabel untuk EditText, Button, dan TextView
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView daftarTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi variabel UI
        emailEditText = findViewById(R.id.Email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        daftarTextView = findViewById(R.id.daftar);

        // Menambahkan aksi saat tombol login ditekan
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan input dari EditText
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Mengecek apakah input kosong
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Logika login sederhana menggunakan if-else
                    if (email.equals("a") && password.equals("a")) {
                        // Jika login berhasil, pindah ke MainActivity
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Menghentikan aktivitas login
                    } else {
                        // Jika login gagal
                        Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Menambahkan aksi saat teks "Daftar" diklik
        daftarTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke aktivitas pendaftaran (misalnya RegistrasiActivity)
                Intent intent = new Intent(Login.this, RegistrasiActivity.class);
                startActivity(intent);
            }
        });
    }
}
