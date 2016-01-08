package org.hbp.mip.controllers;


import io.swagger.annotations.*;
import org.hbp.mip.model.Dataset;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/datasets", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/datasets", description = "the datasets API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class DatasetsApi {


    @ApiOperation(value = "Get a dataset", notes = "", response = Dataset.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "/{code}", produces = {"application/json"}, method = RequestMethod.GET)
    public ResponseEntity<Dataset> getADataset(
            @ApiParam(value = "code", required = true) @PathVariable("code") String code) throws NotFoundException {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Dataset where code= :code");
        query.setString("code", code);
        Dataset dataset = (Dataset) query.uniqueResult();
        session.getTransaction().commit();
        return new ResponseEntity<Dataset>(HttpStatus.OK).ok(dataset);
    }


}
