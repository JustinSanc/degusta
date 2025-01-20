package com.example.degusta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList; 
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private Button addRestaurantButton, manageUsersButton, viewReviewsButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addRestaurantButton = findViewById(R.id.btnAgregarRestaurante);
        manageUsersButton = findViewById(R.id.btnAdministradorUsuarios);
        viewReviewsButton = findViewById(R.id.btnVerOpiniones);
        logoutButton = findViewById(R.id.btnCerrarSesion);

        // Acción para agregar un restaurante
        addRestaurantButton.setOnClickListener(view -> {
            // Acción de agregar restaurante
        });

        // Acción para gestionar usuarios
        manageUsersButton.setOnClickListener(view -> {
            // Acción de administrar usuarios
        });

        // Acción para ver opiniones
        viewReviewsButton.setOnClickListener(view -> {
            // Acción de ver opiniones
        });

        // Acción para cerrar sesión
        logoutButton.setOnClickListener(view -> {
            // Acción de cerrar sesión
            finish();
        });
    }
}
