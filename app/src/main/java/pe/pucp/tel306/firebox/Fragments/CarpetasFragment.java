package pe.pucp.tel306.firebox.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pe.pucp.tel306.firebox.Adaptadores.AdaptadorCarpetas;
import pe.pucp.tel306.firebox.Entity.Usuario;
import pe.pucp.tel306.firebox.LoginyRegistro;
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
    Button btnPrviado;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view  =inflater.inflate(R.layout.fragment_carpetas, container, false);
        ArrayList<String> listaCarpetas = new ArrayList<>();
        MainActivity2 activity2 = (MainActivity2) getActivity();
       listaCarpetas =  activity2.listarArchivos(listaCarpetas);
        lv =(ListView) view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.items_carpetas,listaCarpetas);
        lv.setAdapter(adapter);

        btnPrviado = (Button) view.findViewById(R.id.botonPrivados);
        btnPrviado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginyRegistro activity1 = (LoginyRegistro) getActivity();
                MainActivity2 activity2 = (MainActivity2) getActivity();
                Usuario usuario;

                usuario = activity1.obtenerDatosFireStore();

                usuario.getArchivos_privados().add("Aqui deberia ir el archivo seleccionado");
                usuario.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Map<String, Object> aPrivados = new HashMap<>();

                for(int i=0;i<usuario.getArchivos_privados().size();i++) {
                    if (i > 4) {
                        Toast.makeText(activity2, "Solo puede tener 5 archivos privados", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    aPrivados.put("archivo " + i + 1, usuario.getArchivos_privados().get(i));
                }

                    FirebaseFirestore dbF = FirebaseFirestore.getInstance();
                    DocumentReference usersColeccion = dbF.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    usersColeccion.collection("privateFiles").add(aPrivados);

            }
        });

        return view;


    }


}