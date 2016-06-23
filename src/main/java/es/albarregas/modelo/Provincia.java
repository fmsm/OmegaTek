
package es.albarregas.modelo;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */

@Entity
@Table(name="provincias")
public class Provincia implements java.io.Serializable {
    
    //con AI
    //los datos de esta tabla (igual que PUEBLOS se carga desde el principio de fichero externo)
    //para que funcione, hay que poner en la tabla la columna como auto-increment
    @Id    
    @Column(name="IDPROVINCIA")
    private byte idProvincia;
        
    //relacion PROVINCIAS -- DIRECCIONES    
    @OneToMany(mappedBy="provincia")  //atributo "pueblo" de la clase "Direccion.java"
    private Set<Direccion> direcciones;
    
    //relacion PROVINCIAS -- PROVEEDORES    
    @OneToMany(mappedBy="provincia")  //atributo "pueblo" de la clase "Proveedor.java"
    private Set<Proveedor> proveedores;
        
    @Column(name="NOMBRE")
    private String nombre;
        
    @OneToMany(mappedBy="provincia")    //atributo "provincia" de la clase "Pueblo" (Pueblo.provincia)
    private Set<Pueblo> pueblos;    
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Provincia() {
    }

    public Provincia(String nombre) {
        this.nombre = nombre;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public byte getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(byte idProvincia) {
        this.idProvincia = idProvincia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Pueblo> getPueblos() {
        return pueblos;
    }

    public void setPueblos(Set<Pueblo> pueblos) {
        this.pueblos = pueblos;
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
    
    //</editor-fold>
    
}//CLASS
