/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;

/**
 *
 * @author peter
 */
public class Location {

    private String id;
    private String name;
    private boolean hasPublicPage;
    private double lat;
    private double lng;
    private String slug;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasPublicPage() {
        return hasPublicPage;
    }

    public void setHasPublicPage(boolean hasPublicPage) {
        this.hasPublicPage = hasPublicPage;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}
