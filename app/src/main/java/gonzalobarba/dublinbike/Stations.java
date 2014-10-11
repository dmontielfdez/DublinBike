package gonzalobarba.dublinbike;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.StationAdapter;
import Models.Station;

/**
 * Created by gonzalobarbalopez on 05/10/14.
 */
public class Stations extends Fragment {

    final String TAG = "Debugging";
    final String Url = "http://api.citybik.es/v2/networks/sevici";

    private ListView lstStations;
    ArrayList<Station> dataset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.station_list, container, false);
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lstStations = (ListView)getView().findViewById(R.id.lstStations);

        RequestStations();

       // lstStations.setAdapter(new StationAdapter(this, dataset));

        try{
            //lstStations.setAdapter(new StationAdapter(this, dataset));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void RequestStations() {
        RequestQueue queue = Volley.newRequestQueue(this.getActivity());

        final ProgressDialog progressDialog = ProgressDialog.show(this.getActivity(), "Wait please","Loading..");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        progressDialog.cancel();
                        try {
                            JSONObject jsonObject = response.getJSONObject("network");
                            JSONArray arrayStations = jsonObject.getJSONArray("stations");
                            dataset = new ArrayList<Station>();
                            dataset = Parser(arrayStations);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Mostrar mensaje al usuario
                        Log.e(TAG, "headers: " + error.networkResponse.headers);
                        Log.e(TAG, "statusCode: " + error.networkResponse.statusCode);
                        progressDialog.cancel();
                    }
                }
        );
        queue.add(req);
    }

    public ArrayList<Station> Parser(JSONArray response){
        ArrayList<Station> stations = new ArrayList<Station>();
        for(int i = 0; i < response.length(); i++){
            Station station = new Station();
            try {
                //Getting object
                JSONObject temp = (JSONObject) response.get(i);
                JSONObject extra = temp.getJSONObject("extra");

                //set values
                //extra
                station.setAddress(extra.getString("address"));
                station.setBanking(extra.getString("banking"));
                station.setBonus(extra.getString("bonus"));
                station.setLast_update(extra.getString("last_update"));
                station.setSlots(Integer.parseInt(extra.getString("slots")));
                station.setStatus(extra.getString("status"));

                station.setEmpty_slots(Integer.parseInt(temp.getString("empty_slots")));
                station.setFree_bikes(Integer.parseInt(temp.getString("free_bikes")));
                station.setId(temp.getString("id"));
                station.setLatitude(temp.getString("latitude"));
                station.setLongitude(temp.getString("longitude"));
                station.setName(temp.getString("name"));
                station.setTimestamp(temp.getString("timestamp"));
                //Adding object
                stations.add(station);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stations;
    }

}
