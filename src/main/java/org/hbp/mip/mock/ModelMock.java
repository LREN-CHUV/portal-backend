package org.hbp.mip.mock;

import org.hbp.mip.model.Model;

/**
 * Created by mirco on 04.12.15.
 */
public class ModelMock extends Model {
    public ModelMock(int id) {
        this.setId(id);
        switch (id) {
            case 1:
                this.setTitle("Model1");
                break;
        }
    }
}
