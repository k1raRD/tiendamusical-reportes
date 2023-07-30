/**
 * 
 */
package com.k1rard.tiendamusicalreportes.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadBuilder;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.k1rard.tiendamusicalreportes.services.DropboxAPIService;
import com.k1rard.tiendamusicalreportes.services.JasperReportsService;

import jakarta.ws.rs.core.Response;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 * @author k1rard
 * Clase que implementa los metodos para realizar la logica de negocio para descargar y cargar reportes con Dropbox.
 */
@Service
public class DropboxAPIServiceImpl implements DropboxAPIService {
	
	@Value("${spring.dropbox.directorio.reportes}")
	private String DIRECTORIO_REPORTES;
	
	@Value("${spring.dropbox.archivo.jrxml}")
	private String ARCHIVO_JASPER_JRXML;
	
	@Autowired
	private JasperReportsService jasperReportsServiceImpl;

	@Override
	public Response descargarReporte(DbxClientV2 dbxClientV2, String orderId, String cliente) {
		ByteArrayOutputStream archivoBytes = new ByteArrayOutputStream();
		String mensaje = null;
		 
		try {
			
			// Se descarga el achivo jrxml de Dropbox
			DbxDownloader<FileMetadata> downloader = dbxClientV2.files().download(DIRECTORIO_REPORTES + ARCHIVO_JASPER_JRXML);
			downloader.download(archivoBytes);
			mensaje = "Comprobante se ha generado exitosamente.";
			
			// Se envia a compilar el archivo de jasper y se llena con la informacion consultada.
			JasperPrint jasperPrint = this.jasperReportsServiceImpl.compilarReporteJasper(archivoBytes, orderId);
			
			// Se realiza la carga o subida del reporte PDF a DropBox.
			this.cargarReporteToDropbox(dbxClientV2, orderId, cliente, jasperPrint);
		} catch (DownloadErrorException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DbxException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (IOException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (JRException e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
		return Response.status(Response.Status.OK).entity(mensaje).build();
	}

	@Override
	public void cargarReporteToDropbox(DbxClientV2 dbxClientV2, String orderId, String cliente,
			JasperPrint jasperPrint) throws IOException, JRException, UploadErrorException, DbxException {
		
		String nombreArchivoPdf = orderId + ".pdf";

		File filePDF = File.createTempFile("temp", nombreArchivoPdf);
		
		InputStream archivoExport = new FileInputStream(filePDF);
		
		JRPdfExporter jrPdfExporter = new JRPdfExporter();
		jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePDF));
		
		SimplePdfExporterConfiguration simplePdfExporterConfiguration = new SimplePdfExporterConfiguration();
		jrPdfExporter.setConfiguration(simplePdfExporterConfiguration);
		
		jrPdfExporter.exportReport();
		
		UploadBuilder uploadBuilder = dbxClientV2.files().uploadBuilder(DIRECTORIO_REPORTES + "/" + cliente + "/" + nombreArchivoPdf);
		uploadBuilder.withClientModified(new Date(filePDF.lastModified()));
		uploadBuilder.withMode(WriteMode.ADD);
		uploadBuilder.withAutorename(true);
		
		uploadBuilder.uploadAndFinish(archivoExport);
		
		archivoExport.close();
	}

}
