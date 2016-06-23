
package es.albarregas.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name="confyprods")
public class Confyprod implements java.io.Serializable {
    
    //con AI (esta tabla no viene definida en el fichero .pdf)
    //cambiado el tipo de byte (lo que le corresponderia, por ser TINYINT) a short
    //porque el generador automatico de clave (este campo en la tabla esta puesto a AI) 
    //da error, dice que deben usarse ser claves de tipo integer o algo asi; asi que lo cambio a short y asi si funciona    
    @Id
    @Column(name="IDCONFYPRODS")
    private short idConfyProds;
    
    @ManyToOne
    @JoinColumn(name = "IDPRODUCTO")
    private Producto producto;
    
    @ManyToOne
    @JoinColumn(name = "IDCONFIGURACION")
    private Configuracion configuracion;
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Confyprod() {
    }    

    public Confyprod(Producto producto, Configuracion configuracion) {
        this.producto = producto;
        this.configuracion = configuracion;
    }
    //</editor-fold>
            
    //<editor-fold defaultstate="collapsed" desc="setters y getters">
    public short getIdConfyProds() {
        return idConfyProds;
    }

    public void setIdConfyProds(short idConfyProds) {
        this.idConfyProds = idConfyProds;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuracion configuracion) {
        this.configuracion = configuracion;
    }
    //</editor-fold>
    
}//CLASS
