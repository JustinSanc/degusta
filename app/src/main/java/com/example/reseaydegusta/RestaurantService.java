package com.example.reseaydegusta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestaurantService {

    @GET("/restaurants")
    Call<List<Restaurant>> getRestaurants();

    @POST("/restaurants")
    Call<Void> createRestaurant(@Body Restaurant restaurant); // Se añadió @Body para enviar datos al servidor.
}
