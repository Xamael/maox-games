package org.maox.graphics.models;

import static org.lwjgl.opengl.GL11.*;

/**
 * Implementación de un modelo OBJ que renderiza los datos a traves de
 * una display list.
 * 
 * @author Kevin Glass
 * @author Alex 
 */
public class ObjModel {
	/** Identificador del Display List que indentifica este modelo */
	private int listID;
	/** Indicador de si tiene texturas o no */
	private boolean hasTexture = false;
	/** Tamño del modelo cargado */
	private float fSizeX = 0f;
	private float fSizeY = 0f;
	private float fSizeZ = 0f;
	
	/**
	 * Crear un nuevo modelo OBJ que renderizará el objeto en openGL.
	 * 
	 * @param data Conjunto de datos (vertices, caras, etc) a renderizar
	 */
	public ObjModel(ObjData data) {
		/** Variables usadas para obtener el tamaño del modelo */
		float fMinSizeX = 0;
		float fMaxSizeX = 0;
		float fMinSizeY = 0;
		float fMaxSizeY = 0;
		float fMinSizeZ = 0;
		float fMaxSizeZ = 0;
		
		//Se genera el identificador del Display List
		listID = glGenLists(1);
		
		// Comienzo de las instrucciones del modelo
		glNewList(listID, GL_COMPILE);
		
		// Bucle por todas las caras de modelo renderizando un triangulo por
		// cada una de ellas
		glBegin(GL_TRIANGLES);
		
		int faceCount = data.getFaceCount();
				
		for (int i=0; i<faceCount; i++) {
			for (int v=0;v<3;v++) {

				// Una coordenada de Posición, Normal y Textura de cada vertice de la cara
				Tuple3 vert = data.getFace(i).getVertex(v);
				Tuple3 norm = data.getFace(i).getNormal(v);
				Tuple2 tex = data.getFace(i).getTexCoord(v);
						
				glNormal3f(norm.getX(), norm.getY(), norm.getZ());
				
				// En caso que no se hayan leído coordenadas de textura no se realiza el tratamiento
				if (tex!=null)
				{
					glTexCoord2f(tex.getX(), tex.getY());
					hasTexture = true;
				}
				
				glVertex3f(vert.getX(), vert.getY(), vert.getZ());
				
				// Se almacenan los límites del modelo para saber su tamaño
				if (i==0 && v==0)  {
					fMinSizeX = vert.getX();
					fMaxSizeX = vert.getX();
					fMinSizeY = vert.getY();
					fMaxSizeY = vert.getY();
					fMinSizeZ = vert.getZ();
					fMaxSizeZ = vert.getZ();
				}
				else {
					if (vert.getX()<fMinSizeX) {
						fMinSizeX = vert.getX();
					}
					if (vert.getX()>fMaxSizeX) {
						fMaxSizeX = vert.getX();
					}
					if (vert.getY()<fMinSizeY) {
						fMinSizeY = vert.getY();
					}
					if (vert.getY()>fMaxSizeY) {
						fMaxSizeY = vert.getY();
					}
					if (vert.getZ()<fMinSizeZ) {
						fMinSizeZ = vert.getZ();
					}
					if (vert.getZ()>fMaxSizeZ) {
						fMaxSizeZ = vert.getZ();
					}
				}

			} // bucle vertices
		} // bucle caras
				
		glEnd();
		glEndList();
		
		fSizeX = fMaxSizeX - fMinSizeX;
		fSizeY = fMaxSizeY - fMinSizeY;
		fSizeZ = fMaxSizeZ - fMinSizeZ;
	}
	
	/**
	 * Obtiene el tamaño del objeto en el eje X 
	 * @return
	 */
	public float getSizeX() {
		return fSizeX;
	}
	
	/**
	 * Obtiene el tamaño del objeto en el eje X 
	 * @return
	 */
	public float getSizeY() {
		return fSizeY;
	}
	
	/**
	 * Obtiene el tamaño del objeto en el eje X 
	 * @return
	 */
	public float getSizeZ() {
		return fSizeZ;
	}
	
	/**
	 * Indica si el objeto a cargado correctamente las coodenadas de texturas
	 * @return boolean
	 */
	public boolean hasTexture() {
		return hasTexture;
	}
	
	/**
	 * Renderizdo del Modelo OBJ
	 */
	public void render() {
		// Como se ha definido el renderizado al construir el DisplayList
		// sólo hace falta llamar a la lista con el indentificador
		glCallList(listID);
	}
}

