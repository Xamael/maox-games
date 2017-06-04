package org.maox.test;

import static org.lwjgl.opengl.GL11.GL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_EMISSION;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_LOCAL_VIEWER;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_TWO_SIDE;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SPECULAR;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glLightModeli;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMaterial;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.maox.time.Clock.getTimeMilis;

import java.awt.Font;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.maox.graphics.Graphics;
import org.maox.graphics.models.ObjLoader;
import org.maox.graphics.models.ObjModel;
import org.maox.graphics.textures.Texture;
import org.maox.graphics.textures.TextureLoader;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

public class ViewObj {
	// Logger Instance
	static final Logger	logger			= LogManager.getLogger();

	/**
	 * Atributos
	 */
	public static int	WIDTH			= 400;
	public static int	HEIGHT			= 300;
	private int			TARGET_FPS		= 60;						// Objetivo de FPS
	private long		OPTIMAL_TIME	= 1000 / TARGET_FPS;		// Tiempo optimo en ms entre Frames

	private Texture		texture;									// Textura a aplicar al modelo
	private ObjModel	model			= null;					// Modelo a visualizar

	private float		anguloX			= 30;
	private float		anguloY			= 30;
	private float		anguloZ			= 0;
	private float		fScale			= 10;

	// Fuentes de Escritura
	TrueTypeFont		fArial			= null;

	/**
	 * Programa principal
	 * 
	 * @param args Modelo 3D en formato Wavefront a cargar
	 */
	public static void main(String[] args) {

		try {
			ViewObj viewer = new ViewObj(args[0], args[1]);
			viewer.loop();
		}
		catch (Exception e) {
			logger.fatal(e);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param string Ruta del fichero Wavefront OBJ
	 * @throws Exception
	 */
	public ViewObj(String resOBJ, String resTex) throws Exception {
		initDisplay();
		initFonts();
		initGraphics();
		initLight();
		loadModel(resOBJ, resTex);
	}

	/**
	 * Inicia la ventana gráfica
	 */
	private void initDisplay() throws Exception {
		logger.info("Init Display");

		// Obtener cuantos Bits por Pixel hay actualmente en el escritorio
		int currentBpp = Display.getDisplayMode().getBitsPerPixel();

		// Encontrar el DisplayMode 800x600
		DisplayMode mode = Graphics.findDisplayMode(WIDTH, HEIGHT, currentBpp);

		// Si el modo no está disponible salir con error.
		if (mode == null) {
			logger.fatal("Modo de video " + WIDTH + "x" + HEIGHT + "x" + currentBpp + " no disponible.");
			return;
		}

		// Configurar y crear el display LWJGL
		Display.setTitle("Wavefront Loader");
		Display.setDisplayMode(mode);
		Display.setFullscreen(false);

		// Creación de la ventana (Vista)
		Display.create();
	}

	/**
	 * Inicialización de las fuentes TrueType para escritura
	 */
	private void initFonts() {
		//this.getClass().getResourceAsStream("Arial.ttf");
		//Font fArial = Font.createFont(Font.TRUETYPE_FONT, arg1)
		Font fArialAWT = new Font("Arial", Font.PLAIN, 12);
		fArial = new TrueTypeFont(fArialAWT, false);

	}

	/**
	 * Inicialización de modo grafico de OpenGL
	 */
	private void initGraphics() {
		// Solo renderizar las caras frontales de los poligonos (muy importante en solidos)
		glEnable(GL_CULL_FACE);

		// Funcion de comparacion del buffer de profundidad.
		glDepthFunc(GL_LEQUAL);

		// Renderizado suave.
		glShadeModel(GL_SMOOTH);

		// Establecer el color de borrado cuando se use glClear
		glClearColor(0, 0, 0, 1.0f);

		// Calidad buena en la correccion de perpectiva
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

		// Inicialización de las matrices de proyeccion y transformación e
		// establecer la perspectiva de proyeccion
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		// Perspectiva de visualización
		// Establecer la parte de la ventana que se usará para en renderizado
		// En este caso toda la pantalla
		glViewport(0, 0, WIDTH, HEIGHT);

		// Perpectiva
		//GLU.gluPerspective(45.0f, ((float)WIDTH) / ((float)HEIGHT), 0.1f, 100.0f);
		//glEnable(GL_DEPTH_TEST); // Test de profundidad
		//glClearDepth(1);

		// Ortografica
		glOrtho(0, WIDTH, HEIGHT, 0, -1000, 1000);
		glDisable(GL_DEPTH_TEST);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	/**
	 * Inicialización de la Luz que ilumina la escena
	 * 
	 * @throws Exception
	 */
	private void initLight() throws Exception {

		// Los Buffer tenrán el componente RGB de la luz (4 paramatro es 1 para normalizar)

		// Luz ambiental general
		FloatBuffer bufferModel = BufferUtils.createFloatBuffer(4);
		bufferModel.put(1f).put(1f).put(1f).put(1f);
		bufferModel.flip();

		glLightModel(GL_LIGHT_MODEL_AMBIENT, bufferModel);

		// Modo de cálculo del brillo de la luz especular
		glLightModeli(GL_LIGHT_MODEL_LOCAL_VIEWER, GL_TRUE);

		// Invertir normales si el poligono se da la vuelta
		glLightModeli(GL_LIGHT_MODEL_TWO_SIDE, GL_TRUE);

		// Caracteristicas de la Luz 0 (Se usará como Luz general)
		// Luz ambiental (Procede de todas partes)
		FloatBuffer bufferAmb = BufferUtils.createFloatBuffer(4);
		bufferAmb.put(0.2f).put(0.2f).put(0.2f).put(1);
		bufferAmb.flip();

		glLight(GL_LIGHT0, GL_AMBIENT, bufferAmb);

		// Luz Difusa (Procede de la fuente y rebota en todas direcciones)
		FloatBuffer bufferDif = BufferUtils.createFloatBuffer(4);
		bufferDif.put(0.5f).put(0.5f).put(0.5f).put(1);
		bufferDif.flip();

		glLight(GL_LIGHT0, GL_DIFFUSE, bufferDif);

		// Luz especular (Procede de la fuente y rebota en una dirección (brillo))
		FloatBuffer bufferEsp = BufferUtils.createFloatBuffer(4);
		bufferEsp.put(0.8f).put(0.8f).put(0.8f).put(1);
		bufferEsp.flip();

		glLight(GL_LIGHT0, GL_SPECULAR, bufferEsp);

		// Posición de la Luz
		FloatBuffer bufferPos = BufferUtils.createFloatBuffer(4);
		bufferPos.put(0).put(0).put(-5).put(0);
		bufferPos.flip();

		glLight(GL_LIGHT0, GL_POSITION, bufferPos);

		// Activar la Luz
		glEnable(GL_LIGHTING);

		// Activar la luz 0
		glEnable(GL_LIGHT0);
	}

	/**
	 * Carga el modelo y la textura
	 * 
	 * @param resOBJ Ruta del Modelo
	 * @param resTex Ruta de la textura
	 * @throws IOException
	 */
	private void loadModel(String resOBJ, String resTex) throws IOException {
		//Se carga la textura y el modelo
		ObjLoader loader = ObjLoader.getInstance();
		model = loader.getModel(resOBJ);

		// Redimensionar para que ocupe toda la pantalla
		float fScaleX = (float) ((WIDTH / model.getSizeX()) * 0.8);
		float fScaleY = (float) ((WIDTH / model.getSizeY()) * 0.8);

		fScale = (fScaleX > fScaleY ? fScaleY : fScaleX);

		TextureLoader loaderTex = TextureLoader.getInstance();
		texture = loaderTex.getTexture(resTex);
	}

	/**
	 * Arranque de bucle de visualización
	 */
	private void loop() {
		int fps = 0;
		long lastFpsTime = 0;
		boolean running = true;
		long lastLoop = getTimeMilis();

		// Mientraseste corriendo se acutalizan y renderizan los estados
		while (running) {

			// Se calcula cuanto ha pasado desde el último bucle para poder actualizar
			// el renderizado y la lógica esa cantidad de tiempo determinada.
			int delta = (int) (getTimeMilis() - lastLoop);
			lastLoop = getTimeMilis();

			// Contador de los FPS
			lastFpsTime += delta;
			fps++;

			// Actualizar el FPS si ha transcurrido un segundo
			if (lastFpsTime >= 1000) {
				Display.setTitle("Wavefront Loader (FPS: " + fps + ")");
				lastFpsTime = 0;
				fps = 0;
			}

			// Se actualiza la lógica dependiendo del tiempo transcurrido
			// Se va a realizar en ciclos de 10 ms
			int step = delta / 10;

			for (int i = 0; i < step; i++) {
				updatePosition(10);
			}

			// Renderizado de los objetos
			// Limpieza del buffer de pantalla y del bit de profundidad
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			renderModel();
			renderText();

			glFlush();

			int remainder = delta % 10;
			if (remainder != 0) {
				updatePosition(remainder);
			}

			// Se le indica al LWJGL que actualize la vista
			// El resfresco se realiza por Doble Buffering
			// Como efecto secundario se comprobará el teclado / ratón y controladores
			Display.update();

			// Si el usuario ha solicitado el cierre de la ventana
			if (Display.isCloseRequested()) {
				running = false;
				System.exit(0);
			}

			// Dormir el Thread para optimizar la CPU
			// Tiempo que se ha tardado en actualizar la logica y renderizar todo
			long timeBetweenUpdate = getTimeMilis() - lastLoop;
			Thread.yield();
			if (timeBetweenUpdate < OPTIMAL_TIME) {
				try {
					Thread.sleep(OPTIMAL_TIME - timeBetweenUpdate);
				}
				catch (InterruptedException e) {
				}
			}
		} // bucle
	}

	/**
	 * Modo de reflejo de Luz del material
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param alpha
	 * @return
	 */
	private FloatBuffer material(float r, float g, float b, float alpha) {
		// Definir el material del que está hecho el modelo para
		// saber como refleja la Luz
		FloatBuffer material = BufferUtils.createFloatBuffer(4);
		float reflect[] = { r, g, b, alpha };
		material.put(reflect);
		material.flip();

		return material;
	}

	/**
	 * Renderizado del Modelo
	 */
	private void renderModel() {
		// Establecer iluminación para el modelo
		glEnable(GL_LIGHTING);

		// Almacenar la matrix actual para poder modificar sin afectar nada de otro sitio
		glPushMatrix();

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		// Posicionar el modelo a partir de la posición acutal
		glTranslatef(WIDTH / 2 - model.getSizeX() / 2, HEIGHT / 2 - model.getSizeY() / 2, 0);
		glRotatef(anguloX, 1f, 0f, 0f);
		glRotatef(anguloY, 0f, 1f, 0f);
		glRotatef(anguloZ, 0f, 0f, 1f);

		// Escalar el modelo porque es demasiado grande
		glScalef(fScale, fScale, fScale);

		// Material de las caras frontales y posteriores
		//glMaterial(GL_FRONT, GL_AMBIENT, material(0.5f, 0.5f, 0.5f, 0.5f));
		//glMaterial(GL_FRONT, GL_DIFFUSE, material(0.5f, 0.5f, 0.5f, 0.5f));
		//glMaterial(GL_FRONT, GL_SPECULAR, material(1, 1, 1, 1));
		//glMaterialf(GL_FRONT, GL_SHININESS, 255);
		//glMaterial(GL_FRONT, GL_SPECULAR, material(1, 1, 1, 1));
		glMaterial(GL_FRONT, GL_EMISSION, material(0.1f, 0.6f, 0.3f, 0.3f));

		// Cuerpo cerrado, no hace falta definir las caras traseras
		//glMaterial(GL_BACK, GL_DIFFUSE, material);

		// Enlazar la textura al modelo y renderizar el modelo
		glDisable(GL_TEXTURE_2D);

		if (model.hasTexture()) {
			// Le indico a OpenGL que voy a usar texturas para pintar los
			// objetos y que van a ser de 2 dimensiones (un dibujo normal).
			glEnable(GL_TEXTURE_2D);
			// Modo de mezcla de la textura con el color del material (MODULATE: Mezcla)
			glTexEnvf(GL_TEXTURE_2D, GL_TEXTURE_ENV_MODE, GL_MODULATE);

			texture.bind();
		}

		model.render();

		// Restaurar la matriz a como estaba al entrar en el metodo
		glPopMatrix();
	}

	/**
	 * Renderizado de Texto
	 */
	private void renderText() {
		// Almacenar la matrix actual para poder modificar sin afectar nada de otro sitio
		glPushMatrix();

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glDisable(GL_LIGHTING);
		glEnable(GL_TEXTURE_2D);
		TextureImpl.bindNone();

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		//Color.white.bind(); // color por defecto
		fArial.drawString(10, 280, "X: " + anguloX, Color.white);

		// Restaurar la matriz a como estaba al entrar en el metodo
		glPopMatrix();
	}

	/**
	 * Actualiza la posición del modelo y cámara
	 * 
	 * @param i Intervalo de tiempo en milisegundos
	 */
	private void updatePosition(int iMilis) {
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
			anguloY--;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
			anguloY++;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
			anguloX++;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
			anguloX--;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7)) {
			anguloZ++;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9)) {
			anguloZ--;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
			fScale--;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
			fScale++;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
			anguloX = 0;
			anguloY = 0;
			anguloZ = 0;
		}

	}
}
