package com.example.degusta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ResenaYDegustaDB";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS Users (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Name TEXT, " +
                    "LastName TEXT, " +
                    "BirthDate TEXT, " +
                    "Gender TEXT, " +
                    "Email TEXT UNIQUE, " +
                    "Password TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    // Método para agregar un usuario a la base de datos
    public long addUser(String name, String lastName, String birthDate, String gender, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("LastName", lastName);
        values.put("BirthDate", birthDate);
        values.put("Gender", gender);
        values.put("Email", email);
        values.put("Password", password); // Aquí puedes agregar el hash de la contraseña

        // Insertar el usuario y devolver el ID de la fila insertada
        return db.insert("Users", null, values);
    }

    // Método para verificar si un email ya está registrado
    public boolean isEmailTaken(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Users WHERE Email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        boolean emailTaken = cursor.moveToFirst();
        cursor.close();
        return emailTaken;
    }

    // Método para verificar las credenciales del usuario (login)
    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Users WHERE Email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        boolean userExists = false;
        if (cursor.moveToFirst()) {
            int passwordColumnIndex = cursor.getColumnIndex("Password");
            if (passwordColumnIndex != -1) {
                String storedHashedPassword = cursor.getString(passwordColumnIndex);
                userExists = BCrypt.checkpw(password, storedHashedPassword); // Comparar contraseñas encriptadas
            }
        }

        cursor.close();
        return userExists;
    }
}
