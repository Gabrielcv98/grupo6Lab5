package pe.pucp.tel306.firebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

import pe.pucp.tel306.firebox.Adaptadores.AdaptadorCarpetas;

public class MainActivity2 extends AppCompatActivity {

    private ArrayList<String> listaCarpetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Obtencion de datos
        Bundle parametros = this.getIntent().getExtras();
        String email = parametros.getString("email");
        TextView textprueba = (TextView) findViewById(R.id.datoRecibido);
        textprueba.setText(email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String uid = user.getUid();
            Log.d("uid",uid);
            //tambien se puede obtener su email, pero ya lo pase entre activities
        }else {
            Log.d("Estado del usuario:","no logueado");

        }

        //Guardado de datos
        SharedPreferences.Editor pref = getSharedPreferences("Datos", Context.MODE_PRIVATE).edit();
        pref.putString("email",email);
        pref.apply();

        //Cerrar Sesion
        Button logout;
        logout = (Button) findViewById(R.id.cerrarSesion);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Borrado de datos
                SharedPreferences.Editor pref = getSharedPreferences("Datos", Context.MODE_PRIVATE).edit();
                pref.clear();
                pref.apply();

                FirebaseAuth.getInstance().signOut();
                onBackPressed();
            }
        });

    }

    public ArrayList<String> listarArchivos(final ArrayList<String> listaCarpetas) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(user.getUid());

        reference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        int cantidadElementos = listResult.getItems().size();
                        Log.d("infoApp", "cantidad de elementos: " + cantidadElementos);

                        Log.d("infoApp", "carpetas: " + listResult.getPrefixes().size());


                        for (StorageReference ref : listResult.getItems()) {
                            Log.d("infoApp", "elemento: " + ref.getName());

                            listaCarpetas.add(ref.getName());

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.d("infoApp", "Error al listar");
                    }

                });
        return listaCarpetas;
    }
    public ArrayList<String> lol(){
        ArrayList<String> dato = new ArrayList<>();
        dato.add("Holi");
        dato.add("Holi");
        dato.add("Holi");
        dato.add("Holi");dato.add("Holi");dato.add("Holi");


     return dato;
    }

}