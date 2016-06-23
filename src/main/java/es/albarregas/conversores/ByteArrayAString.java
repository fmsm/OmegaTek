
package es.albarregas.conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;

/**
 * Custom converter para que el password que viene en byte[] de la base de datos se muestre como un string
 * @author Francisco
 */
@FacesConverter("byteArrayAString")
public class ByteArrayAString implements javax.faces.convert.Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        
        //comprobación de que no sea nulo o este vacio
        if (value == null || ((byte[]) value).length == 0) {
            return null;
        } else {
            //pasarlo de byte[] a string
            return new String( (byte[]) value );            
        }//if..else
         
    }//metodo
        
}//CLASS
