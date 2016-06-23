
package es.albarregas.modelo;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */

@Entity
@Table(name="direcciones")
public class Direccion implements java.io.Serializable {
    
    //con AI (segun el fichero de google docs)    
    //cambiado el tipo de byte (lo que le corresponderia, por ser TINYINT) a short
    //porque el generador automatico de clave (este campo en la tabla esta puesto a AI) 
    //da error, dice que deben usarse ser claves de tipo integer o algo asi; asi que lo cambio a short y asi si funciona    
    @Id    
    @Column (name="IDDIRECCION")
    private short idDireccion;
        
    @OneToMany(mappedBy="direccion")    //atributo "direccion" de la clase "Pedido" (Pedido.direccion) / Tabla PEDIDOS
    private Set<Pedido> pedidos;
        
    @ManyToOne
    @JoinColumn(name="IDCLIENTE")
    private Cliente cliente;
    
    @Column (name="NOMBREDIRECCION")
    private String nombreDireccion;
    
    @Column (name="DIRECCION")
    private String direccion;
    
    @Column(name="CODIGOPOSTAL")
    private char[] codigoPostal;    
    
    @ManyToOne
    @JoinColumn(name="IDPROVINCIA")
    private Provincia provincia;        
        
    @ManyToOne
    @JoinColumn(name="IDPUEBLO")
    private Pueblo pueblo;
        
    @Column (name="TELEFONO")
    private String telefono;
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Direccion() {
    }

    public Direccion(Cliente cliente, String nombreDireccion, String direccion, char[] codigoPostal, Provincia provincia, Pueblo pueblo, String telefono) {
        this.cliente = cliente;
        this.nombreDireccion = nombreDireccion;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.provincia = provincia;
        this.pueblo = pueblo;
        this.telefono = telefono;
    }
    //</editor-fold>
            
    //<editor-fold defaultstate="collapsed" desc="setters y getters">
    public short getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(short idDireccion) {
        this.idDireccion = idDireccion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public String getNombreDireccion() {
        return nombreDireccion;
    }

    public void setNombreDireccion(String nombreDireccion) {
        this.nombreDireccion = nombreDireccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public char[] getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(char[] codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    public Pueblo getPueblo() {
        return pueblo;
    }

    public void setPueblo(Pueblo pueblo) {
        this.pueblo = pueblo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }        
    //</editor-fold>
    
}//CLASS
