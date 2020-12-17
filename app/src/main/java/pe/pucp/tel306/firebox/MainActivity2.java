package pe.pucp.tel306.firebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity {

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
}