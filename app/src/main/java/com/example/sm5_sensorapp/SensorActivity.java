package com.example.sm5_sensorapp;

import static com.example.sm5_sensorapp.SensorDetailsActivity.EXTRA_SENSOR_TYPE_PARAMETER;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;
    private SensorAdapter adapter;
    private static final String SENSOR_APP_TAG = "SENSOR_APP_TAG";
    public static final int SENSOR_DETAILS_ACTIVITY_REQUEST_CODE = 1;
    public static final int LOCATION_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);


        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //wpisywanie do logów nazw czujników
            sensorList.forEach(sensor -> {
                Log.d(SENSOR_APP_TAG, "Sensor name:" + sensor.getName());
                Log.d(SENSOR_APP_TAG, "Sensor vendor:" + sensor.getVendor());
                Log.d(SENSOR_APP_TAG, "Sensor max range:" + sensor.getMaximumRange());
            });
        }

        if(adapter == null){
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_sensor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String string = getString(R.string.sensors_count, sensorList.size());
        getSupportActionBar().setSubtitle(string);
        return true;
    }



    private class SensorHolder extends RecyclerView.ViewHolder {
        TextView sensorNameTextView ;
        Sensor sensor;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));

            sensorNameTextView  = itemView.findViewById(R.id.sensor_name);

        }

        public void bind(Sensor sensor) {
            this.sensor = sensor;
            sensorNameTextView .setText(sensor.getName());
            View itemContainer = itemView.findViewById(R.id.list_item_sensor);
            if (sensor.getType() == Sensor.TYPE_LIGHT || sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                itemContainer.setBackgroundColor(getResources().getColor(R.color.lightpink));
                itemContainer.setOnClickListener(v -> {
                    Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                    intent.putExtra(EXTRA_SENSOR_TYPE_PARAMETER, sensor.getType());
                    startActivityForResult(intent, SENSOR_DETAILS_ACTIVITY_REQUEST_CODE);
                });
            }
            if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                itemContainer.setBackgroundColor(getResources().getColor(R.color.lightpink));
                itemContainer.setOnClickListener(v -> {
                    Intent intent = new Intent(SensorActivity.this, LocationActivity.class);
                    startActivityForResult(intent,LOCATION_ACTIVITY_REQUEST_CODE);
                });
            }
        }

    }



    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder>{
        private final List<Sensor> sensors;

        public SensorAdapter(List<Sensor> sensors){
            this.sensors = sensors;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }

}

