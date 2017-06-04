package org.maox.games;


/**
 * Encapsulación de un juego. Establece las pautas para manejar los
 * diferntes estados del juego, la inicialización de estos y el
 * bucle de juego
 *  
 * @author Alex
 */
public interface Game {
	/**
	 * Añade un estado al juego. La clave será el nombre del estado
	 * 
	 * @param state Estado a añadir
	 * @see GameState.getName()
	 */
	public void addState(GameState state);
	
	/**
	 * Cambia el estado actual y lo actualiza. Si el nombre no coincide
	 * con ningún estado registrado no hace nada.
	 * 
	 * @param name Nombre del estado a cambiar
	 * @throws Exception 
	 */
	public void changeToState(String name) throws Exception;
	
	/**
	 * Bucle de juego principal. Encargado de la actualización y renderizado
	 * de los estados
	 * @throws Exception 
	 */
	public void gameLoop() throws Exception;
	
	/**
	 * Inicializa la ventana y los recursos del juego
	 * @throws Exception 
	 */
	public void init() throws Exception;
	
	/**
	 * Arranque del juego
	 * @throws Exception 
     */
	public void startGame() throws Exception;
}
