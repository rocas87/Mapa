package com.example.mapa;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends android.support.v4.app.FragmentActivity implements android.location.LocationListener {

	GoogleMap mapa;
	LocationClient mLocationClient;
	LocationManager handle;
	private String provider;
	LatLng ubicacion;
	ProgressDialog pd;
	Location loc;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();	//Inicializamos el objeto mapara para poder manipular
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);													//Seleccionamos el tipo de mapa de acuerdo a lo requeridoMAP_TYPE_NORMAL / MAP_TYPE_HYBRID / MAP_TYPE_SATELLITE / MAP_TYPE_TERRAIN
		
		mapa.setMyLocationEnabled(true);
		
		centrarMapa();	
		mostrarMarcador(ubicacion);
		mostrarLineas();
		
		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker marker) {
				Toast.makeText(
						MainActivity.this, 
						marker.getTitle()+"\n"+"Esta es informacion del Item:\n",Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}
	
	public void centrarMapa(){
			        
	    handle = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    Criteria c = new Criteria();
	    c.setAccuracy(Criteria.ACCURACY_FINE);
	    c.setAltitudeRequired(false);
	    //obtiene el mejor proveedor en función del criterio asignado
	    //(PRESISION MEDIA)
	    provider = handle.getBestProvider(c, true);
	    //Se activan las notificaciones de localización con los parámetros: 
	    //proveedor, tiempo mínimo de actualización, distancia mínima, Locationlistener
	    handle.requestLocationUpdates(provider, 1000, 1, this);
	    loc = handle.getLastKnownLocation(provider);
	    
	    if(loc != null){
		    //Obtenemos la última posición conocida dada por el proveedor
		    ubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());
			configGPS(ubicacion);
		}else
		{	
			ubicacion = new LatLng(-33.442909,-70.65386999999998);
			Toast t1 = Toast.makeText(this, "No se pudo Obtener la ubicacion", Toast.LENGTH_LONG);
			t1.show();
			configGPS(ubicacion);
		 }		
	}
	private void configGPS(LatLng latlong) {
		
		// TODO Auto-generated method stub
		//ubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());
		CameraPosition cameraPosition = new CameraPosition.Builder().target(latlong).zoom(12).build();	
		mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	private void mostrarMarcador(LatLng latlong)
	{
		//ubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());	
	    mapa.addMarker(new MarkerOptions().position(latlong).title("Usted esta aqui"));	//title(para el titulo) snippet(para el resto del texto)
	}
	
	private void mostrarLineas()
	{
	    //Dibujo con Lineas
		PolylineOptions lineas = new PolylineOptions()
	            .add(new LatLng(45.0, -12.0))
	            .add(new LatLng(45.0, 5.0))
	            .add(new LatLng(34.5, 5.0))
	            .add(new LatLng(34.5, -12.0))
	            .add(new LatLng(45.0, -12.0));
	 
	    lineas.width(8);
	    lineas.color(Color.GREEN);
	 
	    mapa.addPolyline(lineas);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		ubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());
		configGPS(ubicacion);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}