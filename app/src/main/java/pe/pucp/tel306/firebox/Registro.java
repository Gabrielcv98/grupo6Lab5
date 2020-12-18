package pe.pucp.tel306.firebox;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import pe.pucp.tel306.firebox.Entity.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Registro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registro extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Registro() {
        // Required empty public constructor
    }

    public static Registro newInstance(String param1, String param2) {
        Registro fragment = new Registro();
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
    Button btnCancelar, btnRegistro;
    EditText txtUser, txtPwd, txtNames, txtApellidos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        vista = inflater.inflate(R.layout.fragment_registro, container, false);
        txtUser = (EditText) vista.findViewById(R.id.emailRegistro);
        txtPwd = (EditText) vista.findViewById(R.id.contraRegistro);
        txtNames= (EditText) vista.findViewById(R.id.nombre);
        txtApellidos = (EditText) vista.findViewById(R.id.apellidos);

        btnCancelar = (Button) vista.findViewById(R.id.buttonCancelar);
        btnRegistro = (Button) vista.findViewById(R.id.buttonAceptarRegistro);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciar_sesion();
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtNames.getText().toString();
                String apellidos = txtApellidos.getText().toString();
                String email = txtUser.getText().toString();
                String contra = txtPwd.getText().toString();
                String tipoCuenta = "cuenta básica";
                int almacenamientoTopeEnMB = 200;
                Usuario usuario = new Usuario();
                usuario.setNombre(name);
                usuario.setApellido(apellidos);
                usuario.setAlmacenamiento(almacenamientoTopeEnMB);
                usuario.setTipo_usuario(tipoCuenta);
                usuario.setEmail(email);
                usuario.setPassword(contra);

                LoginyRegistro m2 = (LoginyRegistro) getActivity();
                m2.Registro(usuario);
            }
        });

        return vista;
    }

    void iniciar_sesion() {
        IniciarSesion fr=new IniciarSesion();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.esc_reutilizable,fr)
                .addToBackStack(null)
                .commit();
    }
}