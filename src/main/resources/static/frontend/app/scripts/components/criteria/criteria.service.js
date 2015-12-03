/**
 * Created by Michael DESIGAUD on 12/08/2015.
 */

'use strict';

angular.module('chuvApp.components.criteria').factory('Criteria',[function(){
        //TODO will be a resource
        return{
            getMockVariables:function(){
                return{values:[
                    {id:1,label:"Ventricule",code:"ventricule"},
                    {id:2,label:"Brain stem",code:"brain_stem"},
                    {id:3,label:"Cerebellum",code:"cerebellum"},
                    {id:4,label:"Global",code:"global"},
                    {id:5,label:"OpticChiasm",code:"optichiasm"},
                    {id:6,label:"Ventricule 2",code:"ventricule_2"},
                    {id:7,label:"Brain stem 2",code:"brain_stem_2"},
                    {id:8,label:"Global 2",code:"global_2"},
                    {id:9,label:"OpticChiasm 2",code:"opticchiasm_2"},
                    {id:10,label:"Ventricule 3",code:"ventricule_3"},
                    {id:11,label:"Brain stem 3",code:"brain_stem_3"},
                    {id:12,label:"Cerebellum 3",code:"cerebellum_3"},
                    {id:13,label:"Global 3",code:"global_3"},
                    {id:14,label:"OpticChiasm 3",code:"opticchiasm_3"},
                    {id:15,label:"Ventricule 4",code:"ventricule_4"},
                    {id:16,label:"Brain stem 4",code:"brain_stem_4"},
                    {id:17,label:"Cerebellum 4",code:"cerebellum_4"},
                    {id:18,label:"Global 4",code:"global_4"},
                    {id:19,label:"OpticChiasm 4",code:"opticchiasm_4"}]};
            },
            get:function(){
                return {
                        variable:[this.getMockVariables(),this.getMockVariables(),this.getMockVariables()],
                        grouping:[this.getMockVariables(),this.getMockVariables(),this.getMockVariables()],
                        covariable:[this.getMockVariables(),this.getMockVariables(),this.getMockVariables(),this.getMockVariables(),this.getMockVariables(),this.getMockVariables()],
                        filters:[this.getMockVariables(),this.getMockVariables(),this.getMockVariables()]
                    };
            }
        }
    }]
);