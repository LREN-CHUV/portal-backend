/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.mock;

import org.hbp.mip.model.Article;
import org.hbp.mip.model.User;

import java.util.Date;
import java.util.LinkedList;

public class ArticleMock extends Article {
    public ArticleMock(int id) {
        Date currentDate = new Date();
        User currentUser = new UserMock(1);
        this.setId(id);
        switch (id) {
            case 1:
                this.setTitle("Test1");
                this.setStatus("published");
                this.setSlug("test1");
                this.setPublishedAt(currentDate);
                this.setUpdatedAt(currentDate);
                this.setTags(new LinkedList<>());
                this.setAbst("This is a first test article.");
                this.setContent("<!DOCTYPE html><html><head></head><body><p>This is the content of my first test article.</p></body></html>");
                this.setCreatedAt(currentDate);
                this.setCreatedBy(currentUser);
                this.setUpdatedBy(currentUser);
                break;
            case 2:
                this.setTitle("Test2");
                this.setStatus("published");
                this.setSlug("test2");
                this.setPublishedAt(currentDate);
                this.setUpdatedAt(currentDate);
                this.setTags(new LinkedList<>());
                this.setAbst("This is a second test article.");
                this.setContent("<!DOCTYPE html><html><head></head><body><p>This is the content of my second test article.</p></body></html>");
                this.setCreatedAt(currentDate);
                this.setCreatedBy(currentUser);
                this.setUpdatedBy(currentUser);
                break;
        }
    }
}
