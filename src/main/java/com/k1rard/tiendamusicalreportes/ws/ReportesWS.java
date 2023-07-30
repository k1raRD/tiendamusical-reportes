/**
 * 
 */
package com.k1rard.tiendamusicalreportes.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.k1rard.tiendamusicalreportes.services.DropboxAPIService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @author k1rard
 * Webservice que contendra los metodos configurados como servicios del WS.
 */
@Component
@Path("/reporteWS")
public class ReportesWS {
	
	/**
	 * Access token de la API de Dropbox
	 */
	@Value("${spring.dropbox.access.token}")
	private String ACCESS_TOKEN;
	
	/**
	 * Se inyecta el servicio de Dropbox con Spring.
	 */
	@Autowired
	private DropboxAPIService dropboxAPIServiceImpl;

	@GET
	@Path("/pruebaWS")
	public String pruebaWS() {
		return "Ingresando al webservice...";
	}
	
	@POST
	@Path("/generarReporte")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response generarReporte(@FormParam("orderId") String orderId, @FormParam("cliente") String cliente,  @FormParam("destinatario") String destinatario) {
		
		DbxRequestConfig dbxRequestConfig = DbxRequestConfig.newBuilder("dropbox/k1rard").build();
		DbxClientV2 dbxClientV2 = new DbxClientV2(dbxRequestConfig, ACCESS_TOKEN);
		
		Response response = this.dropboxAPIServiceImpl.descargarReporte(dbxClientV2, orderId, cliente);
		
		return response;
	}
	
	
	
	
	
	
	
	
}
