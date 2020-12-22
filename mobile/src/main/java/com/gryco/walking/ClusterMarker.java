package com.gryco.walking;

import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.model.LatLng;

/**
 * Created by Gabrielius on 2018.12.28.
 */

public class ClusterMarker implements ClusterItem {

    private com.google.android.gms.maps.model.LatLng position;
    private String title;
    private String snippet;
    private int iconPicture;
    private int insidePicture;
    private String id;


    public ClusterMarker(com.google.android.gms.maps.model.LatLng position, String title, String snippet, int iconPicture, int insidePicture, String id) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.iconPicture = iconPicture;
        this.insidePicture = insidePicture;
        this.id = id;
    }

    @Override
    public com.google.android.gms.maps.model.LatLng getPosition() {
        return position;
    }

    public void setPosition(com.google.android.gms.maps.model.LatLng position) {
        this.position = position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public int getIconPicture() {
        return iconPicture;
    }

    public void setIconPicture(int iconPicture) {
        this.iconPicture = iconPicture;
    }


    public int getInsidePicture() {
        return insidePicture;
    }

    public void setInsidePicture(int insidePicture) {
        this.insidePicture = insidePicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
