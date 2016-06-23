
package es.albarregas.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name="facturas")
public class Factura implements java.io.Serializable {
    
    //con AI (segun el fichero de google docs)    
    @Id
    @Column(name="NUMEROFACTURA")
    private int numeroFactura;
    
    //relacion uno a uno con tabla FACTURAS    
    @OneToOne
    @JoinColumn(name="IDPEDIDO")
    private Pedido pedido;
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Factura() {
    }

    public Factura(Pedido pedido) {
        this.pedido = pedido;
    }
    //</editor-fold>
        
    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    
    //</editor-fold>    
    
}//CLASS
