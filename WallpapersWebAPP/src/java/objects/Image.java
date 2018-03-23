/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Pacho
 */
@Entity
@Table(name = "image")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Image.findAll", query = "SELECT i FROM Image i")
    , @NamedQuery(name = "Image.findByImgId", query = "SELECT i FROM Image i WHERE i.imgId = :imgId")
    , @NamedQuery(name = "Image.findByImgLink", query = "SELECT i FROM Image i WHERE i.imgLink = :imgLink")
    , @NamedQuery(name = "Image.findByImgHeigth", query = "SELECT i FROM Image i WHERE i.imgHeigth = :imgHeigth")
    , @NamedQuery(name = "Image.findByImgWidth", query = "SELECT i FROM Image i WHERE i.imgWidth = :imgWidth")
    , @NamedQuery(name = "Image.findByImgRatio", query = "SELECT i FROM Image i WHERE i.imgRatio = :imgRatio")})
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "img_id")
    private Integer imgId;
    @Basic(optional = false)
    @Column(name = "img_link")
    private String imgLink;
    @Basic(optional = false)
    @Column(name = "img_heigth")
    private int imgHeigth;
    @Basic(optional = false)
    @Column(name = "img_width")
    private int imgWidth;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "img_ratio")
    private Float imgRatio;

    public Image() {
    }

    public Image(Integer imgId) {
        this.imgId = imgId;
    }

    public Image( String imgLink, int imgHeigth, int imgWidth, float imgRatio) {
        
        this.imgLink = imgLink;
        this.imgHeigth = imgHeigth;
        this.imgWidth = imgWidth;
        this.imgRatio = imgRatio;
    }

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public int getImgHeigth() {
        return imgHeigth;
    }

    public void setImgHeigth(int imgHeigth) {
        this.imgHeigth = imgHeigth;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public Float getImgRatio() {
        return imgRatio;
    }

    public void setImgRatio(Float imgRatio) {
        this.imgRatio = imgRatio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (imgId != null ? imgId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Image)) {
            return false;
        }
        Image other = (Image) object;
        if ((this.imgId == null && other.imgId != null) || (this.imgId != null && !this.imgId.equals(other.imgId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "objects.Image[ imgId=" + imgId + " ]";
    }

}
