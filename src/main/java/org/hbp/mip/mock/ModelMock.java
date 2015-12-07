/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.mock;

import org.hbp.mip.model.Model;


public class ModelMock extends Model {
    public ModelMock(int id) {
        switch (id) {
            case 1:
                this.setTitle("Model1");
                break;
        }
    }
}
