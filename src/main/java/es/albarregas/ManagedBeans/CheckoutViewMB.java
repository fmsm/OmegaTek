package es.albarregas.ManagedBeans;

import es.albarregas.dao.DAOGenerico;
import es.albarregas.modelo.Cliente;
import es.albarregas.modelo.Direccion;
import es.albarregas.modelo.Pedido;
import es.albarregas.modelo.Pueblo;
import es.albarregas.utils.UtilSesion;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Francisco
 */
@Named
@ViewScoped
public class CheckoutViewMB implements java.io.Serializable {
        
    @Inject
    private CestaMB cdiCestaMB;
    
    //Indica la fase del proceso de Checkout por el que va en cada momento
    //0->ident.  1->direcc.   2->pago.     3->confirmacion.
    private int stepNum;    
    
    //Colección para almacenar las direcciones para un determinado cliente, que seran mostradas por pantalla en caso de existir; y en
    //la que se añadirán las nuevas direcciones que se creen
    private List<Direccion> dirsCliente;

    //indica el Direccion.IdDireccion de la direccion que el cliente ha seleccionado de entre las posibles
    private short idDirSeleccionada;
    
    //Indica el objeto Direccion seleccionda por el cliente de entre las existentes, y que durante el proceso se guardará en el atributo
    //CestaMB.pedidoActual del managend bean CestaMB, mediante CDI
    private Direccion dirEnvio;
    
    //atributos usados en el formulario que permite crear una direccion para el envio
    private String denominacion;
    private String direccionStr;
    private String codigoPostal;
    private String puebloStr;
    private String provinciaStr;
    private String telefonoStr;
    
    //municipio, usado en el formulario de añadir direccion
    private Pueblo pueblo = null;
            
    //atributo usado en el formulario que permite escoger la forma de pago
    private int formaPago;   //1->paypal    2->tarjeta de credito   3->transf.
    
    //private String txtDirsDisponibles;

    public String getTxtDirsDisponibles() {
        switch (dirsCliente.size()) {
            case 0:
                return "(Añade una dirección de envio)";
            case 1:
                return "(1 dirección disponible)";
            default:
                return "(" + dirsCliente.size() + " direcciones disponibles )";
        }//switch        
    }//metodo
    
    
    /**
     * Constructor. Comprueba si el usuario estaba ya logeado previamente, y en caso afirmativo pasa al punto 2 (step 1)
     */
    public CheckoutViewMB() {
        
        //this.idDirSeleccionada = 0;   //creo que no necesario
        
        //si el usuario esta logeado, pasar directamente al punto 2 (step 1 ->direccion)
        if (UtilSesion.getUserID() != null) {
            stepNum = 1;
            //si entramos directamente en la fase 2 (step 1)
            //buscar en la BD direcciones para el usuario logeado, y guardarlas en el atributo dirsCliente, para poder mostrarlas al usuario            
            obtenerDirsCliente();
        } else {
            stepNum = 0;
        }//
                
        System.out.println("[INFO - MB] [CheckoutViewMB] Constructor ejecutado");
    }//constructor
    
    
    
    /**
     * Busca en la BD las direcciones existentes (si las hay) para el usuario logeado, y las guardarl en el atributo dirsCliente, 
     * para poder mostrarlas al usuario en la vista correspondiente.
     */
    public void obtenerDirsCliente() {
            DAOGenerico oDAO = new DAOGenerico();
            String clausulaWhere = String.format("where IdCliente = '%d'", UtilSesion.getUserID());            
            
            List<Object> listTemp = oDAO.leer("", "Direccion", clausulaWhere, -1, -1);
            Iterator it = listTemp.iterator();
            dirsCliente = new ArrayList();
            while (it.hasNext()) {
                dirsCliente.add( (Direccion) it.next() );
            }//while                    
    }//metodo
    
    
    /**
     * Añade una direccion para el usuario actual a la tabla de direcciones
     */
    public void addDir() {
        
        DAOGenerico oDAO = new DAOGenerico();
        
        //crear un objeto direccion y añadir sus atributos
        Direccion dir = new Direccion();
        
        
        //obtener el cliente a partir del atributo de sesion
        String clausulaWhere = String.format("where IdCliente = %d", UtilSesion.getUserID());
        Cliente cliente = (Cliente) oDAO.leer("", "Cliente", clausulaWhere, -1, -1).iterator().next();
        dir.setCliente(cliente);
        
        dir.setNombreDireccion(this.denominacion);
        dir.setDireccion(direccionStr);
        dir.setCodigoPostal(codigoPostal.toCharArray());
        dir.setPueblo(pueblo);
        dir.setProvincia(pueblo.getProvincia());
        dir.setTelefono(telefonoStr);
        
        boolean ok = oDAO.crearOactualizar(dir);
        
        FacesMessage mensaje = (ok) ? new FacesMessage(FacesMessage.SEVERITY_INFO, "Direcci&oacute; añadida con &eacute;xito", null) 
                                    : new FacesMessage(FacesMessage.SEVERITY_ERROR, "Se produjo un error", null);
        
        FacesContext.getCurrentInstance().addMessage(null, mensaje);
        //actualizar dirsCliente, para que se reflejen los cambios (o sea la direccion que acabamos de añadir)
        obtenerDirsCliente();
        
        if (ok) {
            pueblo = null;
            resetDir();
        }
        
    }//addDir
    
    
    /**
     * Mediante este metodo se le asigna un valor a la variable 'dirEnvio'; a traves del valor que se ha introducido en el atributo 'idDirSeleccionada'.
     * Se ha obtenido el atributo 'idDirSeleccionado' a partir del select que permite escoger entre las diversas direcciones que ese usuario tiene 
     * guardadas.
     * @param id 
     */
    public void selDir(short id) {
        
        this.idDirSeleccionada = id;
        
        //para averiguar el objeto direccion que corresponde a idDirSeleccionada, hay que recorrer dirsCliente hasta encontrar una
        //una coincidencia entre idDirSeleccionda y dirsCliente.idDireccion
        Iterator<Direccion> it = dirsCliente.iterator();
        while (it.hasNext()) {
            Direccion dirTemp = it.next();
            if (dirTemp.getIdDireccion() == this.idDirSeleccionada) {
                //esta es la direccion que ha seleccionado el ususario como direccion de envio
                dirEnvio = dirTemp;                
            }//if
        }//while
                
        String temporal = String.format("Direccion '%s' seleccionada.<br/>Pulse en continuar.", dirEnvio.getNombreDireccion());
        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, temporal, null);
        FacesContext.getCurrentInstance().addMessage(null, mensaje);                
    }//selDir
    
    
    /**
     * Resetea los valores de los campos del formulario añadir direccion
     */
    public void resetDir() {
        denominacion = "";
        direccionStr = "";
        codigoPostal = "";
        puebloStr = "";
        provinciaStr = "";
        telefonoStr = "";                
    }
    
    
    /**
     * LLega este método cuando el valor del componente cambia, al pulsar enter o cuando el componente pierde el foco.
     * Nos llega una cadena.
     */
    public void buscarCP() {
                
        //System.out.println(codigoPostal);
        
        //comprobar si la cadena tiene 5 caracteres (que debido al pe:keyfilter) solo van a poder ser numeros
        if (codigoPostal.length() == 5) {
            
            //si --> buscar ese cp en la bd y obtener localidad y provincia (busco solo la 1a coincidencia)
            DAOGenerico oDAO = new DAOGenerico();
            String clausulaWhere = String.format("where CodigoPostal = '%s'",codigoPostal);
            List<Object> temporal = oDAO.leer("", "Pueblo", clausulaWhere, 0, 1);

            Iterator it = temporal.iterator();
            while (it.hasNext()) {
                pueblo = (Pueblo) it.next();
            }//while

            if (pueblo != null) {
                puebloStr = pueblo.getNombre();
                provinciaStr = pueblo.getProvincia().getNombre();
            } else {
                puebloStr = "";
                provinciaStr = "";
                codigoPostal = "";
                FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "No se encontraron localidades para el c&oacute;digo postal introducido", null);
                FacesContext.getCurrentInstance().addMessage(null, mensaje);            
            }//if (pueblo!=null) else
            
        } else {
            //no --> solamente limpiar los inputs de provincia y pueblo, para cubrir el caso de que ya se hubiera producido una busqueda exitosa antes
            //y ahora se haya vuelto a buscar y no se encuentra nada
            puebloStr = "";
            provinciaStr = "";
            FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_WARN, "El c&oacute;digo postal debe tener 5 dígitos", null);
            FacesContext.getCurrentInstance().addMessage(null, mensaje);            
        }// if (cadena tiene 5 numeros)
       
       
    }//buscarCP
    
    
    /**
     * Avanza a traves de las diferentes fases del proceso de Checkout.
     * Se encarga de comprobar si estamos en una situacion de compra anonima o de compra por parte de un usuario previamente identificado; y rellena
     * los datos pertinentes sobre la Direccion y el Cliente en caso necesario.
     * 0->ident.  1->direcc.   2->pago.     3->confirmacion.
     * @param step integet, indicando la fase a la que se quiere pasar
     */
    public void continuar(int step) {
        
        DAOGenerico oDAO = new DAOGenerico();
        Pedido temporalP = null;
        
        stepNum = step;
        System.out.println("continuar: " + stepNum);
        
        //si pasamos de la fase 0 a la 1,hay que:
        // - en CestaMB.pedidoActual (en el bean de session CestaMB) hay que poner como Cliente para el pedido actual, al usuario que se acaba de logear o dar de alta
        // - ¿guardar/actualizar 'pedidoActual' en la BD? (se podria hacer tb al final)
        // - obtener las direcciones del cliente, para mostrarlas en el stepNum 2
        if (stepNum == 1) {
            //obtener las direcciones del cliente, para mostrarlas en el stepNum 2
            obtenerDirsCliente();
            
            //lo siguiente solo hay que hacerlo en los casos de compra anonima, es decir, el cliente no se ha identificado hasta
            //cuando ha sido estrictamente necesario para tramitar el pedido (proceso de checkout)
            
            //para comprobar esto, vemos si el pedido actual (que esta en el bean de sesion CestaMB) tiene ya asignado un cliente o no
            
            //obtener el pedido actual (mediante CDI) que esta disponible en el bean de sesion CestaMB
            temporalP = cdiCestaMB.getPedidoActual();
           
            if (temporalP.getCliente() == null) {
                // si --> cliente no asignado, compra anonima
                
                //obtener de la BDobjeto Cliente correspondiente al usuario que se acaba de identificar/registrar            
                String clausulaWhere = String.format( "where IdCliente = %d", UtilSesion.getUserID() );
                Cliente temporalC = (Cliente) oDAO.leer("", "Cliente", clausulaWhere, -1, -1).iterator().next();     //es seguro hacer esto pues si ha llegado hasta aqui sabemos seguro que el ususario existe

                //añadirle el Cliente que obtuvimos de la BD
                temporalP.setCliente(temporalC);

                //actualizar CestaMB.pedidoActual   (puede que so sea necesario)
                cdiCestaMB.setPedidoActual(temporalP);

                //actualizar BD (puede que no sea necesario)
                oDAO.crearOactualizar(cdiCestaMB.getPedidoActual());
                
            }//if
                                    
        }//if
        
        //si pasamos de la fase 1 a la 2,hay que:
        // - en CestaMB.pedidoActual (en el bean de session CestaMB) hay que poner como Direccion para el pedido actual, la direccion que se acaba de seleccionar
        // - ¿guardar/actualizar 'pedidoActual' en la BD? (se podria hacer tb al final)
        if (stepNum == 2) {
            
            //misma situacion que en el bloque anterior
            //obtener el pedido actual (mediante CDI) que esta disponible en el bean de sesion CestaMB
            temporalP = cdiCestaMB.getPedidoActual();
           
            if (temporalP.getDireccion() == null) {

                //obtener el pedido actual (mediante CDI) que esta disponible en el bean de sesion CestaMB
                temporalP = cdiCestaMB.getPedidoActual();            
                //añadirle la Direccion seleccionada por el cliente
                selDir(idDirSeleccionada);
                temporalP.setDireccion(dirEnvio);
                //actualizar CestaMB.pedidoActual   (puede que so sea necesario)
                cdiCestaMB.setPedidoActual(temporalP);

                //actualizar BD (puede que no sea necesario)
                oDAO.crearOactualizar(cdiCestaMB.getPedidoActual()); 
                
            }//if
            
        }//if
                
    }//continuar
        
    //<editor-fold defaultstate="collapsed" desc="getters y setters">    
    public Direccion getDirEnvio() {
        return dirEnvio;
    }
    
    public int getStepNum() {
        return stepNum;
    }

    public List<Direccion> getDirsCliente() {        
        return dirsCliente;
    }
    
    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getDireccionStr() {
        return direccionStr;
    }

    public void setDireccionStr(String direccionStr) {
        this.direccionStr = direccionStr;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getPuebloStr() {
        return puebloStr;
    }

    public void setPuebloStr(String puebloStr) {
        this.puebloStr = puebloStr;
    }

    public String getProvinciaStr() {
        return provinciaStr;
    }

    public void setProvinciaStr(String provinciaStr) {
        this.provinciaStr = provinciaStr;
    }

    public String getTelefonoStr() {
        return telefonoStr;
    }

    public void setTelefonoStr(String telefonoStr) {
        this.telefonoStr = telefonoStr;
    }
    
    public short getIdDirSeleccionada() {
        return idDirSeleccionada;
    }

    public void setIdDirSeleccionada(short idDirSeleccionada) {
        this.idDirSeleccionada = idDirSeleccionada;
    }

    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }
    //</editor-fold>    
    
}//CLASS
