package Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Models.Station;
import gonzalobarba.dublinbike.R;

import java.util.ArrayList;


/**
 * Created by gonzalobarbalopez on 09/10/14.
 */
public class StationAdapter extends ArrayAdapter<Station> {

    //Attributes
    Activity context;
    private ArrayList<Station> datos;

    public StationAdapter(Activity context, ArrayList<Station> datos) {
        super(context, R.layout.station_row , datos);
        this.datos = datos;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(R.layout.station_row, null);

        TextView name = (TextView) item.findViewById(R.id.txt_row_station);
        name.setText(datos.get(position).getName());

        TextView id = (TextView) item.findViewById(R.id.txt_row_id);
        id.setText(datos.get(position).getId() + "");

        return (item);
    }
}
