/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;


import io.swagger.annotations.*;
import org.hbp.mip.model.Dataset;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/datasets", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/datasets", description = "the datasets API")
public class DatasetsApi {

    @ApiOperation(value = "Get a dataset", response = Dataset.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public ResponseEntity<Dataset> getADataset(
            @ApiParam(value = "code", required = true) @PathVariable("code") String code
    )  {

        // Query DB
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Dataset dataset = null;
        try{
            session.beginTransaction();
            Query query = session.createQuery("from Dataset where code= :code");
            query.setString("code", code);
            dataset = (Dataset) query.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }        }

        return new ResponseEntity<Dataset>(HttpStatus.OK).ok(dataset);
    }


}
