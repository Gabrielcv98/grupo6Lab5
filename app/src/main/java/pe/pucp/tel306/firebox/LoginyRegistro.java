package pe.pucp.tel306.firebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginyRegistro extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginyregistro);

        final EditText inputEmail, inputContra;
        inputEmail = (EditText) findViewById(R.id.editTextSesion);
        inputContra = (EditText) findViewById(R.id.editTextTextPassword);
        Button btnRegistro;
        Button btnIniciarSesion;
        btnRegistro = (Button) findViewById(R.id.buttonRegistrarseInicio);
        btnIniciarSesion = (Button) findViewById(R.id.buttonIniciarSesionPri);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputEmail.getText().toString().equals("") && !inputContra.getText().toString().equals("")){
                    try {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(inputEmail.getText().toString(), inputContra.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    ingresoExitoso(inputEmail);
                                }else {
                                    mostrarError();
                                }
                            }
                        });
                    }catch (Error error){
                        Toast.makeText(getApplicationContext(),"Error: no esta registrado", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"Error: debe colocar sus datos", Toast.LENGTH_SHORT).show();
                }
            }
        }
        );

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputEmail.getText().toString().equals("") && !inputContra.getText().toString().equals("")){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(inputEmail.getText().toString(), inputContra.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                ingresoExitoso(inputEmail);
                            }else {
                                mostrarError();
                            }
                        }
                    });
                }

                }
            }
        );
    }

    public void mostrarError(){
        Toast.makeText(getApplicationContext(),"Error: el usuario no se pudo autenticar correctamente",
                Toast.LENGTH_SHORT).show();
    }

    public void ingresoExitoso(EditText inputEmail){
        Bundle params = new Bundle();
        params.putString("email",inputEmail.getText().toString());
        Intent i = new Intent(getApplicationContext(), MainActivity2.class);
        i.putExtras(params);
        startActivity(i);
    }






}