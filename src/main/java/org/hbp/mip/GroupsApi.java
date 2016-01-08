package org.hbp.mip;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hbp.mip.model.Group;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/groups", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/groups", description = "the groups API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-01-07T07:38:20.227Z")
public class GroupsApi {


    @ApiOperation(value = "Get the root group (containing all subgroups)", notes = "", response = Group.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")})
    @RequestMapping(value = "", produces = {"application/json"}, method = RequestMethod.GET)
    public ResponseEntity<Group> getTheRootGroup() throws NotFoundException {
        String rootCode = "root";
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query = session.createQuery("from Group where code= :code");
        query.setString("code", rootCode);
        Group group = (Group) query.uniqueResult();
        session.getTransaction().commit();
        return new ResponseEntity<Group>(HttpStatus.OK).ok(group);
    }


}
