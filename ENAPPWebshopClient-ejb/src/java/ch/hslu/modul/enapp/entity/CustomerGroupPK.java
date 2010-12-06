/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hslu.modul.enapp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author berdir
 */
@Embeddable
public class CustomerGroupPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "groupname")
    private String groupname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "username")
    private String username;

    public CustomerGroupPK() {
    }

    public CustomerGroupPK(String groupname, String username) {
        this.groupname = groupname;
        this.username = username;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupname != null ? groupname.hashCode() : 0);
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerGroupPK)) {
            return false;
        }
        CustomerGroupPK other = (CustomerGroupPK) object;
        if ((this.groupname == null && other.groupname != null) || (this.groupname != null && !this.groupname.equals(other.groupname))) {
            return false;
        }
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ch.hslu.modul.enapp.entity.CustomerGroupPK[ groupname=" + groupname + ", username=" + username + " ]";
    }

}
