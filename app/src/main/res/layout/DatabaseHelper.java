package com.example.degusta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "usuarios.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, email TEXT, password TEXT, isAdmin INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Método para agregar un nuevo usuario
    public long addUser(String name, String email, String password, String fechaNacimiento, String sexo, String isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("isAdmin", isAdmin.equals("admin") ? 1 : 0);  // 1 para admin, 0 para usuario normal

        // Insertar el nuevo usuario en la base de datos
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    // Verificar si el usuario es un administrador
    public boolean esAdmin(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT isAdmin FROM " + TABLE_USERS + " WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        boolean esAdmin = false;
        if (cursor != null && cursor.moveToFirst()) {
            int isAdminIndex = cursor.getColumnIndex("isAdmin");
            esAdmin = cursor.getInt(isAdminIndex) == 1;  // 1 indica que es un administrador
            cursor.close();
        }
        db.close();
        return esAdmin;
    }

    // Verificar si el correo y la contraseña son correctos
    public boolean verificarLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email.trim(), password.trim()});  // Trim para eliminar espacios innecesarios

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
