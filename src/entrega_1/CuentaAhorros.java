package entrega_1;

public class CuentaAhorros {
    private int cedula;
    private double saldo;
    private String nombre;
    private String apellido;
    private String clave;

    public CuentaAhorros(String nombre, String apellido, int cedula, double saldo,  String clave) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.saldo = saldo;
        this.clave = clave;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    @Override
    public String toString() {
        return "CuentaAhorros{" +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                "cedula=" + cedula +
                ", saldo=" + saldo + '\'' +
                ", clave='" + clave + '\'' +
                '}';
    }
}

