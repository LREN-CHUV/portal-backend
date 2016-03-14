/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hbp.mip.model.Group;
import org.hbp.mip.utils.HibernateUtil;
import org.hibernate.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/groups", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/groups", description = "the groups API")
public class GroupsApi {

    @ApiOperation(value = "Get the root group (containing all subgroups)", response = Group.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Group> getTheRootGroup()  {

        String rootCode = "root";

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Group group = null;
        try{
            session.beginTransaction();
            group = (Group) session
                    .createQuery("FROM Group WHERE code= :code")
                    .setString("code", rootCode)
                    .uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e)
        {
            if(session.getTransaction() != null)
            {
                session.getTransaction().rollback();
            }
        }

        return ResponseEntity.ok(group);
    }


}
