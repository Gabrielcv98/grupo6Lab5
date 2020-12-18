package pe.pucp.tel306.firebox.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import pe.pucp.tel306.firebox.R;
import java.util.ArrayList;

public class AdaptadorCarpetas extends ArrayAdapter<String> {


    public AdaptadorCarpetas(Context context, ArrayList<String> carpetas) {
        super(context, 0, carpetas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String carpetas = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_carpetas, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.carpetaName);
        Button hacerPrivado = (Button) convertView.findViewById(R.id.botonPrivados) ;
        // Populate the data into the template view using the data object
        tvName.setText(carpetas);

        // Return the completed view to render on screen
        return convertView;
    }
}

