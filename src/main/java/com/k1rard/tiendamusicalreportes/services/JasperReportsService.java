/**
 * 
 */
package com.k1rard.tiendamusicalreportes.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author k1rard
 * Interface que contiene los metodos de logica de negocio para compilar y generar el reporte PDF
 */
public interface JasperReportsService {

	/**
	 * Metodo que pemrite compilar el archivo jasper jrxml descargado de Dropbox.
	 * @param archivoBytes {@link ByteArrayOutputStream} archivo jrxml a compilar.
	 * @param orderID {@link String} orden de pedido del cliente.
	 * @return {@link JasperPrint} archivo jasper con la informacion a generarse en PDF
	 * @throws ClassNotFoundException {@link ClassNotFoundException} exception en caso de error al encontrar el driver
	 * @throws SQLException  {@link SQLException} excepcion en caso de error al conectarse a la base de datos.
	 * @throws JRException {@link JRException} excepcion en caso de error al compilar el reporte de jasper. 
	 * @throws IOException {@link IOException} excepcion en caso de error al cerrar el flujo de datos de los archivos.
	 */
	JasperPrint compilarReporteJasper(ByteArrayOutputStream archivoBytes, String orderID) throws ClassNotFoundException, SQLException, JRException, IOException ;
}
