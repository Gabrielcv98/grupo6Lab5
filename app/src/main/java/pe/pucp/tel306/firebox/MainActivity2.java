package pe.pucp.tel306.firebox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button logout;
        Bundle parametros = this.getIntent().getExtras();
        String email = parametros.getString("email");
        TextView textprueba = (TextView) findViewById(R.id.datoRecibido);
        textprueba.setText(email);
        logout = (Button) findViewById(R.id.cerrarSesion);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                onBackPressed();
            }
        });

    }
}