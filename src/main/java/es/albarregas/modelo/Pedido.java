
package es.albarregas.modelo;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Francisco
 */

@Entity
@Table(name="pedidos")
public class Pedido implements java.io.Serializable {
    
    //con AI
    @Id
    @Column(name="IDPEDIDO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private int idPedido;
                                
    @OneToMany(mappedBy="pedido",cascade = CascadeType.ALL)    //atributo "pedido" de la clase "LineasPedido" / Tabla LINEASPEDIDOS
    private Set<LineasPedido> lineasPedido;
        
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHA")    
    private Date fecha;
    
    //relacion uno a uno con tabla ESTADOSPEDIDO
    @OneToOne
    @JoinColumn(name="ESTADO")    
    private EstadosPedido estado;
    
    @ManyToOne
    @JoinColumn(name="IDCLIENTE")
    private Cliente cliente;
    
    //no es necesario poner anotaciones en todas las columnas; por defecto, al usar JPA todos los atributos se persisten
    private float baseImponible;
    private float descuento;
    private float gastosEnvio;
    private float iva;
    
    @ManyToOne
    @JoinColumn(name = "IDDIRECCION")
    private Direccion direccion;
    
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public Pedido() {
    }

    public Pedido(Date fecha, EstadosPedido estado, Cliente cliente, float baseImponible, float descuento, float gastosEnvio, float iva, Direccion direccion) {
        this.fecha = fecha;
        this.estado = estado;
        this.cliente = cliente;
        this.baseImponible = baseImponible;
        this.descuento = descuento;
        this.gastosEnvio = gastosEnvio;
        this.iva = iva;
        this.direccion = direccion;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters y setters">
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Set<LineasPedido> getLineasPedido() {
        return lineasPedido;
    }

    public void setLineasPedido(Set<LineasPedido> lineasPedido) {
        this.lineasPedido = lineasPedido;
    }
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public EstadosPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadosPedido estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public float getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(float baseImponible) {
        this.baseImponible = baseImponible;
    }

    public float getDescuento() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public float getGastosEnvio() {
        return gastosEnvio;
    }

    public void setGastosEnvio(float gastosEnvio) {
        this.gastosEnvio = gastosEnvio;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    //</editor-fold>    
    
}//CLASS
