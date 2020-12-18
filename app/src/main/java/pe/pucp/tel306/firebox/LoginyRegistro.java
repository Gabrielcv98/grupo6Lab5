package pe.pucp.tel306.firebox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
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

        //btnRegistro = (Button) findViewById(R.id.buttonRegistrarseInicio);
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

        /*btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputEmail.getText().toString().equals("") && !inputContra.getText().toString().equals("")){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(inputEmail.getText().toString(), inputContra.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                File directorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                File archivo = new File(directorio, "imagen.jpg");

                                try {

                                }catch ()
                                InputStream stream = new FileInputStream(archivo);
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageReference = storage.getReference();
                                StorageReference carpeRoot = storageReference.child("users").child(id);
                                Log.d("infoApp", carpeRoot.toString());
                                ingresoExitoso(inputEmail.getText().toString());
                            }else {
                                mostrarError();
                            }
                        }
                    });
                }

                }
            }
        );*/

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

    String idusuario;
    public void Registro (View view){
        final EditText inputEmail = (EditText) findViewById(R.id.editTextSesion);
        EditText inputContra = (EditText) findViewById(R.id.editTextTextPassword);
        if (!inputEmail.getText().toString().equals("") && !inputContra.getText().toString().equals("")){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(inputEmail.getText().toString(), inputContra.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        idusuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        subirArchivoPutStream(idusuario);

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReference();
                        StorageReference carpeRoot = storageReference.child("users").child(idusuario);
                        Log.d("infoApp", carpeRoot.toString());
                        ingresoExitoso(inputEmail.getText().toString());
                    }else {
                        mostrarError();
                    }
                }
            });
        }
    }

    public void subirArchivoPutStream (String id){
        int permission = ContextCompat.checkSelfPermission(LoginyRegistro.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(externalStoragePublicDirectory, "imagen1.png");
            try {
                InputStream stream = new FileInputStream(file);
                storageReference.child(id + "/subido.jpg").putStream(stream)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("infoApp", "subida exitosa");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("infoApp", "error");
                            }
                        });
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }



        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 1) {
            subirArchivoPutStream(idusuario);
        }
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