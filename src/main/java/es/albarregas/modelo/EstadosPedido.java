
package es.albarregas.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Francisco
 */
@Entity
@Table(name="estadospedido")
public class EstadosPedido implements java.io.Serializable {
    
    //sin AI
    @Id
    @Column(name="IDESTADO")
    private char idEstado;
    
    @Column(name="DESCRIPCION")
    private String descripcion;
    
    @Column(name="ORDEN")
    private byte orden;
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public EstadosPedido() {
    }

    public EstadosPedido(char idEstado, String descripcion, byte orden) {
        this.idEstado = idEstado;
        this.descripcion = descripcion;
        this.orden = orden;
    }
    //</editor-fold>
        
    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public char getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(char idEstado) {
        this.idEstado = idEstado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte getOrden() {
        return orden;
    }

    public void setOrden(byte orden) {
        this.orden = orden;
    }
    //</editor-fold>
        
}//CLASS
