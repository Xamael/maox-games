package org.maox.graphics.textures;

import static org.lwjgl.opengl.GL11.*;

/**
 * Una textura para se enlazada dentro del LWJGL. Este objeto será
 * el responsable de hacer un seguimiento de la textura Open GL para
 * calcular el mapeo de las coordenadas de la imagen completa.
 * 
 * Cómo las texturas necesitan ser potencias de 2, la textura actual
 * puede ser considerablemente mayor que la imagen original y es
 * necesario ajustar el mapeo de coordnadas para que se ajuste. 
 *
 * @author Kevin Glass
 * @author Brian Matzon
 */
public class Texture {
    /** Tipo de textura de GL */
    private int target; 
    /** El ID de textura de GL */
    private int textureID;
    /** Altura de la imagen */
    private int height;
    /** Ancho de la imagen */
    private int width;
    /** Ancho de la textura */
    private int texWidth;
    /** Alto de la textura */
    private int texHeight;
    /** Ratio del ancho de la imagen y la textura */
    private float widthRatio;
    /** Ratio del alto de la imagen y la textura */
    private float heightRatio;
    
    /**
     * Crea una nueva texura
     *
     * @param target Tipo de textura
     * @param textureID ID de GL de la textura
     */
    public Texture(int target,int textureID) {
        this.target = target;
        this.textureID = textureID;
    }
    
    /**
     * Enlaza la textura al GL
     *
     * @param
     */
    public void bind() {
      glBindTexture(target, textureID); 
    }
    
    /**
     * Obtiene el alto fisco de la texura
     *
     * @return Alto fisco de la texura
     */
    public float getHeight() {
        return heightRatio;
    }
    
    /**
     * Obtiene el alto original de la imagen
     *
     * @return Alto original de la imagen
     */
    public int getImageHeight() {
        return height;
    }
    
    /** 
     * Obtiene el ancho original de la imagen
     *
     * @return Ancho original de la imagen
     */
    public int getImageWidth() {
        return width;
    }
    
    /**
     * Obtiene el ancho fisco de la texura
     *
     * @return Ancho fisco de la texura
     */
    public float getWidth() {
        return widthRatio;
    }
    
    /**
     * Establece el alto de la imagen
     *
     * @param height Alto de la imagen
     */
    public void setHeight(int height) {
        this.height = height;
        updateHeightRatio();
    }
    
    /**
     * Establece el ratio de la textura
     *
     * @param texHeight Alto de la texura
     */
    public void setTextureHeight(int texHeight) {
        this.texHeight = texHeight;
        updateHeightRatio();
    }
    
    /**
     * Establece el ancho de la texura
     *
     * @param texWidth Ancho de la texura
     */
    public void setTextureWidth(int texWidth) {
        this.texWidth = texWidth;
        updateWidthRatio();
    }
    
    /**
     * Establece el ancho de la imagen
     *
     * @param width Ancho de la imagen
     */
    public void setWidth(int width) {
        this.width = width;
        updateWidthRatio();
    }
    
    /**
     * Establece el alto de la textura. Actualizará el ratio también
     */
    private void updateHeightRatio() {
        if (texHeight != 0) {
            heightRatio = ((float) height)/texHeight;
        }
    }
    
    /**
     * Establece el ancho de la textura. Actualizará el ratio también
     */
    private void updateWidthRatio() {
        if (texWidth != 0) {
            widthRatio = ((float) width)/texWidth;
        }
    }
}