package org.maox.graphics.models;

/**
 * Una tupla simple de 2 elementos. En este caso, este conjunto puede representar
 * un vertice o normal en un espacio 2D o una coordenada de textura
 * 
 * @author Kevin Glass
 */
class Tuple2 {
	/** Elemento x de la tupla */
	private float x;
	/** Elemento y de la tupla */
	private float y;
	
	/**
	 * Crear una nueva tupla de 2 elementos
	 * 
	 * @param x El valor del elemento X
	 * @param y El valor del elemento Y
	 */
	public Tuple2(float x,float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Obtener el elemento X de esta tupla
	 * 
	 * @return El valor del elemento X
	 */
	public float getX() {
		return x;
	}

	/**
	 *  Obtener el elemento Y de esta tupla
	 * 
	 * @return El valor del elemento Y
	 */
	public float getY() {
		return y;
	}
	
}