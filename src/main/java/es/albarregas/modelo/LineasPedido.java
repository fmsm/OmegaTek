
package es.albarregas.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name="lineaspedidos")
public class LineasPedido implements java.io.Serializable {
        
    @Id
    @Column(name="NUMEROLINEA")
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private int numeroLinea;
        
    @ManyToOne
    @JoinColumn(name = "IDPEDIDO")
    private Pedido pedido;
        
    @ManyToOne
    @JoinColumn(name = "IDPRODUCTO")
    private Producto producto;
    
    @Column(name = "CANTIDAD")
    private byte cantidad;
    
    @Column(name = "PRECIOUNITARIO")
    private float precioUnitario;
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public LineasPedido() {
    }    

    public LineasPedido(int numeroLinea, Pedido pedido, Producto producto, byte cantidad, float precioUnitario) {
        this.numeroLinea = numeroLinea;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public int getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public byte getCantidad() {
        return cantidad;
    }

    public void setCantidad(byte cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    //</editor-fold>    
    
}//CLASS
