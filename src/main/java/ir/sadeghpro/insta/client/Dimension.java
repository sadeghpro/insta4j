package ir.sadeghpro.insta.client;

public class Dimension {
    private int width;
    private int height;

    public Dimension(int width, int height){
        this.width = width;
        this.height = height;
    }

    public Dimension(){

    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
