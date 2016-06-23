
package es.albarregas.modelo;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Francisco
 */

@Entity
@Table(name="productos")
public class Producto implements java.io.Serializable {
    
    //sin AI (no tiene AI en el fichero .pdf)
    @Id
    @Column(name="IDPRODUCTO")
    private int idProducto;
    
    //relacion PRODUCTOS -- LINEAPEDIDOS    
    @OneToMany(mappedBy = "producto")
    private Set<LineasPedido> lineasPedido;
        
    //relacion con "Imagenes.java"        
    @OneToMany
    @JoinColumn(name="IDPRODUCTO")  //relacion 1 a muchos ORDENADA, IDPRODUCTO representa a la columna del mismo nombre de la tabla Imagenes
    @OrderColumn(name="IDX")        //relacion 1 a muchos ORDENADA, IDX (columna que en la tabla hija contiene el orden dentro de la lista de hijos.)
    private List<Imagen> imagenes;
            
    //relacion con "Confyprod.java"    
    @OneToMany(mappedBy = "producto")   //atributo "producto" de la clase "Confyprod.java"
    private Set<Confyprod> confyprods;
    
    //relacion con "Caracyprod.java"    
    @OneToMany
    @JoinColumn(name="IDPRODUCTO")  //relacion 1 a muchos ORDENADA,
    @OrderColumn(name="IDX")        //relacion 1 a muchos ORDENADA, IDX (columna que en la tabla hija contiene el orden dentro de la lista de hijos.)
    private List<Caracyprod> caracteristicas;
    
    //relacion con "Categoria.java"
    @ManyToOne
    @JoinColumn(name="IDCATEGORIA")
    private Categoria categoria;
    
    //relacion con "Marca.java"
    @ManyToOne
    @JoinColumn(name="IDMARCA")
    private Marca marca;    
    
    //no es necesario poner anotaciones en todas las columnas; por defecto, al usar JPA todos los atributos se persisten
    private String denominacion;
    
    @Column(name="DESCRIPCION", columnDefinition = "TEXT")
    private String descripcion;
    
    //relacion con "Proveedor.java"
    @ManyToOne
    @JoinColumn(name="IDPROVEEDOR")
    private Proveedor proveedor;
    
    //no es necesario poner anotaciones en todas las columnas; por defecto, al usar JPA todos los atributos se persisten
    private float precioUnitario;
    private short stock;
    private short stockMinimo;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHAALTA")    
    private Date fechaAlta;
    
    //no es necesario poner anotaciones en todas las columnas; por defecto, al usar JPA todos los atributos se persisten
    private char oferta;        
    private char fueraCatalogo;
    private byte rating;
    
    
    //<editor-fold defaultstate="collapsed" desc="Constructores (con/sin parametros)">    
    /**
     * Constructor sin parámetros
     */
    public Producto() {
    }

    /**
     * Constructor con parametros, para facilitar el proceso de creacion de objetos de la clase Producto
     * @param idProducto
     * @param categoria
     * @param marca
     * @param denominacion
     * @param descripcion
     * @param proveedor
     * @param precioUnitario
     * @param stock
     * @param stockMinimo
     * @param fechaAlta
     * @param oferta
     * @param fueraCatalogo 
     */
    public Producto(int idProducto, Categoria categoria, Marca marca, String denominacion, String descripcion, Proveedor proveedor, float precioUnitario, short stock, short stockMinimo, Date fechaAlta, char oferta, char fueraCatalogo) {
        this.idProducto = idProducto;
        this.categoria = categoria;
        this.marca = marca;
        this.denominacion = denominacion;
        this.descripcion = descripcion;
        this.proveedor = proveedor;
        this.precioUnitario = precioUnitario;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.fechaAlta = fechaAlta;
        this.oferta = oferta;
        this.fueraCatalogo = fueraCatalogo;
    }
    //</editor-fold>
        
    //<editor-fold defaultstate="collapsed" desc="Setters y Getters">
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public Set<LineasPedido> getLineasPedido() {
        return lineasPedido;
    }

    public void setLineasPedido(Set<LineasPedido> lineasPedido) {
        this.lineasPedido = lineasPedido;
    }
    
    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public List<Caracyprod> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(List<Caracyprod> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public Set<Confyprod> getConfyprods() {
        return confyprods;
    }

    public void setConfyprods(Set<Confyprod> confyprods) {
        this.confyprods = confyprods;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public short getStock() {
        return stock;
    }

    public void setStock(short stock) {
        this.stock = stock;
    }

    public short getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(short stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public char getOferta() {
        return oferta;
    }

    public void setOferta(char oferta) {
        this.oferta = oferta;
    }

    public char getFueraCatalogo() {
        return fueraCatalogo;
    }

    public void setFueraCatalogo(char fueraCatalogo) {
        this.fueraCatalogo = fueraCatalogo;
    }
    
    public byte getRating() {
        return rating;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }    
    //</editor-fold>

    @Transient
    private boolean prdOferta;
    @Transient
    private boolean prdDescat;
    
    public boolean isPrdOferta() {
        return (this.oferta == 's');
    }    
    
    public boolean isPrdDescat() {
        return (this.fueraCatalogo == 's');
    }    

    public void setPrdOferta(boolean prdOferta) {
        this.prdOferta = prdOferta;
    }

    public void setPrdDescat(boolean prdDescat) {
        this.prdDescat = prdDescat;
    }
    
}//CLASS
