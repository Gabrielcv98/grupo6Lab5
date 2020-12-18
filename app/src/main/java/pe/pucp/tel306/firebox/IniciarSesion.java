package pe.pucp.tel306.firebox;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IniciarSesion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IniciarSesion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IniciarSesion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IniciarSesion.
     */
    // TODO: Rename and change types and number of parameters
    public static IniciarSesion newInstance(String param1, String param2) {
        IniciarSesion fragment = new IniciarSesion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View vista;
    Button btnIniciarSesion,btnRegistrar,btnGoogle;
    EditText inputEmail, inputContra;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_iniciar_sesion, container, false);
        btnIniciarSesion = (Button) vista.findViewById(R.id.buttonIniciarSesionPri);
        btnRegistrar = (Button) vista.findViewById(R.id.buttonRegistrarseInicio);
        btnGoogle = (Button) vista.findViewById(R.id.buttonIniciarConGoogle);
        inputEmail = (EditText) vista.findViewById(R.id.editTextSesion);
        inputContra = (EditText) vista.findViewById(R.id.editTextTextPassword);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String contra = inputContra.getText().toString();
                LoginyRegistro m2 = (LoginyRegistro) getActivity();
                m2.iniciarSesion(email,contra);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar_usuario();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginyRegistro m2 = (LoginyRegistro) getActivity();
                m2.inicioDeSesionConGoogle();
            }
        });

        return vista;
    }

    void registrar_usuario(){
        Registro fr=new Registro();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.esc_reutilizable,fr)
                .addToBackStack(null)
                .commit();
    }
}