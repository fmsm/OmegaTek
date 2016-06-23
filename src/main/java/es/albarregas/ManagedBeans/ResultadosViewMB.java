
package es.albarregas.ManagedBeans;

import es.albarregas.dao.DAOGenerico;
import es.albarregas.modelo.Producto;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Francisco
 */
@Named
@ViewScoped
public class ResultadosViewMB implements java.io.Serializable {
    
    private String query;       //atributo donde voy a introducir el contenido del componente p:autocomplete de la vista "index.html"        
    private LazyDataModel<Producto> model;   
    long numTotalResultados;
    
    @PostConstruct
    public void init() {
        
        DAOGenerico oDAO = new DAOGenerico();
        
        if (query == null) {    
            //accedo al mapa donde vienen los parámetros de la request
            Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            //busco el parámetro con nombre igual al id del componente p:autocomplete (al que automaticamente primefaces le añade el sufijo _input, a la parte del componente donde el usuario introduce texto)
            //y le asigno su valor al atributo 'query' de este managedbean
            if (requestParameterMap.containsKey("headForm:buscarProd_input")) {
               query = requestParameterMap.get("headForm:buscarProd_input");
            } else {
               query = null;
            }//if..else
        }//if query
        
        //averiguar numero total de resultados para la busqueda representada por el atributo query
        String clausulaWhere = String.format("where denominacion like '%%%s%%'", query);  //doble %% para 'escapar' %
        numTotalResultados = (long) oDAO.leer("count(*)", "Producto", clausulaWhere, -1, -1).iterator().next();

        this.model = new LazyDataModel<Producto>() {
            
            /**
             * Metodo en el que se implementa el "lazy loading" de productos de la BD, cuya denominacion contenga el texto introducido en el campo p:autocomplete
             * y contenido en el atributo query de este managedbean.
             * Obtiene de la BD un numero de productos igual al que van a ser mostrados por pantalla y no todos los productos que hay en la BD.
             * Igual que en CateViewMB.java
             */
            @Override
            public List<Producto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                
                DAOGenerico oDAO = new DAOGenerico();
                model.setRowCount( (int) numTotalResultados );
                
                //obtenemos los productos que se van a mostrar por pantalla (solo un número limitado, indicado por los parámetros
                //first y pagesize, que vienen definidos en la vista
                String clausulaWhere = String.format("where denominacion like '%%%s%%'", query);  //doble %% para 'escapar' %                
                List<Object> temporal = oDAO.leer("", "Producto", clausulaWhere, first, pageSize);
                Iterator it = temporal.iterator();
                List<Producto> result = new ArrayList();                
                while (it.hasNext()) {
                    result.add( (Producto) it.next());                
                }//while                
                
                return result;
            }//load
            
        };//new LazyDataModel
        
        System.out.println("[INFO - MB] [ResultadowViewMB] .init() @PostConstruct ejecutado");
        
    }//init
                    
    //<editor-fold defaultstate="collapsed" desc="setters/getters/constructores">
    public ResultadosViewMB() {
        System.out.println("[INFO - MB] [ResultadosViewMB] Constructor ejecutado"); 
    }
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }        
    
    public LazyDataModel<Producto> getModel() {
        return model;
    }
    
    public long getNumTotalResultados() {
        return numTotalResultados;
    }    
    //</editor-fold>
    
}//CLASS
