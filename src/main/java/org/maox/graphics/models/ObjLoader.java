package org.maox.graphics.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cargador que obtiene la geometría de un fichero Wavefront .obj.
 * No es completamente funcional, le faltaría:
 * <p>
 * - Trabajar con más polígonos ademas de triangulos<br/>
 * - Trabajar con librerias de mareriales (.mtl)<br/>
 * 
 * @author Alex 
 */
public class ObjLoader {
	// Logger Instance
	static final Logger logger = LogManager.getLogger();
	
	/** Patrón singleton */
	private static ObjLoader instance = null;
	
    /** Tabla de Modelos que han sido cargados */
	private HashMap<String, ObjModel> table = new HashMap<String, ObjModel>();

    /**
     * Obtiene una instancia del cargador de modelos 3d (Wavefront)
     * @return Cargador de texturas
     */
    static public ObjLoader getInstance()
    {
    	if (instance == null)
    		instance = new ObjLoader();

    	return instance;
    }

    /** 
     * Crear un nuevo cargador de modelos. Cómo los modelos pueden compartirse
     * entre muchos Objetos 3D y no hace falta cargarlos 2 veces vamos a aplicar
     * el patrón sigleton
     */
    private ObjLoader() {
    	
    }
    
	/**
	 * Cargar un fichero Wavefront OBJ desde el classpath
	 * 
	 * @param ref El nombre de referencia del fichero a cargar
	 * @return El modelo OBJ leido del fichero
	 * @throws IOException Error al cargar el fichero.
	 */
	public ObjModel getModel(String ref) throws IOException {
		
    	// Si el modelo ya ha sigo cargado se recupera directamente
		ObjModel model = (ObjModel) table.get(ref);
        
        if (model != null) {
            return model;
        }
        
		logger.info("Loading model: "+ref);
		
		InputStream in = ObjLoader.class.getClassLoader().getResourceAsStream(ref);
	
		if (in == null) {
			logger.error(ref+" not found!!");
			throw new IOException("Unable to find: "+ref);
		}
		
		model = new ObjModel(new ObjData(in));
		
		logger.info("Size model (x,y,z): " + model.getSizeX() + "," +model.getSizeY() + "," + model.getSizeZ());
		
		table.put(ref, model);
		
		return model;
	}
}

