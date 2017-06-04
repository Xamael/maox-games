package org.maox.graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Encapsulacion de varias funciones con utilidades graficas
 *
 * @author Alex
 */
public class Graphics {

	/**
	 * Determina un modo gráfico compatible con los parametros pasados
	 * 
	 * @param width Anchura deseada de la pantalla
	 * @param height Altura deseada de la pantalla
	 * @param bpp La profundidad de color deseada (bits per pixel) de la ventana
	 * @return El Display Modo que encaja con los parametros  null si no se encuentra ninguno
	 * @throws LWJGLException Indica un fallo en la interación con la biblioteca LWJGL
	 */
	public static DisplayMode findDisplayMode(int width, int height, int bpp) throws LWJGLException {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		DisplayMode mode = null;
		
		for (int i=0;i<modes.length;i++) {
			if ((modes[i].getBitsPerPixel() == bpp) || (mode == null)) {
				if ((modes[i].getWidth() == width) && (modes[i].getHeight() == height)) {
					mode = modes[i];
				}
			}
		}
		
		return mode;
	}
	
}
