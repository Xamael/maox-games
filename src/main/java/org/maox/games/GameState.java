package org.maox.games;

import java.io.IOException;

import org.maox.games.Game;

/**
 * Un estado en el que el juego reside. Se encapsula las situaciones
 * para modularizar las diferentes partes del juego
 * 
 * @author Kevin Glass 
 * @author mod by Alex
 */
public interface GameState {
	// Atributos
	Game game = null; // juego que ha registrado el estado
	
	/**
	 * Notificación que se entra en el estado
	 * @throws Exception 
	 * 
	 */
	public void enter() throws Exception;
	
	/**
	 * Obtiene el nombre por el que se identifica a este estado.
	 * Este nombre permite saltar entre estados
	 * 
	 * @return El nombre del estado
	 */
	public String getName();
	
	/**
	 * Inicializa el estado.
	 * 
	 * @throws IOException Indica un error en la carga de los recursos de la inicialización
	 */
	public void init(Game game) throws Exception;
	
	/**
	 * Notificación que se sale del estado
	 * 
	 */
	public void leave();
	
	/**
	 * Renderiza el estado en la pantalla
	 * 
	 * @param delta Cantidad de tiempo pasada desde la ultima renderización
	 */
	public void render(int delta);

	/**
	 * Actualiza la lógica del estado
	 * 
	 * @param delta Cantidad de tiempo desde la ultima actualización
	 * @throws Exception 
	 */
	public void update(int delta) throws Exception;
}