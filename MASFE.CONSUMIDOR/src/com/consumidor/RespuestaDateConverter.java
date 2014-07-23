package com.consumidor;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class RespuestaDateConverter  implements Converter
 {
   public boolean canConvert(Class clazz)
   {
     return clazz.equals(XMLGregorianCalendarImpl.class);
   }
 
   public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
     XMLGregorianCalendarImpl i = (XMLGregorianCalendarImpl)o;
     writer.setValue(Constantes.dateTimeFormat.format(i.toGregorianCalendar().getTime()));
   }
 
   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc)
   {
     Date date = null;
     try {
       date = Constantes.dateTimeFormat.parse(reader.getValue());
     } catch (ParseException ex) {
       Logger.getLogger(RespuestaDateConverter.class.getName()).log(Level.SEVERE, null, ex);
     }
     GregorianCalendar cal = new GregorianCalendar();
     cal.setTime(date);
     XMLGregorianCalendarImpl item = new XMLGregorianCalendarImpl(cal);
 
     return item;
   }
}
