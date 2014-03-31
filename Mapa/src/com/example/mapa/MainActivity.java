package com.example.mapa;
//??
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends android.support.v4.app.FragmentActivity {

	GoogleMap mapa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
					
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();	//Inicializamos el objeto mapara para poder manipular
		
		mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);													//Seleccionamos el tipo de mapa de acuerdo a lo requeridoMAP_TYPE_NORMAL / MAP_TYPE_HYBRID / MAP_TYPE_SATELLITE / MAP_TYPE_TERRAIN
		mostrarMarcador(-33.4547699,-70.64989889999998);
		
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
	
	private void mostrarMarcador(double lat, double lng)
	{
	    mapa.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Usted esta aqui"));	//title(para el titulo) snippet(para el resto del texto) 
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
}

