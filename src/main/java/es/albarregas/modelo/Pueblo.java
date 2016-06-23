
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
@Table(name="pueblos")
public class Pueblo implements java.io.Serializable {
    
    //con AI
    //los datos de esta tabla (igual que PROVINCIAS se carga desde el principio de fichero externo)
    //para que funcione, hay que poner en la tabla la columna como auto-increment
    @Id
    @Column (name="IDPUEBLO")
    private int idPueblo;
    
    //relacion PUEBLOS -- DIRECCIONES    
    @OneToMany(mappedBy="pueblo")  //atributo "pueblo" de la clase "Direccion.java"
    private Set<Direccion> direcciones;
    
    //relacion PUEBLOS -- PROVEEDORES    
    @OneToMany(mappedBy="pueblo")  //atributo "pueblo" de la clase "Proveedor.java"
    private Set<Proveedor> proveedores;    
    
    @ManyToOne
    @JoinColumn(name="IDPROVINCIA")
    private Provincia provincia;    
    
    @Column(name="CODIGOPOSTAL")
    private char[] codigoPostal;
    
    @Column(name="NOMBRE")
    private String nombre;
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Pueblo() {
    }

    public Pueblo(Provincia provincia, char[] codigoPostal, String nombre) {
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.nombre = nombre;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public char[] getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(char[] codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public int getIdPueblo() {
        return idPueblo;
    }

    public void setIdPueblo(int idPueblo) {
        this.idPueblo = idPueblo;
    }

    public Set<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(Set<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    public Set<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(Set<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    //</editor-fold>
        
}//CLASS
