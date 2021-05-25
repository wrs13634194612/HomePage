package com.example.mepositry;


/**
 * Created by weioule
 * on 2019/6/26.
 */
public class ProductBean {

    public ProductBean(String name, String price, String content, int imageResource) {
        this.name = name;
        this.price = price;
        this.content = content;
        this.imageResource = imageResource;
    }

    private String name;
    private String price;
    private String content;
    private int imageResource;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
