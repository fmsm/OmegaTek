
package es.albarregas.dao;

//import es.albarregas.webservice.EmpresaWeb;
//import es.albarregas.webservice.EmpresaWeb_Service;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;

/**
 * Clase genérica para llamar al web-service, independientemente del tipo de datos (Producto, Usuario, ...) que queramos leer
 * (DE MOMENTO SOLO LEER, MEJORAR PARA TAMBIEN SALVAR Y BORRAR)
 * @author Francisco
 */
public class OLD_AccesoTWS<T> {
    
        
//    public List<T> leerWS(String nomClase, String clausulaWhere) {
//        
//        List<T> devolver = new ArrayList();
//        
//        try { 
//            EmpresaWeb_Service service = new EmpresaWeb_Service();            
//            EmpresaWeb port = service.getEmpresaWebPort();
//            
//            String query = nomClase + " " + clausulaWhere;
//            List<Object> result = port.leer(query);
//            Iterator it = result.iterator();            
//            while (it.hasNext()) {
//                devolver.add( (T) it.next());
//            }//while
//
//            System.out.printf("\n[{OK: (leerWS: %s %s) (Registros = %s}]\n",nomClase,clausulaWhere,result.size());
//        } catch (Exception ex) {
//            // TODO handle custom exceptions here
//            System.out.println("\n[{ERROR}]"+ ex);
//        }//try..catch  
//            
//        return devolver;        
//    }
    
}//CLASS
