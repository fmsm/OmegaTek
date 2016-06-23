
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
@Table(name="imagenes")
public class Imagen implements java.io.Serializable {
    
    //con AI
    @Id
    @Column (name="IDIMAGEN")
    private short idImagen;
    
    @ManyToOne
    @JoinColumn(name="IDPRODUCTO")
    private Producto producto;
    
    @Column (name="imagen")
    private String imagen;

    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Imagen() {
    }    

    public Imagen(Producto producto, String imagen) {
        this.producto = producto;
        this.imagen = imagen;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public short getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(short idImagen) {
        this.idImagen = idImagen;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    //</editor-fold>
    
}//CLASS
