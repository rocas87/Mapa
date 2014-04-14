package com.example.mapa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends android.support.v4.app.FragmentActivity implements android.location.LocationListener {

	GoogleMap mapa;
	LocationClient mLocationClient;
	LocationManager handle;
	private String provider;
	LatLng Miubicacion, monedaCord, EstNacCoord;
	ProgressDialog pd;
	Location loc,Moneda, Estadio;
	Marker moneda,estNacCoord;
	double distance;
	TextView tv1;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv1 = (TextView)findViewById(R.id.Tv1);
		//Declaramos el objeto mapa
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();	//Inicializamos el objeto mapara para poder manipular
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);													//Seleccionamos el tipo de mapa de acuerdo a lo requeridoMAP_TYPE_NORMAL / MAP_TYPE_HYBRID / MAP_TYPE_SATELLITE / MAP_TYPE_TERRAIN
		//Boton para sentra ubicaion en el mapa
		mapa.setMyLocationEnabled(true);
		
		centrarMapa();	
		//Genero un nuevo marcador
		//title(para el titulo) snippet(para el resto del texto)
		monedaCord = new LatLng(-33.442909,-70.65386999999998);
		moneda = mapa.addMarker(new MarkerOptions().position(monedaCord).title("Usted quiere ir a la moneda"));
		
		EstNacCoord = new LatLng(-33.46449847739583,-70.6105856847168);
		estNacCoord = mapa.addMarker(new MarkerOptions().position(EstNacCoord).title("Usted quiere al Estadio"));
		
		//Setear una variable Location
		Estadio =new Location("Estadio");
		Estadio.setLatitude(-33.46449847739583);
		Estadio.setLongitude(-70.6105856847168);
		
		Moneda =new Location("Moneda");
		Moneda.setLatitude(-33.442909);
		Moneda.setLongitude(-70.65386999999998);
		
		//Distancia entre el punto A y B
		distance = Moneda.distanceTo(Estadio);
		distance = distance/1000;
		
		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker marker) {
				Toast.makeText(
						MainActivity.this, 
						marker.getTitle()+"\n"+"Esta es informacion del Item:\n"+distance+"KM",Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		
		/*Polyline line = mapa.addPolyline(new PolylineOptions()
        .add(monedaCord, EstNacCoord)
        .geodesic(true));*/	
		
		// Instantiates a new CircleOptions object and defines the center and radius
		CircleOptions circleOptions = new CircleOptions();
		// Indico las coordenadas del centro y el radio en metros
		circleOptions.center(new LatLng(-33.442909, -70.65386999999998)).radius(10000);
		circleOptions.strokeColor(Color.GREEN);
		// Get back the mutable Circle
		Circle circle = mapa.addCircle(circleOptions);
		
		//Dibujo ruta entre mas de 2 puntos
		String url = getMapsApiDirectionsUrl();
	    ReadTask downloadTask = new ReadTask();
	    downloadTask.execute(url);
	 
	}
	
	public void centrarMapa(){
		//Llamo al servico de localizacion	        
	    handle = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    //Clase criteria permite decidir mejor poveedor de posicion
	    Criteria c = new Criteria();
	    //obtiene el mejor proveedor en función del criterio asignado
	    //ACCURACY_FINE(La mejor presicion)--ACCURACY_COARSE(PRESISION MEDIA)
	    c.setAccuracy(Criteria.ACCURACY_COARSE);
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
		    Miubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());
			configGPS(Miubicacion);
		}else
		{	
			//Miubicacion = new LatLng(-33.442909,-70.65386999999998);
			//Toast t1 = Toast.makeText(this, "No se pudo Obtener la ubicacion", Toast.LENGTH_LONG);
			//t1.show();
			//configGPS(Miubicacion);
		 }		
	}
	
	private void configGPS(LatLng latlong) {
		
		// TODO Auto-generated method stub
		//Se indica el latitud y longitud y zoom
		CameraPosition cameraPosition = new CameraPosition.Builder().target(latlong).zoom(12).build();	
		//Se mueve la camara a lo indicado anteriormente
		mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
		Miubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());
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
	//monedaCord,EstNacCoord
	 private String getMapsApiDirectionsUrl() {
		    String waypoints = "waypoints=optimize:true|"
		    		+ Miubicacion.latitude + "," + Miubicacion.longitude
		            + "|" + "|" 
		    		+ monedaCord.latitude + "," + monedaCord.longitude
		            + "|" + "|" 
		            + EstNacCoord.latitude + ","+ EstNacCoord.longitude;
		    String sensor = "sensor=false";
		    String params = waypoints + "&" + sensor;
		    String output = "json";
		    String url = "https://maps.googleapis.com/maps/api/directions/"
		        + output + "?" + params;
		    //tv1.setText(url);
		    return url;
		  }
		 
		  private class ReadTask extends AsyncTask<String, Void, String> {
		    @Override
		    protected String doInBackground(String... url) {
		      String data = "";
		      try {
		        HttpConnection http = new HttpConnection();
		        data = http.readUrl(url[0]);
		      } catch (Exception e) {
		        Log.d("Background Task", e.toString());
		      }
		      return data;
		    }
		 
		    @Override
		    protected void onPostExecute(String result) {
		      super.onPostExecute(result);
		      new ParserTask().execute(result);
		    }
		  }
		 
		  private class ParserTask extends
		      AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
		 
		    @Override
		    protected List<List<HashMap<String, String>>> doInBackground(
		        String... jsonData) {
		 
		      JSONObject jObject;
		      List<List<HashMap<String, String>>> routes = null;
		 
		      try {
		        jObject = new JSONObject(jsonData[0]);
		        PathJSONParser parser = new PathJSONParser();
		        routes = parser.parse(jObject);
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		      return routes;
		    }
		 
		    @Override
		    protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
		      ArrayList<LatLng> points = null;
		      PolylineOptions polyLineOptions = null;
		 
		      // traversing through routes
		      for (int i = 0; i < routes.size(); i++) {
		        points = new ArrayList<LatLng>();
		        polyLineOptions = new PolylineOptions();
		        List<HashMap<String, String>> path = routes.get(i);
		 
		        for (int j = 0; j < path.size(); j++) {
		          HashMap<String, String> point = path.get(j);
		 
		          double lat = Double.parseDouble(point.get("lat"));
		          double lng = Double.parseDouble(point.get("lng"));
		          LatLng position = new LatLng(lat, lng);
		 
		          points.add(position);
		        }
		 
		        polyLineOptions.addAll(points);
		        polyLineOptions.width(2);
		        polyLineOptions.color(Color.BLUE);
		      }
		 
		      mapa.addPolyline(polyLineOptions);
		    }
		  }
}