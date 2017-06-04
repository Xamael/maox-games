package org.maox.time;

import org.lwjgl.Sys;

/**
 * Encapsulacion de varias funciones con utilidades de obtención de tiempo
 *
 * @author Alex
 */
public class Clock {

	/**
	 * Obtiene el tiempo actual en milisegundos a partir del reloj
	 * de sistema LWJGL de alta resolución
	 * 
	 * @return El tiempo en milisegundos
	 */
	static public long getTimeMilis() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
}
