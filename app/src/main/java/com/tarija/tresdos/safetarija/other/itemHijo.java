package com.tarija.tresdos.safetarija.other;

/**
 * Created by tresdos on 05-10-17.
 */

public class itemHijo {
    private String name;
    private String activo;
    private int image;
    private String key;

    public itemHijo(){

    }
    public  itemHijo(String name, String Activo, int image, String key){
        this.name = name;
        this.activo = Activo;
        this.image = image;
        this.setKey(key);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
