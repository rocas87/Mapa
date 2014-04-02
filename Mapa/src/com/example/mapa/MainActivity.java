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
	Location loc,Moneda, Estadio;
	Marker moneda,estNacCoord;
	double distance;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Declaramos el objeto mapa
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();	//Inicializamos el objeto mapara para poder manipular
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);													//Seleccionamos el tipo de mapa de acuerdo a lo requeridoMAP_TYPE_NORMAL / MAP_TYPE_HYBRID / MAP_TYPE_SATELLITE / MAP_TYPE_TERRAIN
		//Boton para sentra ubicaion en el mapa
		mapa.setMyLocationEnabled(true);
		
		centrarMapa();	
		//Genero un nuevo marcador
		//title(para el titulo) snippet(para el resto del texto)
		LatLng monedaCord = new LatLng(-33.442909,-70.65386999999998);
		moneda = mapa.addMarker(new MarkerOptions().position(monedaCord).title("Usted quiere ir a la moneda"));
		
		LatLng EstNacCoord = new LatLng(-33.46449847739583,-70.6105856847168);
		estNacCoord = mapa.addMarker(new MarkerOptions().position(EstNacCoord).title("Usted quiere al Estadio"));
		
		//Setear una variable Location
		Estadio =new Location("Estadio");
		Estadio.setLatitude(-33.46449847739583 );
		Estadio.setLongitude(-70.65386999999998);
		
		Moneda =new Location("Moneda");
		Moneda.setLatitude(-33.442909 );
		Moneda.setLongitude(-70.65386999999998);
		
		//Distancia entre el punto A y B
		distance = Moneda.distanceTo(Estadio);
		distance = distance/1000;
		mostrarLineas();
		
		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker marker) {
				Toast.makeText(
						MainActivity.this, 
						marker.getTitle()+"\n"+"Esta es informacion del Item:\n"+distance+"KM",Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}
	
	public void centrarMapa(){
		//Llamo al servico de localizacion	        
	    handle = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    //Clase criteria permite decidir mejor poveedor de posicion
	    Criteria c = new Criteria();
	    //obtiene el mejor proveedor en función del criterio asignado
	    //ACCURACY_FINE(La mejor presicion)--ACCURACY_COARSE(PRESISION MEDIA)
	    c.setAccuracy(Criteria.ACCURACY_FINE);
	    //Indica si es necesaria la altura por parte del proveedor
	    c.setAltitudeRequired(false);
	    provider = handle.getBestProvider(c, false);
	    //Se activan las notificaciones de localización con los parámetros: 
	    //proveedor, tiempo mínimo de actualización, distancia mínima, Locationlistener
	    handle.requestLocationUpdates(provider, 60000, 5, this);
	    //Obtiene la ultima posicion conocida por el proveedor
	    loc = handle.getLastKnownLocation(provider);
	    
	    if(loc != null){
		    //Obtenemos la última posición conocida dada por el proveedor
		    ubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());
			configGPS(ubicacion);
		}else
		{	
			ubicacion = new LatLng(-33.442909,-70.65386999999998);
			//Toast t1 = Toast.makeText(this, "No se pudo Obtener la ubicacion", Toast.LENGTH_LONG);
			//t1.show();
			configGPS(ubicacion);
		 }		
	}
	
	private void configGPS(LatLng latlong) {
		
		// TODO Auto-generated method stub
		//Se indica el latitud y longitud y zoom
		CameraPosition cameraPosition = new CameraPosition.Builder().target(latlong).zoom(12).build();	
		//Se mueve la camara a lo indicado anteriormente
		mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	private void mostrarLineas()
	{
	    //Dibujo con Lineas cuadro en el mapa
		PolylineOptions lineas = new PolylineOptions()
	            .add(new LatLng(45.0, -12.0))
	            .add(new LatLng(45.0, 5.0))
	            .add(new LatLng(34.5, 5.0))
	            .add(new LatLng(34.5, -12.0))
	            .add(new LatLng(45.0, -12.0));
		//Ancho de la linea
	    lineas.width(8);
	    //Color de la linea
	    lineas.color(Color.GREEN);
	    //Agrego lo descrito al objeto mapa
	    mapa.addPolyline(lineas);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	//Metodos de Location
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		//Transforma las variales lat y long en 1 sola variable del tipo LatLng
		ubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());
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
	//Fin metodos location
}