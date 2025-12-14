package com.dsa.core;

public class Track {
    private String id;
    private String name;
    private String artist;
    private String genre;
    private double energy; // 0.0 to 1.0 (chill -> intense)
    private double valence; // 0.0 to 1.0 (sad -> happy)
    private String imageUrl;

    public Track(String id, String name, String artist, String genre, double energy, double valence, String imageUrl) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.genre = genre;
        this.energy = energy;
        this.valence = valence;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public double getEnergy() {
        return energy;
    }

    public double getValence() {
        return valence;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setters for audio features (updated after fetching from Spotify)
    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void setValence(double valence) {
        this.valence = valence;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // Override equals and hashCode for deduplication (based on ID)
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Track track = (Track) obj;
        return id.equals(track.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return name + " - " + artist;
    }
}
