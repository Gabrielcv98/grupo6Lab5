package pe.pucp.tel306.firebox.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import pe.pucp.tel306.firebox.Adaptadores.AdaptadorCarpetas;
import pe.pucp.tel306.firebox.MainActivity2;
import pe.pucp.tel306.firebox.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarpetasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarpetasFragment extends Fragment {
    ArrayAdapter adapter;

    ListView lv;
    public CarpetasFragment() {
        // Required empty public constructor
    }
    public static CarpetasFragment newInstance() {
        CarpetasFragment fragment = new CarpetasFragment();

        return fragment;
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view  =inflater.inflate(R.layout.fragment_carpetas, container, false);
        ArrayList<String> listaCarpetas = new ArrayList<>();
        MainActivity2 activity2 = (MainActivity2) getActivity();
       listaCarpetas =  activity2.lol();
        lv =(ListView) view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,listaCarpetas);
        lv.setAdapter(adapter);



        return view;

    }
}