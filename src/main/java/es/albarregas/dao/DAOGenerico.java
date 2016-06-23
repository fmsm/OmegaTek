package es.albarregas.dao;

import es.albarregas.modelo.Mensaje;
import es.albarregas.modelo.Pedido;
import es.albarregas.modelo.Usuario;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;

public class DAOGenerico extends HibernateUtil {
    private String mensajeError = "";
    
    /**
     * Crear o actualizar una fila/registro en la BD
     * @param objeto
     * @return 
     */
    public boolean crearOactualizar(Object objeto) {
        boolean ok = false;
        
        super.openSession();
        
        try {
            super.transaccion = super.sesion.beginTransaction();
            super.sesion.saveOrUpdate(objeto);
            super.transaccion.commit();
            
            ok = true;
        }
        catch (Exception e) {
            super.transaccion.rollback();
            
            this.mensajeError = e.getMessage();
            System.err.println("[ERROR - es.albarregas.dao.DAOGenerico.java] Fallo en método crearOactualizar()\n" + e);
            
            ok = false;
        }
        finally {
            super.closeSession();
        }
        
        System.out.println("[INFO - ACCESSO BD] .crearOActualizar " + objeto.getClass());
        return ok;
    }
    
    
    public List<Object> leer(String peticion) {
        super.openSession();
        List<Object> lResults = null;
        
        try {
            super.transaccion = super.sesion.beginTransaction();
            lResults = super.sesion.createQuery("from " + peticion).setReadOnly(true).list();
        }
        catch (Exception e) {
            this.mensajeError = e.getMessage();
            System.err.println("[ERROR - es.albarregas.dao.DAOGenerico.java] Fallo en método leer()\n" + e);
        }
        finally {
            super.closeSession();
        }

        if (lResults != null) {
            System.out.printf("[INFO - ACCESSO BD] .leer(peticion) - Obtenidos: %d registros de la BD.\n",lResults.size());
        }
        
        return lResults;
    }
    
    /**
     * Leer ultimo Pedido con estado 'n' (nuevo) del cliente que se pase como parámetro
     * @param idCli
     * @return 
     */
    public Pedido leerPedidoN(int idCli) {
        
        super.openSession();
        List<Object> lResults = null;
        Query query;
        char estado = 'n';
        
        try {
            super.transaccion = super.sesion.beginTransaction();
            query = super.sesion.createQuery("FROM Pedido p WHERE estado.idEstado =:estado AND cliente.idCliente =:IdCliente")
                    .setParameter("estado", estado).setParameter("IdCliente", idCli);
            lResults = query.list();
        } catch (Exception e) {
            this.mensajeError = e.getMessage();
            System.err.println("[ERROR - DAOGenerico.java] Fallo en método leerPedidoN()\n" + e);
        }
        finally {
            super.closeSession();
        }
        
        System.out.printf("[INFO - ACCESSO BD] .leerPedidoN(%d).\n",idCli);
        
        if (lResults != null && lResults.size() > 0) {
            return (Pedido) lResults.iterator().next();
        } else {
            return null;
        }
        
        
    }//leerPedidoN
    
    
    /**
     * Leer mensajes enviados o recibidos por el usuario.
     * @param idUsr Usuario.idUsuario del usuario del que se quieren obtener los mensajes
     * @param enviados true para obtener mensajes enviados y false para los mensajes recibidos
     * @return 
     */
    public List<Mensaje> leerMensajesUsr(int idUsr, boolean enviados) {
        
        super.openSession();
        List<Object> lResults = null;
        List<Mensaje> resultados = null;
        Query query;        
        
        try {
            super.transaccion = super.sesion.beginTransaction();
            
            //seleccionar obtener enviados o recibidos
            if (enviados) {
                query = super.sesion.createQuery("FROM Mensaje m WHERE origen.idUsuario=:dst").setParameter("dst", idUsr);
            } else {
                query = super.sesion.createQuery("FROM Mensaje m WHERE destino.idUsuario=:dst").setParameter("dst", idUsr);
            }//seleccionar obtener enviados o recibidos
                        
            lResults = query.list();
        } catch (Exception e) {
            this.mensajeError = e.getMessage();
            System.err.println("[ERROR - DAOGenerico.java] Fallo en método leerPedidoN()\n" + e);
        }
        finally {
            super.closeSession();
        }
        
        if (lResults != null) {
            Iterator<Object> it = lResults.iterator();
            resultados = new ArrayList();
            while (it.hasNext()) {
                resultados.add( (Mensaje) it.next() );
            }
        }
        
        System.out.printf("[INFO - ACCESSO BD] .leerMensajeUsr(%d).\n",idUsr);        
        
        if (lResults != null && lResults.size() > 0) {
             return resultados;
        } else {
            return null;
        }
        
        
    }//leerPedidoN    
    
    
    /**
     * Leer de la base de datos, usando una serie de parámetros que indicaran que es lo que se leerá de la BD
     * @param campo indica si queremos leer un campo determinado; si queremos leer todos los campos, simplemente usamos ""
     * @param clase indica la clase java correspondiente a la tabla a la que se quiere acceder
     * @param clausulaWhere es un String con el contenido de la clausula where, se puede poner como "" y no se usara esa clausula en la sentencia
     * @param indInicio indica el indice del primer resultado que se quiere obtener con la consulta a la BD; si ponemos -1 ignorara este parámetro
     * @param indFinal indica cuantos resultados queremos obtener de la BD,  si ponemos -1 ignorara este parámetro
     * @return 
     */
    public List<Object> leer(String campo, String clase, String clausulaWhere, int indInicio, int indFinal) {
        super.openSession();
        List<Object> lResults = null;
        Query query = null;
        
        //ir construyendo la query dependiendo de los parametros que vengan
        StringBuilder sb = new StringBuilder();        
        String tmp = (campo.isEmpty()) ? "" : "select " + campo;
        sb.append(tmp);
        sb.append(" from ").append(clase).append(" ").append(clausulaWhere);
                
        try {
            super.transaccion = super.sesion.beginTransaction();
//            Query query = super.sesion.createQuery("select " + campo + " from " + clase + " " + clausulaWhere)
//                          .setReadOnly(true).setFirstResult(indInicio).setMaxResults(indFinal);
            if (indInicio == -1) {
                query = super.sesion.createQuery(sb.toString()).setReadOnly(true);
            } else {
                query = super.sesion.createQuery(sb.toString()).setReadOnly(true).setFirstResult(indInicio).setMaxResults(indFinal);
            }
        
            lResults = query.list();
        }
        catch (Exception e) {
            this.mensajeError = e.getMessage();
            System.err.println("[ERROR - es.albarregas.dao.DAOGenerico.java] Fallo en método leer()\n" + e);
        }
        finally {
            super.closeSession();
        }
        
        if (lResults != null) {
            System.out.printf("[INFO - ACCESSO BD] leer(5 param.) - Obtenidos: %d registros. Clase: %s\n",lResults.size(), clase);
        }
        
        return lResults;        
    }    
    
    
    /**
     * Método para leer de la BD usando una namedQuery definida en '/other sources/named-queries.hbm.xml'
     * @param nomNamedQuery
     * @return 
     */
    public List<Object> leerNamed(String nomNamedQuery) {
        super.openSession();
        List<Object> lResults = null;
        
        try {
            super.transaccion = super.sesion.beginTransaction();            
            lResults = super.sesion.getNamedQuery(nomNamedQuery).list();
        }
        catch (Exception e) {
            this.mensajeError = e.getMessage();
            System.err.println("[ERROR - es.albarregas.dao.DAOGenerico.java] Fallo en método leerNamed()\n" + e);
        }
        finally {
            super.closeSession();
        }
        
        if (lResults != null) {
            System.out.printf("[INFO - ACCESSO BD] .leerNamed(%s) - Obtenidos: %d registros de la BD.\n",nomNamedQuery,lResults.size());
        }
                
        return lResults;
    }    
    
    
    /**
     * Método para comprobar si hay un usuario en la BD con el email y la clave que se pasan como parámetro
     * @param email
     * @param clave
     * @return 
     */
    public Usuario login(String email, byte[] clave) {
        
        super.openSession();
        Usuario u = null;
        
        try {
            super.transaccion = super.sesion.beginTransaction();
            Query query = super.sesion.getNamedQuery("login")
                    .setParameter("email", email).setParameter("clave", clave);
            u = (Usuario) query.uniqueResult();
        }
        catch (Exception e) {
            this.mensajeError = e.getMessage();
            System.err.println("[ERROR - es.albarregas.dao.DAOGenerico.java] Fallo en método login()\n" + e);
        }
        finally {
            super.closeSession();
        }
        
        if (u != null) {
            System.out.println("[INFO - ACCESSO BD] .login() - Obtenido usuario de la BD.");
        }
                
        return u;
    }    


    
    public List<Object> leerCampo(String clase, String campo) {
        super.openSession();
        List<Object> lResults = null;
        
        try {
            super.transaccion = super.sesion.beginTransaction();
            lResults = super.sesion.createQuery("select " + campo + " from " + clase).setReadOnly(true).list();
        }
        catch (Exception e) {
            this.mensajeError = e.getMessage();
            System.err.println("[ERROR - es.albarregas.dao.DAOGenerico.java] Fallo en método leer()\n" + e);
        }
        finally {
            super.closeSession();
        }
        
        if (lResults != null) {
            System.out.printf("[INFO - ACCESSO BD] .leerCampo(2 param.) - Obtenidos: %d registros de la BD.\n",lResults.size());
        }
                
        return lResults;
    }    
    
//    public List<Object> leerCampoNamed(String nomNamedQuery) {
//        super.openSession();
//        List<Object> lResults = null;
//        
//        try {
//            super.transaccion = super.sesion.beginTransaction();            
//            lResults = super.sesion.getNamedQuery(nomNamedQuery).list();
//        }
//        catch (Exception e) {
//            this.mensajeError = e.getMessage();
//            System.err.println("[ERROR - es.albarregas.dao.DAOGenerico.java] Fallo en método leerNamed()\n" + e);
//        }
//        finally {
//            super.closeSession();
//        }
//        
//        return lResults;
//    }    
    
    
    /**
     * Eliminar una fila/registro de la BD
     * @param objeto
     * @return 
     */
    public boolean eliminar(Object objeto) {
        boolean ok = false;
        
        super.openSession();
        
        try {
            super.openSession();
            super.transaccion = super.sesion.beginTransaction();
            super.sesion.delete(objeto);
            super.transaccion.commit();
            
            ok = true;
        }
        catch (Exception e) {
            super.transaccion.rollback();
            
            this.mensajeError = e.getMessage();
            System.err.println("[ERROR - es.albarregas.dao.DAOGenerico.java] Fallo en método eliminar()\n" + e);
            
            ok = false;
        }
        finally {
            super.closeSession();
        }
        
        System.out.println("[INFO - ACCESSO BD] .eliminar " + objeto.getClass());
        return ok;
    }
    
    
    /**
     * Obtiene el ultimo error que se ha producido en nuestros acceso a la BD
     * @param eliminar si se pone a true elimina ese mensaje
     * @return 
     */
    public String utimoError(boolean eliminar) {
        String mensaje = this.mensajeError;
        
        if(eliminar) {
            this.mensajeError = "";
        }
        
        return mensaje;
    }
}