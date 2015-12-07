/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.model;

public class Tag {
    private Long id;
    private String name;

    public Tag() {
    }

    private Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
