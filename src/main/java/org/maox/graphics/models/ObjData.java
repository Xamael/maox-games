package org.maox.graphics.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Los datos que se han leído desde el Wavefont .obj. Se mantendrá
 * separádo del renderizado con la esperanza que poder usar la
 * información con otro motor de renderizado en el futuro.
 * 
 * @author Kevin Glass
 * @author Alex
 */
public class ObjData {
	// Logger Instance
	static final Logger logger = LogManager.getLogger();

	/** Los vertices leídos del fichero */
	private ArrayList<Tuple3> verts = new ArrayList<Tuple3>();
	/** Las normales leídas del fichero */
	private ArrayList<Tuple3> normals = new ArrayList<Tuple3>();
	/** Las coordenadas de Textura leídas del fichero */
	private ArrayList<Tuple2> texCoords = new ArrayList<Tuple2>();
	/** Las caras leídas del fichero */
	private ArrayList<Face> faces = new ArrayList<Face>();
	
	/**
	 * Create a new set of OBJ data by reading it in from the specified
	 * input stream.
	 * 
	 * @param in The input stream from which to read the OBJ data
	 * @throws IOException Indicates a failure to read from the stream
	 */
	public ObjData(InputStream in) throws IOException {
		// read the file line by line adding the data to the appropriate
		// list held locally
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		while (reader.ready()) {
			String line = reader.readLine();
			
			// if we read a null line thats means on some systems
			// we've reached the end of the file, hence we want to 
			// to jump out of the loop
			if (line == null) {
				break;
			}
			
			// "vn" indicates normal data
			if (line.startsWith("vn")) {
				Tuple3 normal = readTuple3(line);
				normals.add(normal);
			// "vt" indicates texture coordinate data
			} else if (line.startsWith("vt")) {
				Tuple2 tex = readTuple2(line);
				texCoords.add(tex);
			// "v" indicates vertex data
			} else if (line.startsWith("v")) {
				Tuple3 vert = readTuple3(line);
				verts.add(vert);
			// "f" indicates a face
			} else if (line.startsWith("f")) {
				Face face = readFace(line);
				faces.add(face);
			}
		}
		
		// Traza del objeto leído
		logger.info("Read " + verts.size() + " vertex");
		logger.info("Read " + faces.size() + " faces");
		
		// Advetencias
		if (texCoords.isEmpty())
			logger.warn("Texture UV coord not found");
	}
	
	/**
	 * Get the data for specific face
	 * 
	 * @param index The index of the face whose data should be retrieved
	 * @return The face data requested
 	 */
	public Face getFace(int index) {
		return (Face) faces.get(index);
	}
	
	/**
	 * Get the number of faces found in the model file
	 * 
	 * @return The number of faces found in the model file
	 */
	public int getFaceCount() {
		return faces.size();
	}
	
	/**
	 * Lee un conjunto de datos de la líne a de face(cara)
	 * 
	 * @param line La línea de la cual interpretar los datos
	 * @return La cara extraida de la línea
	 * @throws IOException Error en el proceso de la línea
	 */
	private Face readFace(String line) throws IOException {
		StringTokenizer points = new StringTokenizer(line, " ");
		
		points.nextToken();
		int faceCount = points.countTokens();
		
		// Actualmente sólo se soportan triangulos
		if (faceCount != 3) {
			throw new RuntimeException("Only triangles are supported");
		}
		
		// Es posible que el objeto no tenga defindas texturas
		boolean bTex = !texCoords.isEmpty();
		
		// Se cre una nueva cara con el número de vertices leído
		Face face = new Face(faceCount);
		
		try {
			// Por cada línea se van a leer 3 bits de datos
			// El vertice, la textura y la normal
			for (int i=0;i<faceCount;i++) {
				StringTokenizer parts = new StringTokenizer(points.nextToken(), "/");
				
				int v = Integer.parseInt(parts.nextToken());
				
				int t = 0;
				if (bTex)
					t = Integer.parseInt(parts.nextToken());
				
				int n = Integer.parseInt(parts.nextToken());
				
				// Se añade el punto a la cara
				face.addPoint((Tuple3) verts.get(v-1),
						      bTex?(Tuple2) texCoords.get(t-1):null,
							  (Tuple3) normals.get(n-1));
			}
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());
		}
		
		return face;
	}
	
	/**
	 * Read a set of 2 float values from a line assuming the first token
	 * on the line is the identifier
	 * 
	 * @param line The line from which to read the 3 values
	 * @return The set of 2 floating point values read
	 * @throws IOException Indicates a failure to process the line
	 */
	private Tuple2 readTuple2(String line) throws IOException {
		StringTokenizer tokens = new StringTokenizer(line, " ");
		
		tokens.nextToken();
		
		try {
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());
			
			return new Tuple2(x,y);
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	/**
	 * Read a set of 3 float values from a line assuming the first token
	 * on the line is the identifier
	 * 
	 * @param line The line from which to read the 3 values
	 * @return The set of 3 floating point values read
	 * @throws IOException Indicates a failure to process the line
	 */
	private Tuple3 readTuple3(String line) throws IOException {
		StringTokenizer tokens = new StringTokenizer(line, " ");
		
		tokens.nextToken();
		
		try {
			float x = Float.parseFloat(tokens.nextToken());
			float y = Float.parseFloat(tokens.nextToken());
			float z = Float.parseFloat(tokens.nextToken());
			
			return new Tuple3(x,y,z);
		} catch (NumberFormatException e) {
			throw new IOException(e.getMessage());
		}
	}
}
