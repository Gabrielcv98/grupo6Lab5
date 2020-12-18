package pe.pucp.tel306.firebox;


import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    FloatingActionButton agregar, foto, doc;
    Boolean isAllFabVisible;
    private final int CHOOSE_PDF_FROM_DEVICE = 1001;


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

        agregar= findViewById(R.id.add_fab);
        foto = findViewById(R.id.add_foto_fab);
        doc = findViewById(R.id.add_doc_fab);

        foto.setVisibility(View.GONE);
        doc.setVisibility(View.GONE);

        isAllFabVisible = false;

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllFabVisible){
                    foto.show();
                    doc.show();
                    isAllFabVisible = true;
                }else {
                    foto.hide();
                    doc.hide();
                    isAllFabVisible = false;
                }
            }
        });

        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2.this, "subee", Toast.LENGTH_SHORT).show();

                Log.d("infoApp", "aquí");
                obtenerPath();
            }
        });




    }

    public void obtenerPath (){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        Log.d("infoApp", "llegaste");
        startActivityForResult(intent, CHOOSE_PDF_FROM_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PDF_FROM_DEVICE && resultCode == RESULT_OK){
            if (data != null){
                Log.d("infoApp", data.getData().toString());

            }
        }
    }

    public  void subirArchivo (View view){
        Toast.makeText(this, "holoaaaa", Toast.LENGTH_SHORT).show();

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


}