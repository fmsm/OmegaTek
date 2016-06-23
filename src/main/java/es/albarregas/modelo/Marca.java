
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
@Table(name="marcas")
public class Marca implements java.io.Serializable{
        
    //con AI (esta tabla no viene definida en el fichero .pdf)
    //cambiado el tipo de byte (lo que le corresponderia, por ser TINYINT) a short
    //porque el generador automatico de clave (este campo en la tabla esta puesto a AI) 
    //da error, dice que deben usarse ser claves de tipo integer o algo asi; asi que lo cambio a short y asi si funciona    
    @Id
    @Column(name="IDMARCA")
    private short idMarca;
    
    @Column(name="DENOMINACION")
    private String denominacion;    
        
    @OneToMany(mappedBy="marca")    //atributo "marca" de la clase "Producto" (Producto.marca)
    private Set<Producto> productos;    
        
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Marca() {
    }

    public Marca(String denominacion) {
        this.denominacion = denominacion;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public short getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(short idMarca) {
        this.idMarca = idMarca;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }
    //</editor-fold>
    
}//CLASS
