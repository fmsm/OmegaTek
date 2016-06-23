
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
@Table(name="configuraciones")
public class Configuracion implements java.io.Serializable{
        
    //con AI (esta tabla no viene definida en el fichero .pdf)
    //cambiado el tipo de byte (lo que le corresponderia, por ser TINYINT) a short
    //porque el generador automatico de clave (este campo en la tabla esta puesto a AI) 
    //da error, dice que deben usarse ser claves de tipo integer o algo asi; asi que lo cambio a short y asi si funciona
    @Id
    @Column(name="IDCONFIGURACION")
    private short idConfiguracion;
        
    //relacion con "Confyprod.java"    
    @OneToMany(mappedBy = "configuracion")   //atributo "configuracion" de la clase "Confyprod.java"
    private Set<Confyprod> confyprods;    
    
    @Column(name="NOMBRE")
    private String nombre;    

    @Column(name="DESCRIPCION", columnDefinition = "TEXT")
    private String descripcion;
    
            
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Configuracion() {
    }

    public Configuracion(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="setters y getters">
    public short getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(short idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public Set<Confyprod> getConfyprods() {
        return confyprods;
    }

    public void setConfyprods(Set<Confyprod> confyprods) {
        this.confyprods = confyprods;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    //</editor-fold>
    
}//CLASS
