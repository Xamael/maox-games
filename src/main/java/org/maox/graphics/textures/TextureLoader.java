package org.maox.graphics.textures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL11.*;

/**
 * Clase de utilidades para cargar Texturas por LWJGL.
 * This source is based on a texture that can be found in
 * the Java Gaming (www.javagaming.org) Wiki. 
 * 
 * Debido a que OpenGl utiliza un formato peculiar de imagenes,
 * este cargador se encarga de almacenar en una imagen intermedia
 * la textura desde el formato de disco.
 *
 * @author Kevin Glass
 * @author Brian Matzon
 * @author Alex (patrón sigleton)
 */
public class TextureLoader {
	// Logger Instance
	static final Logger logger = LogManager.getLogger();

	/** Patrón singleton */
	private static TextureLoader instance = null;

    /** Tabla de texturas que han sido cargadas */
	private HashMap<String, Texture> table = new HashMap<String, Texture>();

    /** El modelo de color incluyendo el canal alfa para la imagen GL */
    private ColorModel glAlphaColorModel;
    
    /** El modelo de color para la imagen GL */
    private ColorModel glColorModel;
    
    /**
     * Obtiene una instancia del cargador de texturas
     * @return Cargador de texturas
     */
    static public TextureLoader getInstance()
    {
    	if (instance == null)
    		instance = new TextureLoader();

    	return instance;
    }

    /** 
     * Crear un nuevo cargador de texturas. Cómo las texturas pueden compartirse
     * entre muchos Objetos 3D y no hace falta cargarlas 2 veces vamos a aplicar
     * el patrón sigleton
     */
    private TextureLoader() {
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,8},
                                            true,
                                            false,
                                            ComponentColorModel.TRANSLUCENT,
                                            DataBuffer.TYPE_BYTE);
                                            
        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                                            new int[] {8,8,8,0},
                                            false,
                                            false,
                                            ComponentColorModel.OPAQUE,
                                            DataBuffer.TYPE_BYTE);
    }
    
    /**
     * Convertir un buffer de imagen a textura
     *
     * @param bufferedImage La imagen a convertir en textura
     * @param texture Textura donde almacenar la información
     * @return Buffer con la información
     */
    @SuppressWarnings("rawtypes")
	private ByteBuffer convertImageData(BufferedImage bufferedImage, Texture texture) { 
        ByteBuffer imageBuffer = null; 
        WritableRaster raster;
        BufferedImage texImage;
        
        // Las dimensiones de las texturas han de ser multipos de 2
        int texWidth = get2Fold(bufferedImage.getWidth());
        int texHeight = get2Fold(bufferedImage.getHeight());

        // Se almacena la información en la textura
        texture.setTextureHeight(texHeight);
        texture.setTextureWidth(texWidth);
        
        // Crear un Raster que usará GL como origen de textura
        // Depende de si tiene o no canal alpha
        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable());
        }
            
        // Copiar la imagen
        Graphics2D g = (Graphics2D) texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        g.translate(0,texHeight);
        AffineTransform t = AffineTransform.getScaleInstance(1,-1);
        g.drawImage(bufferedImage,t,null);
        
        // Construir el buffer temporal para la imagen usado por el GL
        // para producir la textura
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 

        imageBuffer = ByteBuffer.allocateDirect(data.length); 
        imageBuffer.order(ByteOrder.nativeOrder()); 
        imageBuffer.put(data, 0, data.length); 
        imageBuffer.flip();
        
        return imageBuffer; 
    } 
    
    /**
     * Crea un buffer de enteros para almacenar enteros
     * - un método de utilidad
     *
     * @param Cantidad de enteros que contendra
     * @return IntBuffer creado
     */
    protected IntBuffer createIntBuffer(int size) {
      ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
      temp.order(ByteOrder.nativeOrder());

      return temp.asIntBuffer();
    }
    
    /**
     * Crear un nuevo identificador de textura
     *
     * @return Nuevo ID de textura
     */
    private int createTextureID() 
    { 
       IntBuffer tmp = createIntBuffer(1); 
       glGenTextures(tmp); 
       return tmp.get(0);
    } 
    
    /**
     * Obtiene la potencia de 2 más cercana al parametro de entrada
     * 
     * @param numero a aproximar
     * @return Potencia de 2
     */
    private int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    } 
    
    /**
     * Carga una textura
     *
     * @param resourceName El nombre del recurso a cargar
     * @return La textura cargada
     * @throws IOException Error al acceder al recurso
     */
    public Texture getTexture(String resourceName) throws IOException {
    	// Si la textura ya ha sigo cargada se recupera directamente
        Texture tex = (Texture) table.get(resourceName);
        
        if (tex != null) {
            return tex;
        }
        
        logger.info("Loading texture: "+resourceName);
        
        // Textura nueva: se carga y se almacena en la tabla de texturas 
        tex = getTexture(resourceName,
                         GL_TEXTURE_2D, // Textura de 2D
                         GL_RGBA,       // Modelo de color RGB con canal alpha
                         GL_LINEAR,     // Filtro de minimización
                         GL_NEAREST);   // Filtro de maximización
        
        table.put(resourceName,tex);
        
        return tex;
    }
    
    /**
     * Carga una textura en GL desde un fichero.
     *
     * @param resourceName Localización de la textura a cargar
     * @param target Tipo de textura GL contra el que cargar la textura
     * @param dstPixelFormat Formato del pixel en la ventana
     * @param minFilter Filtro de minimización
     * @param magFilter Filtro de maximización
     * @return La textura cargada
     * @throws IOException Error de acceso al recurso
     */
    public Texture getTexture(String resourceName, 
                              int target, 
                              int dstPixelFormat, 
                              int minFilter, 
                              int magFilter) throws IOException 
    { 
        int srcPixelFormat = 0;
        
        // Crear un ID de Textura 
        int textureID = createTextureID(); 
        Texture texture = new Texture(target, textureID); 
        
        // Enlazar la textura en GL
        glBindTexture(target, textureID); 
 
        // Carga de la imagen del disco y establecer el tamaño
        BufferedImage bufferedImage = loadImage(resourceName); 
        texture.setWidth(bufferedImage.getWidth());
        texture.setHeight(bufferedImage.getHeight());
        
        // Establecer el modelo de color dependiendo si tiene canal alpha
        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL_RGBA;
        } else {
            srcPixelFormat = GL_RGB;
        }

        // Convertir la imagen a un buffer de bytes
        ByteBuffer textureBuffer = convertImageData(bufferedImage, texture); 
        
        // Establece en Gl los filtros de cambio de tamaño
        if (target == GL_TEXTURE_2D) 
        { 
            glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter); 
            glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter); 
        } 
 
        // Generar la textura desde el buffer de bytes
        glTexImage2D(target, 
                      0, 
                      dstPixelFormat, 
                      get2Fold(bufferedImage.getWidth()), 
                      get2Fold(bufferedImage.getHeight()), 
                      0, 
                      srcPixelFormat, 
                      GL_UNSIGNED_BYTE, 
                      textureBuffer ); 
        
        return texture; 
    }
    
    /** 
     * Carga una imagen desde el recurso indicado
     * 
     * @param ref Localización del recurso a cargar
     * @return El buffer de la imagen cargada
     * @throws IOException Indica un error en la carga del recurso
     */
    private BufferedImage loadImage(String ref) throws IOException 
    { 
    	// Obtener la ruta (desde los directorios del classpath)
        URL url = TextureLoader.class.getClassLoader().getResource(ref);
        
        if (url == null) {
            throw new IOException("Cannot find: "+ref);
        }
        
        BufferedImage bufferedImage = ImageIO.read(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(ref))); 
 
        return bufferedImage;
    }    
}