package org.maox.games.entities;

/**
 * Una implementación abstracta de una entidad 2D en el juego
 * An abstract implementation of an in game entity. This provides
 * the code for the common functionality between the majority of 
 * entities including movement, rotating and collision detection.
 * 
 * Nota: Aunque las entidades sean modelos 3D y el juego corra en 3D
 * se está considerando que sólo se pueden mover en 2D obteniendo
 * un efecto del clásico juego 2D
 * 
 * @author Kevin Glass
 * @author Alex 
 */
public abstract class AbstractEntity2D implements Entity {
	
	/** Anchura del area de juego */
	private static final int PLAY_AREA_WIDTH = 800;
	//private static final int PLAY_AREA_WIDTH = 56;
	/** Altura del area de juego */
	private static final int PLAY_AREA_HEIGHT = 600;
	//private static final int PLAY_AREA_HEIGHT = 46;

	/** La posición X de la entidad */
	protected float positionX = 0;
	/** La posición Y de la entidad */
	protected float positionY = 0;
	/** La posición Z de la entidad (No se podrá mover en este eje) */
	// Lo alejamos para poder ver los objetos de lejos
	protected float positionZ = -50;
	
	/** El componente X de velociad de la entidad */
	protected float velocityX = 0;
	/** El componente Y de velociad de la entidad */
	protected float velocityY = 0;
	
	/**
	 * @see org.maox.graphics.Entity#collides()
	 */
	public boolean collides(Entity other) {
		// La colisión se realizará usando un cirulo de sencillo
		// en 2D

		// Si la distancia entre el centro de los 2 circulos es inferior
		// a la suma de sus radios es que ha ocurrido una colisión

		// Trabajar con la distancia entre los dos puntos requeriria 
		// Una raiz cuadrada (Math.sqrt((dx*dx)+(dy*dy)) la cual puede
		// se algo lenta
		//
		// En vez de eso vamos a comparar la suma de los radios al cuadrado
		// contra un valor sin hacer la raiz que es mucho más rapido
		
		// El tamaño de la otra entidad combinado con el de esta da el
		// rango de colisión. Y lo hacemos cuadrado
		float otherSize = other.getSize();
		float range = (otherSize + getSize());
		range *= range;

		// Distancia entre los dos puntos
		float dx = getX() - other.getX();
		float dy = getY() - other.getY();
		float distance = (dx*dx)+(dy*dy);
		
		// Si el cuadrado de la distancia es menor que el cuadrado del rango
		// hay colisión
		return (distance <= range);
	}
	
	/**
	 * @see org.maox.graphics.Entity#getVelocityX()
	 */
	public float getVelocityX() {
		return velocityX;
	}
	
	/**
	 * @see org.maox.graphics.Entity#getVelocityY()
	 */
	public float getVelocityY() {
		return velocityY;
	}
	
	/**
	 * @see org.maox.graphics.Entity#getVelocityZ()
	 */
	public float getVelocityZ() {
		return 0f;
	}

	/**
	 * @see org.maox.graphics.Entity#getX()
	 */
	public float getX() {
		return positionX;
	}

	/**
	 * @see org.maox.graphics.Entity#getY()
	 */
	public float getY() {
		return positionY;
	}

	/**
	 * @see org.maox.graphics.Entity#getZ()
	 */
	public float getZ() {
		return positionZ;
	}
	
	/**
	 * @see org.newdawn.asteroids.entity.Entity#update(org.newdawn.asteroids.entity.EntityManager, int)
	 */
	public void update(EntityManager manager, int delta) {
		float fMargen = 0f; // En procentaje (0..1)
		
		// Actualizar la posición a partir de la velocidad
		positionX += (velocityX * delta) / 1000.0f;
		positionY += (velocityY * delta) / 1000.0f;
		
		// Limite de movimiento es el area de juego
		if (positionX < getSizeX()/2 + PLAY_AREA_WIDTH*fMargen) {
			positionX = (int)(getSizeX()/2 + PLAY_AREA_WIDTH*fMargen);
			velocityX = 0;
		}
		
		if (positionX > PLAY_AREA_WIDTH - getSizeX()/2 - PLAY_AREA_WIDTH*fMargen) {
			positionX = (int)(PLAY_AREA_WIDTH - getSizeX()/2 - PLAY_AREA_WIDTH*fMargen);
			velocityX = 0;
		}
		
		if (positionY < getSizeY()/2 + PLAY_AREA_WIDTH*fMargen) {
			positionY = (int)(getSizeY()/2 + PLAY_AREA_WIDTH*fMargen);
			velocityY = 0;
		}
		
		if (positionY > PLAY_AREA_HEIGHT - getSizeY()/2 - PLAY_AREA_HEIGHT*fMargen) {
			positionY = (int)(PLAY_AREA_HEIGHT - getSizeY()/2 - PLAY_AREA_HEIGHT*fMargen);
			velocityY = 0;
		}		
	}
}
