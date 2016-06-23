
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
@Table(name="caracyprods")
public class Caracyprod implements java.io.Serializable {
    
    //con AI (esta tabla no viene definida en el fichero .pdf)
    @Id
    @Column(name="IDCARACYPRODS")
    private short idCaracyProd;
    
    @ManyToOne
    @JoinColumn(name = "IDPRODUCTO")
    private Producto producto;
    
    @ManyToOne
    @JoinColumn(name = "IDCARACTERISTICA")
    private Caracteristica caracteristica;
    
    @Column(name ="DESCRIPCION")
    private String descripcion;
    
    @Column(name = "IDX")
    private byte idx;

    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Caracyprod() {
    }

    public Caracyprod(Producto producto, Caracteristica caracteristica, String descripcion, byte idx) {
        this.producto = producto;
        this.caracteristica = caracteristica;
        this.descripcion = descripcion;
        this.idx = idx;
    }
    //</editor-fold>
        
    //<editor-fold defaultstate="collapsed" desc="setters y getters">
    public short getIdCaracyProd() {
        return idCaracyProd;
    }

    public void setIdCaracyProd(short idCaracyProd) {
        this.idCaracyProd = idCaracyProd;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte getIdx() {
        return idx;
    }

    public void setIdx(byte idx) {
        this.idx = idx;
    }
    //</editor-fold>
    
}//CLASS
