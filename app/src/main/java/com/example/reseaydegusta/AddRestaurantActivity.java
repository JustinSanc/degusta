package com.example.reseaydegusta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddRestaurantActivity extends AppCompatActivity {

    private ImageView restaurantImageView;
    private EditText nameEditText, locationEditText, phoneEditText, socialMediaEditText;
    private Button saveButton;
    private Uri selectedImageUri;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Views
        restaurantImageView = findViewById(R.id.restaurantImageView);
        nameEditText = findViewById(R.id.nameEditText);
        locationEditText = findViewById(R.id.locationEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        socialMediaEditText = findViewById(R.id.socialMediaEditText);
        saveButton = findViewById(R.id.saveButton);

        // Add image click listener
        restaurantImageView.setOnClickListener(v -> selectImage());

        // Save button listener
        saveButton.setOnClickListener(v -> saveRestaurant());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            restaurantImageView.setImageURI(selectedImageUri);
        }
    }

    private void saveRestaurant() {
        String name = nameEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String socialMedia = socialMediaEditText.getText().toString();

        if (name.isEmpty() || location.isEmpty() || phone.isEmpty() || socialMedia.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload image to Firebase Storage
        StorageReference storageRef = storage.getReference().child("restaurant_images/" + System.currentTimeMillis() + ".jpg");
        storageRef.putFile(selectedImageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    saveRestaurantData(name, location, phone, socialMedia, uri.toString());
                });
            } else {
                Toast.makeText(this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveRestaurantData(String name, String location, String phone, String socialMedia, String imageUrl) {
        Map<String, Object> restaurant = new HashMap<>();
        restaurant.put("name", name);
        restaurant.put("location", location);
        restaurant.put("phone", phone);
        restaurant.put("socialMedia", socialMedia);
        restaurant.put("image", imageUrl);

        db.collection("restaurants").add(restaurant).addOnSuccessListener(documentReference -> {
            Toast.makeText(this, "Guardado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
