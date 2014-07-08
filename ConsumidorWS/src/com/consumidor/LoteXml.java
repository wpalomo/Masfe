package com.consumidor;

import java.util.ArrayList;
import java.util.List;



public class LoteXml {
	private String version;
	   private String claveAcceso;
	   private String ruc;
	   private List<ComprobanteXml> comprobantes;
	 
	   @SuppressWarnings({ "rawtypes", "unchecked" })
			   public LoteXml()
	   {
	     this.comprobantes = new ArrayList();
	   }
	 
	   public String getClaveAcceso() {
	     return this.claveAcceso;
	   }
	 
	   public void setClaveAcceso(String claveAcceso) {
	     this.claveAcceso = claveAcceso;
	   }
	 
	   public List<ComprobanteXml> getComprobantes() {
	     return this.comprobantes;
	   }
	 
	   public void setComprobantes(List<ComprobanteXml> comprobantes) {
	     this.comprobantes = comprobantes;
	   }
	 
	   public String getRuc() {
	     return this.ruc;
	   }
	 
	   public void setRuc(String ruc) {
	     this.ruc = ruc;
	   }
	 
	   public String getVersion() {
	     return this.version;
	   }
	 
	   public void setVersion(String version) {
	     this.version = version;
	   }
}
