package org.maox.games.entities;

/**
 * El interfaz describirá una entidad que aparecerá en el juego.
 * Se definirá la unión entre los elementos que construyen el juego
 * y la clase que los contiene
 * 
 * @author Kevin Glass
 */
public interface Entity {
	/**
	 * Notifica que esta entidad ha colisionado con otra entidad.
	 * Es llamado una vez que la colisión ha ocurrido.
	 * 
	 * @see collides(Entity)
	 * @param manager El gestor responsable de mantener la lista de las entidades
	 * en el juego. Nos proporciona la posibilidad de manipular el resto de
	 * entidades a partir de los eventos que vayan ocurriendo durante la
	 * actualización 
	 * @param other La entidad con la que ha colisionado
	 */
	public void collide(EntityManager manager, Entity other);
	
	/**
	 * Comprueba que la entidad ha colisionado con otra entidad
	 * 
	 * @see getSize()
	 * @param other Entidad contra la que colisionar
	 * @return True si la colisión se ha detectado
	 */
	public boolean collides(Entity other);
	
	/**
	 * Obtener el tamaño de la entidad, (El radio de colisión), es usado por otras
	 * entidades para calcular las colisiones 
	 * 
	 * @return El tamaño de la entidad
	 */
	public float getSize();
	
	/**
	 * Obtener el tamaño horizaontal de la entidad, (El radio de colisión), es usado por otras
	 * entidades para calcular las colisiones 
	 * 
	 * @return El tamaño de la entidad
	 */
	public float getSizeX();
	
	/**
	 * Obtener el tamaño vertical de la entidad, (El radio de colisión), es usado por otras
	 * entidades para calcular las colisiones 
	 * 
	 * @return El tamaño de la entidad
	 */
	public float getSizeY();
	
	/**
	 * Obtener la velocidad X de la entidad
	 * 
	 * @return La velocidad X de la entidad
	 */
	public float getVelocityX();

	/**
	 * Obtener la velocidad Y de la entidad
	 * 
	 * @return La velocidad Y de la entidad
	 */
	public float getVelocityY();
	
	/**
	 * Obtener la velocidad Z de la entidad
	 * 
	 * @return La velocidad Z de la entidad
	 */
	public float getVelocityZ();
	
	/**
	 * Obtener la coodenada X de posición de la entidad
	 * 
	 * @return La coodenada X de posición de la entidad
	 */
	public float getX();

	/**
	 * Obtener la coodenada Y de posición de la entidad
	 * 
	 * @return La coodenada Y de posición de la entidad
	 */
	public float getY();
	
	/**
	 * Obtener la coodenada Z de posición de la entidad
	 * 
	 * @return La coodenada Z de posición de la entidad
	 */
	public float getZ();
	
	/**
	 * Renderizar la entidad. Esto causa que la implementación de la entidad
	 * ejecute las llamadas de OpenGl para producir la geometría para su
	 * representación visual.
	 * 
	 * Será llamada en cada frame de renderizador de la escena
	 */
	public void render();
	
	/**
	 * Actualizar la entidad, se realizará lo que sea necesario (movimientos,
	 * comprobar controles, etc)
	 * 
	 * @param manager El gestor responsable de mantener la lista de las entidades
	 * en el juego. Nos proporciona la posibilidad de manipular el resto de
	 * entidades a partir de los eventos que vayan ocurriendo durante la
	 * actualización 
	 * @param delta La contidad de tiempo en milisegundos que han transcurrido
	 * desde la última actualización
	 */
	public void update(EntityManager manager, int delta);
}
