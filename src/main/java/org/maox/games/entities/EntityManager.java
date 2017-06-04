package org.maox.games.entities;

/**
 * The description of the class holding and maintaining the list of 
 * entities within the game. This interface forms the contract between
 * the entities being held in the game and their container. It provides
 * the entity logic a callback to the game code in a non-coupled manner.
 * 
 * @author Kevin Glass
 * @author Alex
 */
public interface EntityManager {
	/**
	 * Añade una entidad al juego
	 * 
	 * @param entity Entidad a ser añadida
	 */
	public void addEntity(Entity entity);
	
	/**
	 * Obtiene una entidad del juego identificada por una clave
	 * @param key Clave para obtener la entidad
	 * @return Entidad
	 */
	public Entity getEntity(String key);

	/**
	 * Elimina una entidad del juego
	 * 
	 * @param entity Entidad a ser eliminada
	 */
	public void removeEntity(Entity entity);
}
