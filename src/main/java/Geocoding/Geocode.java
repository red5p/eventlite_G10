package Geocoding;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.data.util.Pair;




import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

public class Geocode {
	public static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoidzU3NjY0bWsiLCJhIjoiY2tuamhxa2c3MDE4ZjJwbDRjOXltd2FhMiJ9.3VK7Vq1Z6SClt1Ic2SzzQA";

	public static Pair<Double, Double> getLatAndLonForAddress(String address) {

		CountDownLatch countdown = new CountDownLatch(1);
		final Pair<Double, Double>[] coordinate = new Pair[1];
		MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
			.accessToken(MAPBOX_ACCESS_TOKEN)
			.query(address)
			.build();
		
		mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
			@Override
			public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
		 
				List<CarmenFeature> sol = response.body().features();
		 
				if (sol.size() > 0) {
				  Point theFirstPoint = sol.get(0).center();
				  System.out.println("onResponse: " + theFirstPoint.toString());
				  coordinate[0] = Pair.of(theFirstPoint.latitude(), theFirstPoint.longitude());
		 
				} else {
					System.out.println("onResponse: No results");
				}
				
				countdown.countDown();
			}
		 
			@Override
			public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
				System.out.println("onFailure");
				throwable.printStackTrace();
				countdown.countDown();
			}
		});
		
		while(true) {
			try {
				countdown.await();
				break;
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return coordinate[0];
		
		
	}


}
