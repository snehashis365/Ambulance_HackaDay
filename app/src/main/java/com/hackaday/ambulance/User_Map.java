package com.hackaday.ambulance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.skyfishjy.library.RippleBackground;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User_Map extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap mMap;
	private  FusedLocationProviderClient mFusedLocationClient;
	ImageButton btn_Logout;
	Button select;
	ImageView user_pin;
	Location mLastLoacation;
	Location pinLocation;
	LocationRequest mLocationRequest;
	LatLng pinPosition,pickupPosition,dropoffPosition;
	View mapView;
	PlacesClient placesClient;
	private List<AutocompletePrediction> predictionList;
	private String destination;
	boolean pickupMarked=false;
	boolean dropoffMarked=false;

	RippleBackground rippleBackground;



	public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
		Drawable drawable =  AppCompatResources.getDrawable(context, drawableId);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			drawable = (DrawableCompat.wrap(drawable)).mutate();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}


	private void logOut(){

		FirebaseAuth.getInstance().signOut();
		Toast.makeText(User_Map.this, "Session Ended", Toast.LENGTH_SHORT).show();
		//Intent logout=new Intent(User_Map.this, MainActivity.class);
		//startActivity(logout);
		finish();
		return;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user__map);

		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		btn_Logout=findViewById(R.id.btn_Logout);
		select = findViewById(R.id.select);
		user_pin=findViewById(R.id.user_pin);
		rippleBackground=findViewById(R.id.ripple);

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.user_map);
		assert mapFragment != null;
		mapFragment.getMapAsync(this);

		//Initializing API key to for Places SDK
		Places.initialize(User_Map.this,"AIzaSyD_51pdZf8pAXCGT7Ti8fhWczuhbSFLFM0");

		placesClient = Places.createClient(this);
		//final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

		mapView = mapFragment.getView();

		btn_Logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				//Logout User
				view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				logOut();

			}
		});

		// Initialize the AutocompleteSupportFragment.
		AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
				getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
		autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

// Set up a PlaceSelectionListener to handle the response.
		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			@Override
			public void onPlaceSelected(Place place) {
				// TODO: Get info about the selected place.
				destination=place.getName().toString();

				//Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
			}

			@Override
			public void onError(Status status) {
				// TODO: Handle the error.
				//Log.i(TAG, "An error occurred: " + status);
			}
		});


	}

	private void moveToUserLocation(){
		mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
			@Override
			public void onSuccess(Location location) {
				if(location!=null){
					mLastLoacation = location;
					pinLocation = location;
					LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
					mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
					mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
				}
				else {
					Toast.makeText(User_Map.this, "Failed to get current location please try again", Toast.LENGTH_LONG).show();
					finish();
				}

			}
		});

	}

	//Moves my location button by the provided margin arguments
	private void moveMyLocationButton(int left,int top,int right, int bottom){

		View locationButton = ((View)mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		layoutParams.setMargins(left,top,right,bottom);

	}


	@Override
	public void onMapReady(final GoogleMap googleMap) {
		mMap = googleMap;
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(3000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		mMap.getUiSettings().setCompassEnabled(false);
		mMap.getUiSettings().setZoomControlsEnabled(false);
		mMap.getUiSettings().setMapToolbarEnabled(false);
		mMap.getUiSettings().setTiltGesturesEnabled(false);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
			if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

				moveToUserLocation();
				mMap.setMyLocationEnabled(true);

				//Send my location button to bottom
				moveMyLocationButton(0,0,18,100);


			}else{
				checkLocationPermission();

				moveToUserLocation();
				mMap.setMyLocationEnabled(true);
				//Send my location button to bottom
				moveMyLocationButton(0,0,18,150);
			}
		}
        //Pin position fetcher
		mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
			@Override
			public void onCameraMove() {
				pinPosition=googleMap.getCameraPosition().target;
			}
		});
		mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
			@Override
			public void onCameraIdle() {
				if (pinPosition!=null){
					pinLocation.setLatitude(pinPosition.latitude);
					pinLocation.setLongitude(pinPosition.longitude);
					//Logs for testing
					Log.d("Moved Pin to:","Latitude:"+pinLocation.getLatitude()+", Longitude:"+pinLocation.getLongitude());
				}

			}
		});

		select.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!pickupMarked){

					mMap.addMarker(new MarkerOptions().position(pinPosition).icon(BitmapDescriptorFactory
							.fromBitmap(getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.user_pin_pickup))).title("Pickup Here")).showInfoWindow();
					select.setText("Set as Drop off Location");
					pickupMarked = true;
					pickupPosition = pinPosition;

				}
				else if (!dropoffMarked){

					mMap.addMarker(new MarkerOptions().position(pinPosition).icon(BitmapDescriptorFactory
							.fromBitmap(getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.user_pin_dropoff))).title("Drop here")).showInfoWindow();
					select.setText("Call Ambulance");
					dropoffMarked = true;
					dropoffPosition = pinPosition;
				}
				else {

						rippleBackground.startRippleAnimation();
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								rippleBackground.stopRippleAnimation();
								Toast.makeText(User_Map.this, "Animation is for testing only for now", Toast.LENGTH_SHORT).show();

							}
						}, 10000);

				}



			}
		});


	}



	private void checkLocationPermission() {

		if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

			if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
				new AlertDialog.Builder(this).setTitle("Give Permission").setMessage("Give Permission Message").setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						ActivityCompat.requestPermissions(User_Map.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
					}
				}).create().show();
			}
			else{
				ActivityCompat.requestPermissions(User_Map.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode){
			case 1:{
				if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
					if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
						moveToUserLocation();
						mMap.setMyLocationEnabled(true);
					}
				}
			}
		}
	}



	@Override
	public void onPointerCaptureChanged(boolean hasCapture) {

	}

	@Override
	protected void onStop() {
		super.onStop();
		logOut();
	}
}

