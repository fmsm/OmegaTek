
package es.albarregas.ManagedBeans;

import es.albarregas.dao.DAOGenerico;
import es.albarregas.modelo.Producto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Francisco
 */
@Named
@ViewScoped
public class IndexViewMB implements java.io.Serializable {
    
    private String textoAC;
    private Map<String,Integer> sugerenciasMap;
        
    /**
     * Atributo usado para mostrar en un compenent p:carousel los ultimos productos (12 en total, según Producto.fechaAlta) añadidos a la BD
     * (MEJORAR para que no se almacene una lista de productos, sino solo la información que es mostrada en el carousel de últimos productos
     * -imagen, nombre, y quizas idProducto para el boton/link-)
     */
    private List<Producto> ultimosProductos;
    
        
    /**
     * Método usado para cargar desde la BD, los datos para los atributos 'ultimosProductos' y 'placeholderVentas', para que la vista pueda
     * mostrar esos datos correctamente. Si no lo hago asi (con @PostConstruct), al depender "momentaneamente" 'placeholderVentas' de los datos de 'ultimosProductos'
     * se produciría un error al renderizar la vista asociada
     */
    @PostConstruct
    public void cargarDatosVista() {
        DAOGenerico oDAO = new DAOGenerico();
        
        //rellenar lista ultimosProductos si esta vacia (solo se deberia ejecutar la primera vez que se renderize esta vista)
        if (ultimosProductos == null) { 
            ultimosProductos = new ArrayList();            
            List<Object> temporal = oDAO.leerNamed("leerUltimosProductos");
            Iterator it = temporal.iterator();
            while (it.hasNext() && ultimosProductos.size() < 12) {
                ultimosProductos.add( (Producto) it.next());                
            }//while
        }//if       
        
        System.out.println("[INFO - MB] [IndexViewMB] .cargarDatosVista() @PostConstruct ejecutado");        
    }//metodo
    
    
    /**
     * Método usado por el componente p:autocomplete para obtener la lista de sugerencias (5 en total), segun la cadena de texto introducida en el input field. 
     * Dichas sugerencias, se obtienen mediante una llamada a la BD.
     * Aparte de las cadenas de texto, tambien voy a necesitar el IdProducto, para si se produce el caso de que el ususario selecciona un producto
     * en concreto de la lista.
     * @param query
     * @return 
     */
    public List<String> aCompletar(String query) {
        
        DAOGenerico oDAO = new DAOGenerico();
                
        String clausulaWhere = String.format("where denominacion like '%%%s%%'", query);  //doble %% para 'escapar' %
        List<Object> temporal = oDAO.leer("", "Producto", clausulaWhere, 0, 5);
        
//        List<String> sugerencias = new ArrayList();
//        Iterator it = temporal.iterator();
//        while (it.hasNext() && sugerencias.size() < 5) {
//            sugerencias.add( (String)it.next() );
//        }//while
        
        List<String> sugerencias = new ArrayList();     //Strings que se devolveran y seran mostradas en la vista
        sugerenciasMap = new HashMap();                     //mapa, que contendra el idProducto y la denominacion del producto, para el caso de que el ususario seleccione un producto determinado poder acceder inmediatamente a su id
        Iterator it = temporal.iterator();
        while (it.hasNext()) {
            Producto temp = (Producto) it.next();
            sugerenciasMap.put(temp.getDenominacion(), temp.getIdProducto());
            sugerencias.add(temp.getDenominacion());
        }//while        
        
        return sugerencias;                                
    }//metodo    
    
    /**
     * ActionListener para que cuando el usuario selecciona un producto de entre los que aparecen en la lista de sugerencias del componente
     * p:autocomplete, automaticamente se le rediriga a la pagina de ese producto.
     * La parte de la redirección sacada de inet: http://stackoverflow.com/a/18589746
     * @param event 
     */
    public void acItemSel(SelectEvent event) {
                        
//        if (event != null) {
//            Object item = event.getObject();
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Seleccionado", item.toString());            
//        } else {
//            message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Seleccionado", "Algo Fallo");
//        }
                
        if (event != null) {
            //averiguar la id del producto seleccionado
            String prodSel = (String) event.getObject();
            int idProdSel = sugerenciasMap.get(prodSel);
            String urlRed = String.format("producto.xhtml?id=%d", idProdSel);
            
            //y redireccional a la pagina de detalles del producto seleccionado por el usuario
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(urlRed);
            } catch (IOException ex) {
                Logger.getLogger(IndexViewMB.class.getName()).log(Level.SEVERE, null, ex);
            }//try..catch
            FacesContext.getCurrentInstance().responseComplete();                    
            
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Se produjo un fallo al intentar acceder al producto seleccionado", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }//if..else
                        
    }//acItemSel
    
          
    //<editor-fold defaultstate="collapsed" desc="setters/getters/constructores">
    public IndexViewMB() {
        System.out.println("[INFO - MB] [IndexViewMB] Constructor ejecutado");        
    }
    
    public String getTextoAC() {
        return textoAC;
    }
    
    public Map<String, Integer> getSugerenciasMap() {
        return sugerenciasMap;
    }    

    public void setTextoAC(String textoAC) {
        this.textoAC = textoAC;
    }
    
    public List<Producto> getUltimosProductos() {
        return ultimosProductos;
    }    
    //</editor-fold>    
    
}//CLASS
