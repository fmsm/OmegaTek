
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
@Table(name="categorias")
public class Categoria implements java.io.Serializable{
    
    //con AI (segun el fichero de google docs)
    //cambiado el tipo de byte (lo que le corresponderia, por ser TINYINT) a short
    //porque el generador automatico de clave (este campo en la tabla esta puesto a AI) 
    //da error, dice que deben usarse ser claves de tipo integer o algo asi; asi que lo cambio a short y asi si funciona    
    @Id
    @Column(name="IDCATEGORIA")
    private short idCategoria;
    
    @Column(name="NOMBRE")
    private String nombre;    
        
    @OneToMany(mappedBy="categoria")    //atributo "categoria" de la clase "Producto" (Producto.categoria)
    private Set<Producto> productos;    
        
    @OneToMany(mappedBy="categoria")    //atributo "categoria" de la clase "Caracteristica" (Caracteristica.categoria)
    private Set<Caracteristica> caracteristicas;    
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Categoria() {
    }

    public Categoria(byte idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }
    //</editor-fold>
        
    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public short getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(short idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    public Set<Caracteristica> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(Set<Caracteristica> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    //</editor-fold>
            
}//CLASS
