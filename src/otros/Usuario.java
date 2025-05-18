package otros;

public class Usuario {
    private String correo;
    private String dni;
    private String codigoPostal;
    private String fechaNacimiento;
    private String telefono;
    private String permisos;

    public Usuario(String correo, String dni, String codigoPostal, String fechaNacimiento, String telefono, String permisos) {
        this.correo = correo;
        this.dni = dni;
        this.codigoPostal = codigoPostal;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.permisos = permisos;
    }

    public String getCorreo() { return correo; }
    public String getDni() { return dni; }
    public String getCodigoPostal() { return codigoPostal; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public String getPermisos() { return permisos; }
}
