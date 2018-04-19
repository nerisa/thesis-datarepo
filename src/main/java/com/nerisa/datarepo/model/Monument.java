package com.nerisa.datarepo.model;

import com.nerisa.datarepo.utils.Constant;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nerisa on 2/14/18.
 */
@XmlRootElement
public class Monument {

    private long id;
    private String name;
    private String creator;
    private String desc;
    private double latitude;
    private double longitude;
    private String monumentPhoto;
    private List<TemperatureData> temperatures;
    private List<NoiseData> noiseProfiles;
    private List<Warning> warnings;
    private List<Post> posts;
    private User custodian;
    private String reference;

    public Monument(){}

    public Monument(Long id, String name, String creator, String desc, String monumentPhoto, double latitude, double longitude){
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.desc = desc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.monumentPhoto = monumentPhoto;
    }

    public Monument(String name, String creator, String desc, String monumentPhoto, double latitude, double longitude){
        this.name = name;
        this.creator = creator;
        this.desc = desc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.monumentPhoto = monumentPhoto;
    }

    @XmlElement
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @XmlElement
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @XmlElement
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @XmlElement
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @XmlElement
    public String getMonumentPhoto() {
        return monumentPhoto;
    }

    public void setMonumentPhoto(String monumentPhoto) {
        this.monumentPhoto = monumentPhoto;
    }

    @XmlElement
    public List<NoiseData> getNoiseProfiles() {
        if(this.noiseProfiles == null){
            noiseProfiles = new ArrayList<NoiseData>();
        }
        return noiseProfiles;
    }

    public void setNoiseProfiles(List<NoiseData> noiseProfiles) { this.noiseProfiles = noiseProfiles; }

    public void addNoise(NoiseData noiseData){
        if(this.noiseProfiles == null){
            noiseProfiles = new ArrayList<NoiseData>();
        }
        this.noiseProfiles.add(noiseData);
    }

    @XmlElement
    public List<TemperatureData> getTemperatures() {
        if(temperatures == null){
            temperatures = new ArrayList<TemperatureData>();
        }
        return temperatures;
    }

    public void setTemperatures(List<TemperatureData> temperatures) {
        this.temperatures = temperatures;
    }

    public void addTemperature(TemperatureData temperatureData){
        if(temperatures == null){
            temperatures = new ArrayList<TemperatureData>();
        }
        this.temperatures.add(temperatureData);
    }

    @XmlElement
    public List<Warning> getWarnings() {
        if(warnings == null){
            warnings = new ArrayList<Warning>();
        }
        return warnings;
    }

    public void setWarnings(List<Warning> warnings) {
        this.warnings = warnings;
    }

    public void addWarning(Warning warning){
        if(this.warnings == null){
            this.warnings = new ArrayList<Warning>();
        }
        this.warnings.add(warning);
    }

    @XmlElement
    public List<Post> getPosts() {
        if(this.posts == null){
            this.posts = new ArrayList<Post>();
        }
        return  posts;
    }

    public void setPosts(List<Post> posts) { this.posts = posts; }

    public void addPost(Post post){
        if(this.posts == null){
            this.posts = new ArrayList<Post>();
        }
        this.posts.add(post);
    }

    public String getMonumentUri(){
        return Constant.BASE_URI + "/" + getId();
    }

    public static String createMonumentUri(Long id){
        return Constant.BASE_URI + "/" + id;
    }

    @XmlElement
    public User getCustodian() {
        return custodian;
    }

    public void setCustodian(User custodian) {
        this.custodian = custodian;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
