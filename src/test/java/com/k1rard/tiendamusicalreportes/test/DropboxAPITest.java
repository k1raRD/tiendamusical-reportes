/**
 * 
 */
package com.k1rard.tiendamusicalreportes.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

/**
 * @author k1rard
 * Prueba unitaria para verificar la comunicacion de un aplicativo Java con Dropbox
 */
class DropboxAPITest {

	@Test
	void test() {
		// :::::::::::: Se configura el token de acceso a la app creada en DropBox ::::::::::::::
		String TOKEN = "sl.BjFEx3BQIKxP_lm3F0DeDkQXtT0B7-JLobzw4on9lOMzlsyA-Werw-cl1YBnGv372hpyjRaEBKDHDqojeEPGukLAMBPhGocSjv98mDbv_U5Ib5ovwnryCEadPcQ4wJ0E6nWTjaRN_YPE";
		
		// :::::::::::: Se configura el token y el ambiente de configuracion inicial de DropBox ::::::::::::::
		DbxRequestConfig dbxRequestConfig = DbxRequestConfig.newBuilder("k1rard/text-dropbox").build();
		DbxClientV2 dbxClientV2 = new DbxClientV2(dbxRequestConfig, TOKEN);
		
		try {
			assertNotNull(dbxClientV2);
			
			// :::::::: Se obtiene y se muestra la informacion de la cuenta perteneciente a la app :::::::::::
			FullAccount fullAccount = dbxClientV2.users().getCurrentAccount();
			System.out.println("Nombre de la cuenta: " + fullAccount.getEmail());
			
			// Get files and folder metadata from Dropbox root directory
			ListFolderResult result = dbxClientV2.files().listFolder("");
			while (true) {
			    for (Metadata metadata : result.getEntries()) {
			        System.out.println(metadata.getPathLower());
			    }

			    if (!result.getHasMore()) {
			        break;
			    }

			    result = dbxClientV2.files().listFolderContinue(result.getCursor());
			}
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertNull(dbxClientV2, "Probando si es null");
		}
		
		
	}

}
