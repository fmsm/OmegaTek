
package es.albarregas.modelo;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Francisco
 */

@Entity
@Table(name="proveedores")
public class Proveedor implements java.io.Serializable {
    
    //con AI
    //cambiado el tipo de byte (lo que le corresponderia, por ser TINYINT) a short
    //porque el generador automatico de clave (este campo en la tabla esta puesto a AI) 
    //da error, dice que deben usarse ser claves de tipo integer o algo asi; asi que lo cambio a short y asi si funciona    
    @Id
    @Column(name="IDPROVEEDOR")    
    private short idProveedor;
        
    @OneToMany(mappedBy = "proveedor")  //atributo "proveedor" de la clase java "Producto"
    private Set<Producto> productos;
    
    //no es necesario poner anotaciones en todas las columnas; por defecto, al usar JPA todos los atributos se persisten
    private String razonSocial;
    private String cif;
    private String direccion;
        
    @Column(name="CODIGOPOSTAL")
    private char[] codigoPostal;
    
    @ManyToOne
    @JoinColumn(name="IDPROVINCIA")
    private Provincia provincia;        
        
    @ManyToOne
    @JoinColumn(name="IDPUEBLO")
    private Pueblo pueblo;    
    
    //no es necesario poner anotaciones en todas las columnas; por defecto, al usar JPA todos los atributos se persisten
    private char[] telefono;
    private String email;
    private String web;
    private String personaContacto;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHAALTA")    
    private Date fechaAlta;

    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Proveedor() {
    }

    public Proveedor(String razonSocial, String cif, String direccion, char[] codigoPostal, Provincia provincia, Pueblo pueblo, char[] telefono, String email, String web, String personaContacto, Date fechaAlta) {
        this.razonSocial = razonSocial;
        this.cif = cif;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.provincia = provincia;
        this.pueblo = pueblo;
        this.telefono = telefono;
        this.email = email;
        this.web = web;
        this.personaContacto = personaContacto;
        this.fechaAlta = fechaAlta;
    }
    //</editor-fold>
            
    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public short getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(short idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
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

    public char[] getTelefono() {
        return telefono;
    }

    public void setTelefono(char[] telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
            
    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
    //</editor-fold>
    
}//CLASS
