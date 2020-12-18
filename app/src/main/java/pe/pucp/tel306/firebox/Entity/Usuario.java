package pe.pucp.tel306.firebox.Entity;

import java.util.ArrayList;

public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String tipo_usuario;
    private int almacenamiento;
    private ArrayList<String> archivos_privados;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(String tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    public int getAlmacenamiento() {
        return almacenamiento;
    }

    public void setAlmacenamiento(int almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getArchivos_privados() {
        return archivos_privados;
    }

    public void setArchivos_privados(ArrayList<String> archivos_privados) {
        this.archivos_privados = archivos_privados;
    }
}
