package pe.pucp.tel306.firebox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import pe.pucp.tel306.firebox.Entity.Usuario;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class LoginyRegistro extends AppCompatActivity {

    private int google_sign_in = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginyregistro);

        final EditText inputEmail, inputContra;
        inputEmail = (EditText) findViewById(R.id.editTextSesion);
        inputContra = (EditText) findViewById(R.id.editTextTextPassword);
        Button btnRegistro,btnIniciarSesion,btnGoogle;

        btnRegistro = (Button) findViewById(R.id.buttonRegistrarseInicio);
        btnIniciarSesion = (Button) findViewById(R.id.buttonIniciarSesionPri);
        btnGoogle = (Button) findViewById(R.id.buttonRegistroConGoogle);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputEmail.getText().toString().equals("") && !inputContra.getText().toString().equals("")){
                    try {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(inputEmail.getText().toString(), inputContra.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    ingresoExitoso(inputEmail.getText().toString());
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
                                ingresoExitoso(inputEmail.getText().toString());
                            }else {
                                mostrarError();
                            }
                        }
                    });
                }

                }
            }
        );

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions googleconf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient googleClient = GoogleSignIn.getClient(getApplicationContext(),googleconf);
                googleClient.signOut();

                startActivityForResult(googleClient.getSignInIntent(),google_sign_in);

            }
        });


        session();
    }

    public void session(){
        SharedPreferences pref = getSharedPreferences("Datos", Context.MODE_PRIVATE);
        String email = pref.getString("email",null);
        if (email != null){
            ingresoExitoso(email);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == google_sign_in){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                final GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null){
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                ingresoExitoso(account.getEmail());
                            }else {
                                mostrarError();
                            }
                        }
                    });
                }
            } catch (ApiException e) {
                mostrarError();
            }
        }
    }

    public void mostrarError(){
        Toast.makeText(getApplicationContext(),"Error: el usuario no se pudo autenticar correctamente",
                Toast.LENGTH_SHORT).show();
    }

    public void ingresoExitoso(String inputEmail){
        Bundle params = new Bundle();
        params.putString("email",inputEmail);
        Intent i = new Intent(getApplicationContext(), MainActivity2.class);
        i.putExtras(params);
        startActivity(i);
    }




}