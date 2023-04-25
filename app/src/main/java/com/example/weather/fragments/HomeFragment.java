package com.example.weather.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.weather.utils.Constants;
import com.example.weather.databinding.FragmentHomeBinding;
import com.example.weather.model.WeatherModel;
import com.example.weather.viewmodel.WeatherViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private static final String TAG = "Home";
    private FragmentHomeBinding binding;
    private WeatherViewModel viewModel;
    protected String latitude, longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;

    private static final int REQUEST_CODE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    private void observeData() {
      binding.progressBar.setVisibility(View.VISIBLE);

        viewModel.getWeather().observe(getViewLifecycleOwner(), weatherModel -> {
            Log.e(TAG, "WeatherData : " + weatherModel.getName());
            binding.progressBar.setVisibility(View.GONE);
            setData(weatherModel);
        });
    }

    private void observeDataFromCityName() {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.getWeather().observe(getViewLifecycleOwner(), weatherModel -> {
            Log.e(TAG, "WeatherData : " + weatherModel.getName());
            binding.progressBar.setVisibility(View.GONE);
            setData(weatherModel);
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(WeatherModel weatherModel) {

        binding.tvDate.setText(Constants.currentDate());
        binding.tvCityName.setText(weatherModel.getName());
        double temp = weatherModel.getMain().getTemp() - 273.15;
        binding.tvTemp.setText(Math.round(temp) + "\u2103");
        double feelsLikeTemp = weatherModel.getMain().getFeelsLike() - 273.15;
        binding.tvFeelsLike.setText(Math.round(feelsLikeTemp) + "\u2103. " + weatherModel.getWeather().get(0).getMain() + ". " + weatherModel.getWeather().get(0).getDescription());

        Glide.with(getContext()).load(Constants.getIcon(weatherModel.getWeather().get(0).getIcon())).into(binding.ivIcon);
        binding.tvPressure.setText(weatherModel.getMain().getPressure().toString() + " hPa");
        binding.tvWind.setText(weatherModel.getWind().getSpeed().toString() + " m/s N");
        binding.tvSunrise.setText(Constants.getTime(weatherModel.getSys().getSunrise()));
        binding.tvHumidity.setText(weatherModel.getMain().getHumidity().toString() + "%");
        binding.tvVisibility.setText(weatherModel.getVisibility() / 1000 + " km");
        binding.tvSunset.setText(Constants.getTime(weatherModel.getSys().getSunset()));

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        if (getArguments() != null) {
            observeDataFromCityName();
            String query = getArguments().getString("query");
            viewModel.getWeatherFromCityName(query, Constants.APP_ID);
        } else {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fetchLocation();
        }

    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    latitude = "" + currentLocation.getLatitude();
                    longitude = "" + currentLocation.getLongitude();
                    observeData();
                    viewModel.getWeatherFromLatLong(latitude, longitude, Constants.APP_ID);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }
}