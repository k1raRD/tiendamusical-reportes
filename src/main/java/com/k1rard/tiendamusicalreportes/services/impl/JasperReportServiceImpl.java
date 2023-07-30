/**
 * 
 */
package com.k1rard.tiendamusicalreportes.services.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.k1rard.tiendamusicalreportes.services.JasperReportsService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * @author k1rard
 * Clase que implementa los metodos de logica de negocio de Jasper Reports.
 */
@Service
public class JasperReportServiceImpl implements JasperReportsService{
	
	@Value("${spring.datasource.driverClassName}")
	private String driver;
	
	@Value("${spring.datasource.url}")
	private String url;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;

	@Override
	public JasperPrint compilarReporteJasper(ByteArrayOutputStream archivoBytes, String orderID) throws ClassNotFoundException, SQLException, JRException, IOException {
		
		// Se obtiene la imagen dle archivo del classpath del proyecto.
		InputStream imageInputStream = this.getClass().getClassLoader().getResourceAsStream("images/logo-x.png");
		
		// Se envian los parametros de compilacion para el archivo jrxml
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderID);
		map.put("logo", imageInputStream);
		
		// Se convierte el archivo de salida a un flujo de bytes.
		byte[] bytes = archivoBytes.toByteArray();
		InputStream archivoInputStream = new ByteArrayInputStream(bytes);
		
		// Se asignan los parametros de conexion para el archivo jasper.
		Class.forName(this.driver);
		Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
		
		JasperReport jasperReport = JasperCompileManager.compileReport(archivoInputStream);
		
		imageInputStream.close();
		archivoInputStream.close();
		return JasperFillManager.fillReport(jasperReport, map, connection);
	}

}
