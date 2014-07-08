package com.consumidor;

public class Proxy {
	private String url;
	   private Integer puerto;
	   private String usuario;
	   private String clave;
	   private String wsProduccion;
	   private String wsPruebas;
	 
	   public Proxy()
	   {
	   }
	 
	   public Proxy(String url, Integer puerto, String usuario, String clave, String wsProduccion, String wsPruebas)
	   {
	     this.url = url;
	     this.puerto = puerto;
	     this.usuario = usuario;
	     this.clave = clave;
	     this.wsProduccion = wsProduccion;
	     this.wsPruebas = wsPruebas;
	   }
	 
	   public String getUrl()
	   {
	     return this.url;
	   }
	 
	   public void setUrl(String url)
	   {
	     this.url = url;
	   }
	 
	   public Integer getPuerto()
	   {
	     return this.puerto;
	   }
	 
	   public void setPuerto(Integer puerto)
	   {
	     this.puerto = puerto;
	   }
	 
	   public String getUsuario()
	   {
	     return this.usuario;
	   }
	 
	   public void setUsuario(String usuario)
	   {
	     this.usuario = usuario;
	   }
	 
	   public String getClave()
	   {
	     return this.clave;
	   }
	 
	   public void setClave(String clave)
	   {
	     this.clave = clave;
	   }
	 
	   public String getWsProduccion()
	   {
	     return this.wsProduccion;
	   }
	 
	   public void setWsProduccion(String wsProduccion)
	   {
	     this.wsProduccion = wsProduccion;
	   }
	 
	   public String getWsPruebas()
	   {
	     return this.wsPruebas;
	   }
	 
	   public void setWsPruebas(String wsPruebas)
	   {
	     this.wsPruebas = wsPruebas;
	   }
}
