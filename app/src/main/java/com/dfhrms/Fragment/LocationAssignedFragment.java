package com.dfhrms.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dfhrms.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class LocationAssignedFragment extends Fragment {
    private static final String SOAP_NAMESPACE = "http://tempuri.org/";
    private static final String SOAP_METHOD_NAME = "GetEmployeeLocations";
    private static final String SOAP_ACTION = "http://tempuri.org/GetEmployeeLocations";
    private static final String SOAP_URL = "https://dfhrms.dfindia.org/PMSservice.asmx?WSDL";
    private String emp_id;

    private List<LocationData> locationDataList;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;

    public LocationAssignedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get emp_id from SharedPreferences or any other method
        SharedPreferences myprefs = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        emp_id = myprefs.getString("emp_id", "nothing");
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_assigned, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_loc);
        locationDataList = new ArrayList<>();

        adapter = new LocationAdapter(locationDataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        getDataFromSoapAPI();

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private void getDataFromSoapAPI() {
        new AsyncTask<Void, Void, List<LocationData>>() {
            @Override
            protected List<LocationData> doInBackground(Void... voids) {
                try {
                    SoapObject request = new SoapObject(SOAP_NAMESPACE, SOAP_METHOD_NAME);
                    request.addProperty("EmployeeId", emp_id);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE transport = new HttpTransportSE(SOAP_URL);
                    transport.call(SOAP_ACTION, envelope);
                    SoapObject response = (SoapObject) envelope.getResponse();
                    Log.e("Response", response.toString());
                    SoapObject userLatLong = (SoapObject) response.getProperty("UserLatLong");
                    List<LocationData> locations = new ArrayList<>();
                    for (int i = 0; i < userLatLong.getPropertyCount(); i++) {
                        SoapObject location = (SoapObject) userLatLong.getProperty(i);
                        String latitude = location.getProperty("Latitude").toString();
                        String longitude = location.getProperty("Longitude").toString();
                        String radius = location.getProperty("Radius").toString();
                        String locationName = location.getProperty("Location").toString();
                        LocationData locationData = new LocationData(latitude, longitude, radius, locationName);
                        locations.add(locationData);
                    }
                    return locations;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<LocationData> locations) {
                if (locations != null) {
                    locationDataList.addAll(locations);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private static class LocationData {
        private String latitude;
        private String longitude;
        private String radius;
        private String locationName;

        public LocationData(String latitude, String longitude, String radius, String locationName) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.radius = radius;
            this.locationName = locationName;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getRadius() {
            return radius;
        }

        public String getLocationName() {
            return locationName;
        }
    }

    private static class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
        private List<LocationData> locations;

        public LocationAdapter(List<LocationData> locations) {
            this.locations = locations;
        }

        @NonNull
        @Override
        public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_location, parent, false);
            return new LocationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
            LocationData location = locations.get(position);

            String serialNumber = "" + (position + 1);
            holder.serialNumberTextView.setText(serialNumber);

            holder.locationNameTextView.setText(location.getLocationName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLocationDetailsPopup(v.getContext(), location);
                }
            });
        }

        @Override
        public int getItemCount() {
            return locations.size();
        }

        public static class LocationViewHolder extends RecyclerView.ViewHolder {
            TextView serialNumberTextView;
            TextView locationNameTextView;

            public LocationViewHolder(@NonNull View itemView) {
                super(itemView);
                serialNumberTextView = itemView.findViewById(R.id.text_sl);
                locationNameTextView = itemView.findViewById(R.id.text_location_name);
            }
        }

        private static void showLocationDetailsPopup(Context context, LocationData location) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(" " + location.getLocationName());
            builder.setMessage("Latitude: " + location.getLatitude()
                    + "\nLongitude: " + location.getLongitude()
                    + "\nRadius: " + location.getRadius());
            builder.setPositiveButton("OK", null);
            builder.show();
        }
    }
}
