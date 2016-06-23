
package es.albarregas.ManagedBeans;

import es.albarregas.dao.DAOGenerico;
import es.albarregas.modelo.Producto;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Francisco
 */
@Named
@ViewScoped
public class CateViewMB implements java.io.Serializable {
    
    private int catSeleccionada = -1;    //-1 -> todas las categorias (todos los productos)    
    private LazyDataModel<Producto> model;  //objeto de la clase LazyDataModel, para habilitar "lazy loading" en el datagrid y no cargar
                                            //en memoria todos los objetos de golpe, sino ir obteniendolos segun se vayan a visualizar
        
    @PostConstruct
    public void init() {
        
        //esto esta pillado a medias del showcase y de una pagina de inet (http://www.javacodegeeks.com/2014/01/primefaces-datatable-lazy-loading-with-pagination-filtering-and-sorting-using-jpa-criteria-viewscoped.html)
        this.model = new LazyDataModel<Producto>() {
            
            /**
             * Metodo en el que se implementa el "lazy loading" de productos de la BD. 
             * Obtener de la BD un numero de productos igual al que van a ser mostrados por pantalla y no todos los productos que hay en la BD.
             */
            @Override
            public List<Producto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                DAOGenerico oDAO = new DAOGenerico();
                
                //este método necesita que se le proporcione un valor al atributo model.setRowCount, que debe ser igual al número de resultados totales
                //de la consulta a la BD que se haga, y es necesario proporcionarselo, para que PrimeFaces pueda mostrar el paginador de la vista de resultados
                
                //esta solucion no seria valida si hay una cantidades de registros/productos enorme (mayor que el valor máximo de int)
                int numProductos;
                
                //preparamos la clausula where, dependiendo de la categoria seleccionada actualmente
                String clausulaWhere = "";
                if (catSeleccionada != -1) {
                    clausulaWhere = String.format("where IdCategoria = %d", catSeleccionada);
                }                
                
                //y lanzamos la consulta
                if (catSeleccionada == -1) {
                    //si la categoria es -1 (=todos los productos), obtenemos el numero total de productos que hay en la tablas productos de la BD
                    numProductos = (Integer) oDAO.leerNamed("leerNumeroProductos").iterator().next();
                } else {
                    //si por el contrario, la categoria es una categoria especifica, obtenemos el numero total de productos de esa categoria
                    long temp = (long) oDAO.leer("count(*)", "Producto", clausulaWhere, -1, -1).iterator().next();
                    numProductos = (int) temp;
                }
                
                //y finalmente le asignamos el valor obtendio a model.setRowCount
                model.setRowCount( (int) numProductos);
                
                //para terminar obtenemos los productos que se van a mostrar por pantalla (solo un número limitado, indicado por los parámetros
                //first y pagesize, que vienen definidos en la vista
                List<Object> temporal = oDAO.leer("", "Producto", clausulaWhere, first, pageSize);
                Iterator it = temporal.iterator();
                List<Producto> result = new ArrayList();                
                while (it.hasNext()) {
                    result.add( (Producto) it.next());                
                }//while                
                
                return result;
            }//método          
            
        };//new LazyDataModel
        
        System.out.println("[INFO - MB] [CateViewMB] .init() @PostConstruct ejecutado");
    }//init
    
    public void seleccionCategoria(int opcion) {
        
        this.catSeleccionada = opcion;        
        this.model.setRowIndex(0);
        
    }//metodo

    //<editor-fold defaultstate="collapsed" desc="setters/getters/constructores">
    public CateViewMB() {
        System.out.println("[INFO - MB] [CateViewMB] Constructor ejecutado");        
    }//constructor

    public LazyDataModel<Producto> getModel() {
        return model;
    }

    public void setModel(LazyDataModel<Producto> model) {
        this.model = model;
    }    
    
    public void setCatSeleccionada(int catSeleccionada) {
        this.catSeleccionada = catSeleccionada;
    }
    //</editor-fold>    
    
}//CLASS
