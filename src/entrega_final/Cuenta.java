package entrega_final;

public class Cuenta {

    private static int cuentas = 0;
    private String nroCuenta;
    private String nombre;
    private String apellido;
    private String cedula;
    private double monto;
    private String clave;

    public Cuenta(){
        this.nroCuenta = String.valueOf(++Cuenta.cuentas);
    }

    public String getNroCuenta() {
        return nroCuenta;
    }

    public void setNroCuenta(String nroCuenta) {
        this.nroCuenta = nroCuenta;
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public String toString(){
        return "{Id: " + this.nroCuenta + ", nombre: " + this.nombre + ", apellido: "+ this.apellido +
                ", cedula: " + this.cedula + ", monto: " + this.monto + ", clave: " + this.clave + "}";
    }
}