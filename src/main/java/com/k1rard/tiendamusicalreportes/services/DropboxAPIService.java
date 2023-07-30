/**
 * 
 */
package com.k1rard.tiendamusicalreportes.services;

import java.io.IOException;

import com.amazonaws.services.networkmanager.model.Link;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.UploadErrorException;

import jakarta.ws.rs.core.Response;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author k1rard
 * Interface que proporciona los metodos para acceder a la API de Dropbox
 */
public interface DropboxAPIService {

	/**
	 * Metodo que permite descargar el archivo compilado de jasperReport para compilar el reporte y generar el PDF.
	 * @param dbxClientV2 {@link DbxClientV2} api para conectarse a Dropbox.
	 * @param orderId {@link String} Numero de pedido de la compra de los productos.
	 * @param cliente {@link String} Nombre completo del cliente que realizo la compra.
	 * @return {@link Response} Respuesta generada por el webService
	 */
	Response descargarReporte(DbxClientV2 dbxClientV2, String orderId, String cliente);
	
	/**
	 * Metodo que permie cargar o subir el reporte PDF generado con JasperReports a Dropbox
	 * @param dbxClientV2 {@link DbxClientV2} api para conectarse a Dropbox.
	 * @param orderId {@link String} Numero de pedido de la compra de los productos.
	 * @param cliente {@link String} Nombre completo del cliente que realizo la compra.
	 * @param jasperPrint {@link JasperPrint} Archivo de jasper generado como PDF.
	 * @throws IOException {@link IOException} excepcion generada en caso de error al crear el archivo PDF en la carpeta temporal
	 * @throws JRException {@link JRException} excepcion generada en caso de error al exportar la informacion del reporte al archivo temporal.
	 * @throws DbxException {@link DbxException} excepcion generada en caso de error al realizar el proceso de carga.
	 * @throws UploadErrorException {@link UploadErrorException} excepcion generada en caso error en el momento de se esta subiendo el archivo pdf a DropBox.
	 */
	void cargarReporteToDropbox(DbxClientV2 dbxClientV2, String orderId, String cliente, JasperPrint jasperPrint) throws IOException, JRException, UploadErrorException, DbxException;
}
