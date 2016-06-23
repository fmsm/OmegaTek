
package es.albarregas.ManagedBeans;

import es.albarregas.dao.DAOGenerico;
import es.albarregas.modelo.Cliente;
import es.albarregas.modelo.EstadosPedido;
import es.albarregas.modelo.Factura;
import es.albarregas.modelo.LineasPedido;
import es.albarregas.modelo.Pedido;
import es.albarregas.modelo.Producto;
import es.albarregas.modelo.TablaGeneral;
import es.albarregas.utils.UtilSesion;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Francisco
 */
@Named
@SessionScoped
public class CestaMB implements java.io.Serializable {
    
    private Pedido pedidoActual;
    private List<LineasPedido> cesta;    
    private final float ivaPct;   //valor del tanto por ciento de iva que hay que tener en cuenta para el calculo del importe total

    public List<LineasPedido> getCesta() {
        return cesta;
    }

    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    public void setPedidoActual(Pedido pedidoActual) {
        this.pedidoActual = pedidoActual;
    }
    
    //constructor
    public CestaMB() {                        
        //obtener el iva (el %)  de la BD
        DAOGenerico oDAO = new DAOGenerico();
        TablaGeneral tablaG = (TablaGeneral) oDAO.leer("", "TablaGeneral", "", -1, -1).iterator().next();
        ivaPct = tablaG.getIva();        
        System.out.println("[INFO - MB] [CestaMB] Constructor ejecutado");        
    }//constructor
    
    
    /**
     * Ver si hay atributo de sesion indicando usuario logeado y si lo hay, ver si hay algun pedido que este marcado como 'n' (no tramitado/realizado)
     * y si lo hay, cargarlo en 'pedidoActual', asi como cargar 'cesta' con los objetos LineasPedido de 'pedidoActual'.
     */       
    public void recuperarPedidoUsuario() {
        
        //ver si hay atributo de sesion indicando usuario logeado
        if (UtilSesion.getUserID() != null) {
            //si --> obtener de la BD pedido con estado 'n' si lo hay
            DAOGenerico oDAO = new DAOGenerico();
                        
            //obtener idUsuario de la sesion
            int temporal = UtilSesion.getUserID();
            //obtener (si existe) pedido con estado 'n' (no tramitado o realizado), del usuario que esta logeado
            Pedido ped = (Pedido) oDAO.leerPedidoN(temporal);
            
            //si hay pedido, cargarlo en 'pedidoActual', asi como cargar 'cesta' con los objetos LineasPedido de 'pedidoActual'.
            if (ped != null) {
                pedidoActual = ped;
                
                cesta = new ArrayList();
                Iterator<LineasPedido> it = ped.getLineasPedido().iterator();
                while (it.hasNext()) {
                    cesta.add(it.next() );
                }//while
            }//if ped not null
            
        }//if .getUserID() not null
        
        System.out.println("[INFO - MB] [CestaMB] .recuperarPedidoUsuario: " + UtilSesion.getUserID() );
        
    }//metodo
    
    
    /**
     * Busqueda en la BD de los datos necesarios del producto sobre el que se acaba de pulsar el boton Comprar.
     * Y gestión de los objetos 'pedidoActual' y 'cesta', para los diferentes casos posibles
     * 
     * Caso: Usuario Logeado 
     * -> Cada vez que se pulsa sobre comprar:
     * -> - Comprobar si ArrayList 'cesta' es null:
     * ->       si --> crear nuevo objeto cesta
     * ->       no --> no hacer nada, seguir proceso
     * -> - Comprobar si objeto 'pedidoActual' es null:
     * ->       si --> crear nuevo objeto 'pedidoActual'
     * ->       no --> no hacer nada, seguir proceso
     * -> - Comprobar si hay alguna linea de pedido para el producto sobre el que se ha pulsado comprar:
     * ->       si --> no crear lp nueva, añadir 1 a la cantidad de la lp existente para ese producto en el ArrayList cesta
     * ->       no --> crear lina pedido nueva
     * ->          --> Asignar linea pedido creada al pedido que se creo en el paso anterior o que ya existia anteriormente (MB de sesion)
     * -> - Establecer el atributo pedidoActual.lineasPedido, con el ArrayLista 'cesta', que ahora contendra los datos actualizados
     * -> - Guardar/actualizar pedido en BD
     * 
     * Caso: Usuario Anonimo
     * -> Parecido, pero en el método 'inicializarPedido', no se tendra acceso a los sgtes datos para introducirlos en el objeto pedido:
     *      Cliente cliente y Direccion direccion
     * -> Y tampoco se podra guardar el objeto Pedido pedidoActual en la BD
     * 
     * @param id
     * @return 
     */     
    public String gestionPedidoCesta(int id) {
        
        //variable para identificar el tipo de usuario, y actuar en consecuencia en el resto del proceso
        //(tipoPed =  1)->usuario logeado
        //(tipoPed = -1)->usuario anónimo
        int tipoPed = (UtilSesion.getUserID() != null) ? 1 : -1;
                
        DAOGenerico oDAO = new DAOGenerico();
        
        //obtener info del producto
        String clausulaWhere = String.format("where idProducto = %s", id);
        Producto prod = (Producto) oDAO.leer("", "Producto", clausulaWhere, -1, -1).iterator().next();
        
        //Cada vez que se pulsa sobre comprar
        
        //Comprobar si ArrayList 'cesta' es null:                
        if (cesta == null) {
            //si --> crear 
            cesta = new ArrayList();
        }//if
        
        //Comprobar si objeto 'pedidoActual' es null:
        if (pedidoActual == null) {
            //si --> crear
            pedidoActual = new Pedido();
            inicializarPedido(tipoPed);
        }//if
        
        //Buscar si para ese producto, ya habia un objeto LineasPedido en el ArrayList 'cesta' (para no añadir dos LineasPedido con el mismo producto)
        boolean prodExistente = false;
        Iterator<LineasPedido> it = cesta.iterator();
        while (it.hasNext()) {
            LineasPedido temporal = it.next();
            if ( temporal.getProducto().getDenominacion().equals(prod.getDenominacion()) ) {
                //si ya existia una LineasPedido para ese producto en la cesta, incremento la cantidad en 1
                
                //Pero siempre que haya stock suficiente para realizar esta operacion, es decir:
                //controlar el caso en que si un usuario tiene ya un producto en su cesta, y pulsa sobre seguir comprando. Y despues, pulsa de nuevo 
                //sobre comprar otra unidad (en la vista 'producto.xhtml'), de un producto que ese mismo usuario tenia previamente en el carrito. 
                //Controlar que al hacer click sobre comprar una nueva unidad, sumando la cantidad que tiene en el carrito, mas esta nueva unidad que se dispone a 
                //comprar, haya suficiente stock de ese producto para poder atender ese posible pedido.                
                if ( temporal.getCantidad() + 1 >  prod.getStock() ) {
                    //no hay cantidad suficiente, informar al usuario de que no hay cantidad suficiente para atender su peticion de comprar el producto
                    FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Stock temporalmente agotado.", null);
                    FacesContext.getCurrentInstance().addMessage(null, mensaje);
                    return null;    //y me quedo en la misma vista
                } else {
                    //stock suficiente
                    temporal.setCantidad( (byte) (temporal.getCantidad() + 1) );
                    prodExistente = true;
                    System.out.printf("[Añadida unidad a un producto (id=%d) ya existente en la cesta]\n", prod.getIdProducto());
                    break;  //no hace falta seguir iterando
                }//if..else                
                
            }//if
        }//while        
        
        //si en el bucle anterior, no encontramos ninguna LineasPedido con el objeto Producto que se buscaba, creamos una nueva LineasPedido en 'cesta'
        if (!prodExistente) {            
            LineasPedido lp = new LineasPedido();
            lp.setCantidad( (byte) 1);
            //lp.setNumeroLinea(0);
            lp.setPedido(pedidoActual);            
            lp.setPrecioUnitario( prod.getPrecioUnitario() );
            lp.setProducto( prod );
            
            //Asignar linea pedido creada al pedidoque se creo en el paso anterior o que ya existia anteriormente (MB de sesion)
            cesta.add(lp);            
            
            System.out.printf("[Producto (id=%d) añadido a la cesta]\n", prod.getIdProducto());            
        }//if (!prodExistente)
        
        //Establecer el atributo pedidoActual.lineasPedido, con el ArrayLista 'cesta', que ahora contendra los datos actualizados
        Set cestaTemp = new HashSet<>(cesta);
        pedidoActual.setLineasPedido( cestaTemp );
                
        //TODOS LOS PRECIOS DE LOS PRODUCTOS EN LA BASE DE DATOS TIENEN EL IVA YA INCLUIDO ASI QUE PARA CALCULAR baseImponible E iva LO HAGO A 
        //PARTIR DE LO QUE CONSIGO EN calcTotal()
        
        //actualizar los valores de baseImponible e Iva, acorde con el producto que se acaba de añadir a la cesta 
        pedidoActual.setBaseImponible( calcTotal() - ( (calcTotal() * ivaPct) / 100 ) );     
        pedidoActual.setIva( (calcTotal() * ivaPct) / 100);        
                
        //Actualizar/Guardar pedido en BD (si tipoPed es 1 = usuario logeado)
        if (tipoPed == 1) {
            oDAO.crearOactualizar(pedidoActual);
        }
        
        //redirigir a la vista 'cesta.xhtml' donde se mostrarán los datos del pedido actual
        return "cesta?faces-redirect=true";  
    };
    
    
    /**
     * Método auxiliar del método gestionPedidoCesta, para inicializar los datos de un pedido nuevo, exceptuando del atributo .direccion que se 
     * rellenara durante el proceso de checkout, al que se accede tras pulsar tramitar en la vista de la cesta de la compra
     * @param tipoPed
     */
    public void inicializarPedido(int tipoPed) {

        EstadosPedido estadoN;
        TablaGeneral tablaG;
        String clausulaWhere;        
        int temporal = 0;
        Cliente cliente = null;
        
        //sacar el usuarioId de la sesion
        if (tipoPed == 1) {
            temporal = UtilSesion.getUserID();        
        }
        
        //cosas que hay que pillar de la base de datos para poder crear un pedido: estadospedido, cliente, direccion, gastosEnvio, iva
        //aunque depende del tipo de usuario, logeado o anonimo
        DAOGenerico oDAO = new DAOGenerico();
        
        //obtener estado pedido con idestado = n (.iterator().next() porque como maximo solo va a haber uno)
        estadoN = (EstadosPedido) oDAO.leer("", "EstadosPedido", "where idEstado = 'n'", -1, -1).iterator().next();
        //obtener los gastos de envio de la BD
        tablaG = (TablaGeneral) oDAO.leer("", "TablaGeneral", "", -1, -1).iterator().next();

        
        if (tipoPed == 1) {
            //faltaria comprobar fallos, dificil que haya pero por si acaso        
            clausulaWhere = String.format("where IdCliente = '%d'",temporal);        
            cliente = (Cliente) oDAO.leer("", "Cliente", clausulaWhere, -1, -1).iterator().next();  
            
//            //CUIDADO AL ASIGNAR DIRECCION, PUES SI LO HAGO SOBRE UN USUARIO DEL QUE NO TENGO DIRECCION PETA
//            //ahora mismo tengo solo una direccion por cliente, habria que dar opcion de seleccionar direccion al usuario, ya que puede tener varias            
//            List<Object> listTemp = oDAO.leer("", "Direccion", clausulaWhere, -1, -1);
//            Iterator it = listTemp.iterator();
//            dirsCliente = new ArrayList();
//            while (it.hasNext()) {
//                dirsCliente.add( (Direccion) it.next() );
//            }//while
            
        }//if tipoPed
        
        //ped.setIdPedido(0);
        pedidoActual.setFecha(new Date());
        pedidoActual.setEstado(estadoN);
        pedidoActual.setBaseImponible(0);
        pedidoActual.setIva(0);
        pedidoActual.setDescuento(0);
        pedidoActual.setGastosEnvio(tablaG.getGastosEnvio());

        
        if (tipoPed == 1) {
            pedidoActual.setCliente(cliente);
              //CUIDADO AL ASIGNAR DIRECCION, PUES SI LO HAGO SOBRE UN USUARIO DEL QUE NO TENGO DIRECCION PETA
//            //añadir direccion, si existe alguna direccion para ese cliente, si no poner a null
//            if (dirsCliente != null && dirsCliente.size() > 0) {
//                //ahora mismo tengo solo una direccion por cliente, habria que dar opcion de seleccionar direccion al usuario, ya que puede tener varias                            
//                pedidoActual.setDireccion( dirsCliente.get(0));
//            }//if            
        }//if
        
    }//método inicializarPedido
    

    /**
     * Aqui implementar la lógica que conlleva finalizar el pedido, cuando el usuario pulsa sobre "Finalizar Pedido" en el paso4 del proceso de "checkout".
     * 
     * - Cambiar pedidoActual.estado a realizado y actualizar el pedido en la DB
     * - Crear un objeto Factura y guardarlo en la DB
     * - En la tabla Productos, restar al stock actual de cada uno de los productos que se ha vendido, la cantidad vendida de ese producto en el pedido que
     *   se esta procesando
     * - Borrar/poner a null los atributos de este bean, pedidoActual y cesta
     * - Mostrar un mensaje o dialog o algo, mostrando que el pedido se ha compleatdo con exito
     * - Y luego redirigir a index
     */
    public void finalizar() {
        
        DAOGenerico oDAO = new DAOGenerico();
        
        //obtener estado pedido con idestado = r (.iterator().next() porque como maximo solo va a haber uno)
        EstadosPedido estadoR = (EstadosPedido) oDAO.leer("", "EstadosPedido", "where idEstado = 'r'", -1, -1).iterator().next();
        
        //actualizar pedido
        pedidoActual.setEstado(estadoR);
        oDAO.crearOactualizar(pedidoActual);
        
        //crear y guardar factura
        Factura factura = new Factura(pedidoActual);
        oDAO.crearOactualizar(factura);
        
        //actualizar stock productos
        //recorrer pedidoActual.lineasPedidos
        Iterator<LineasPedido> it = pedidoActual.getLineasPedido().iterator();
        while (it.hasNext()) {
            
            LineasPedido lpTemp = it.next();
            //idProdTmp->id del producto al que vamos a restarles unidades
            //int idProdTmp = lpTemp.getProducto().getIdProducto();
            
            //cantidadTmp->numero de unidades que se han vendido y vamos a restar
            byte cantidadTmp = lpTemp.getCantidad();
            
            //producto temporal para realizar la actualización
            Producto prodTemp = lpTemp.getProducto();
            
            //actualizar la cantidad en el producto temporal
            prodTemp.setStock( (short) (prodTemp.getStock() - cantidadTmp) );
            
            //guardar producto con el stock actualizado en la BD
            oDAO.crearOactualizar(prodTemp);
           
        }//while
        
        //poner atributos a null
        pedidoActual = null;
        cesta = null;
        
        //mostrar mensaje confirmación        
        RequestContext.getCurrentInstance().execute("PF('dlgCheckout').show();");
        
        System.out.println("[INFO - MB] [CestaMB] .finalizar");
        
    }//finalizar
    
        
    /**
     * Añadir 1 unidad de un producto determinado. 
     * Por seguridad, aunque ya esta controlado en la vista, controlo que no se produzca el caso de que se añada una unidad de un producto del que no
     * hay mas unidades disponibles en stock (esto se hace en la 2da condición del 'if..else' del principio del método.
     * Caso: Usuario Logeado -> Actualizar pedido en BD
     * 
     * @param linea 
     */
    public void plus(LineasPedido linea) {
        
        String textoMensaje ="";
        if (cesta.contains(linea) && linea.getProducto().getStock() > 0) {
            
            //Añadir 1 a cantidad
            cesta.get( cesta.indexOf(linea) ).setCantidad( (byte) (linea.getCantidad() + 1) );
                        
            //Establecer el atributo pedidoActual.lineasPedido, con el ArrayLista 'cesta', que ahora contendra los datos actualizados
            Set cestaTemp = new HashSet<>(cesta);
            pedidoActual.setLineasPedido( cestaTemp );
        
            //actualizar baseImponible e Iva
            pedidoActual.setBaseImponible( calcTotal() - ( (calcTotal() * ivaPct) / 100 ) );     
            pedidoActual.setIva( (calcTotal() * ivaPct) / 100);                                    
            
            //Actualizar/Guardar pedido en BD, pero solo si es un usuario logeado
            if (UtilSesion.getUserID() != null) {            
                DAOGenerico oDAO = new DAOGenerico();
                oDAO.crearOactualizar(pedidoActual);
            }
            textoMensaje = cesta.get( cesta.indexOf(linea) ).getProducto().getDenominacion();
        } else {
            textoMensaje = "Se produjo un error inesperrado, por favor, intente de nuevo";
        }//if
        
        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Añadida 1 unidad", textoMensaje);
        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                
    }//metodo
    
    
    /**
     * Eliminar 1 unidad de un producto determinado.
     * Caso: Usuario Logeado -> Actualizar pedido en BD
     * @param linea 
     */
    public void minus(LineasPedido linea) {
        
        String textoMensaje ="";
        if (cesta.contains(linea)) {
            
            //Restar 1 a cantidad (en la vista cuando la cantidad lleega a 1, el boton para eliminar se pone como disabled, para que no se pueda acceder mas a este método)
            cesta.get( cesta.indexOf(linea) ).setCantidad( (byte) (linea.getCantidad() - 1) );

            //Establecer el atributo pedidoActual.lineasPedido, con el ArrayLista 'cesta', que ahora contendra los datos actualizados
            Set cestaTemp = new HashSet<>(cesta);
            pedidoActual.setLineasPedido( cestaTemp );
        
            //actualizar baseImponible e Iva
            pedidoActual.setBaseImponible( calcTotal() - ( (calcTotal() * ivaPct) / 100 ) );     
            pedidoActual.setIva( (calcTotal() * ivaPct) / 100);                                                
            
            //Actualizar/Guardar pedido en BD, pero solo si es un usuario logeado
            if (UtilSesion.getUserID() != null) {                        
                DAOGenerico oDAO = new DAOGenerico();
                oDAO.crearOactualizar(pedidoActual);
            }
            
            textoMensaje = cesta.get( cesta.indexOf(linea) ).getProducto().getDenominacion();
        } else {
            textoMensaje = "Se produjo un error inesperrado, por favor, intente de nuevo";
        }//if
        
        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminada 1 unidad", textoMensaje);
        FacesContext.getCurrentInstance().addMessage(null, mensaje);
                        
    }//metodo

    
    /**
     * Eliminar el producto seleccionado del objeto 'cesta' (su LineasPedido correspondiente).
     * Y si corresponde (la cesta se queda vacia), borrar pedido (de la BD) tambien.
     * Caso: Usuario Logeado -> Eliminar de la tabla de la BD la fila 'lineaspedidos', correspondiente al objeto LineasPedidos del ArrayList cesta, que se quiere elminar
     * @param linea 
     */
    public void remove(LineasPedido linea) {
        
        String textoMensaje ="";
        if (cesta.contains(linea)) {
            
            DAOGenerico oDAO = new DAOGenerico();
            
            textoMensaje = cesta.get( cesta.indexOf(linea) ).getProducto().getDenominacion();
                                                
            //si al elimiar esta linea, la cesta va a pasa a estar vacia, hay que eliminar de la base de datos el pedido correspondiente, y al mismo
            //tiempo se eliminara la fila restante de la tabla 'lineaspedidos'
            //Tambien hay que poner 'pedidoActual' y 'cesta' a null
            if (cesta.size() == 1) {
                if (UtilSesion.getUserID() != null) {
                    oDAO.eliminar(pedidoActual);
                }//if
                pedidoActual = null;
                cesta = null;
            } else {
                
                //si al eliminar la linea de cesta, aun no esta vacia la cesta, hay que eliminar de la tabla de la BD la fila 'lineaspedidos' 
                //correspondiente a 'linea'
                if (UtilSesion.getUserID() != null) {                            
                    oDAO.eliminar(linea);
                }//if                
                
                //eliminar del atributo 'cesta', el objeto LineasPedido correspondiente al producto que se ha elimiado
                cesta.remove(linea);
                
                //Establecer el atributo pedidoActual.lineasPedido, con el ArrayLista 'cesta', que ahora contendra los datos actualizados
                Set cestaTemp = new HashSet<>(cesta);
                pedidoActual.setLineasPedido( cestaTemp );

                //actualizar baseImponible e Iva
                pedidoActual.setBaseImponible( calcTotal() - ( (calcTotal() * ivaPct) / 100 ) );     
                pedidoActual.setIva( (calcTotal() * ivaPct) / 100);                                                    
                
                //Actualizar/Guardar pedido en BD, pero solo si es un usuario logeado
                if (UtilSesion.getUserID() != null) {            
                    oDAO = new DAOGenerico();
                    oDAO.crearOactualizar(pedidoActual);
                }                
                
            }//if..else

        FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto eliminado de la cesta", textoMensaje);
        FacesContext.getCurrentInstance().addMessage(null, mensaje);        
        
        if (cesta == null) {
            FacesMessage mensaje2 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cesta Vacia", " ");
            FacesContext.getCurrentInstance().addMessage(null, mensaje2);            
        }
            
        }//if
                                
    }//metodo
    
        
    /**
     * Calcular importe total de los productos contenidos en la cesta, para mostrarlo en la vista 'cesta.xhtml'
     * @return 
     */
    public float calcTotal() {
        //recorrer pedidoActual.lineasPedido e ir sumando (precioUnitario * cantidad)
        float suma = 0;
        Iterator<LineasPedido> it = pedidoActual.getLineasPedido().iterator();
        
        while (it.hasNext()) {
            LineasPedido temporal = it.next();
            suma = suma + temporal.getCantidad() * temporal.getPrecioUnitario();
        }
        
        return suma;
        
    }//calcTotal
    
    
    /**
     * Calcular el numero total de productos contenidos en la cesta, para mostrarlo en la vista '/comun/cabecera.xhtml'
     * @return 
     */        
    public int calcNumProdCesta() {
        
        int suma = 0;
        Iterator<LineasPedido> it = pedidoActual.getLineasPedido().iterator();
        
        while (it.hasNext()) {
            LineasPedido temporal = it.next();
            suma = suma + temporal.getCantidad();
        }        
        
        return suma;
    }//calcNumProdCesta
    
    
//    /**
//     * Cuando pulsamos en el boton comprar en la vista 'producto.xhtml', tenemos que comprobar, no solo que haya unidades en stock para ese producto.
//     * Eso ya se controla en dicha vista, deshabilitando el boton si se produce ese caso.
//     * Si no tambien, controlar el caso en que si un usuario tiene ya un producto en su cesta, y pulsa sobre seguir comprando. Y despues, pulsa de nuevo 
//     * sobre comprar otra unidad (en la vista 'producto.xhtml'), de un producto que ese mismo usuario tenia previamente en el carrito. 
//     * Controlar que al hacer click sobre comprar una nueva unidad, sumando la cantidad que tiene en el carrito, mas esta nueva unidad que se dispone a 
//     * comprar, haya suficiente stock de ese producto para poder atender ese posible pedido.
//     * @param prod
//     * @return 
//     */
//    public boolean checkStock(Producto prod) {
//                        
//        for (LineasPedido lpTemp : pedidoActual.getLineasPedido()) {
//            Producto prodTemp = lpTemp.getProducto();
//            
//            //se trada del mismo producto??
//            if (prodTemp.getIdProducto() == prod.getIdProducto()) {
//                //cantidad en pedidoActual de ese producto + 1, es mayor que el stock que hay de ese producto
//                if ( lpTemp.getCantidad() + 1 >  prodTemp.getStock() ) {
//                    return true;
//                } else {
//                    //no hay cantidad suficiente, informar al usuario de que no hay cantidad suficiente para atender su peticion de comprar el producto
//                    FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Stock temporalmente agotado.", null);
//                    FacesContext.getCurrentInstance().addMessage(null, mensaje);                    
//                }//if..else
//            }//if
//        }//for
//        
//        return false;
//    }//metodo
    
    
    /**
     * ActionListener que se ejecutara cuando se pulse sobre el p:menuitem Logout, y se encargara de vaciar los atributos pedidoActual y cesta
     * y en consecuencia hacer que el icono del carrito/cesta en la cabecera se actualize
     */
//    public void logoutCesta() {
//        
//        pedidoActual = null;
//        cesta = null;
//
//        System.out.println("[INFO - MB] [CestaMB] .logoutCesta");        
//    }

    
}//CLASS
