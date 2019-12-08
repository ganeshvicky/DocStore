package com.gv.docstore;

public class Dataset {
    private String img;
    private String imgname;

    public Dataset(String img, String imgname) {
        this.img = img;
        this.imgname = imgname;
    }

    public Dataset() {

    }

    public String getImg() {
        return img;
    }

    public String getImgname() {
        return imgname;
    }
}
