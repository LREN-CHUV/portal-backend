#!/usr/bin/env bash


# Define reference data

GROUPS_REF='{"code":"root","groups":[{"code":"tg1","label":"Test Group 1","groups":[{"code":"tg3","label":"Test Group 3","groups":[]}]},{"code":"tg2","label":"Test Group 2","groups":[{"code":"tg4","label":"Test Group 4","groups":[]}]}]}'

VARIABLES_REF='[{"code":"tv1","label":"Test Variable 1","type":"text","group":{"code":"tg3","label":"Test Group 3"},"isVariable":true},{"code":"tv2","label":"Test Variable 2","type":"integer","group":{"code":"tg4","label":"Test Group 4"},"isVariable":true},{"code":"tv3","label":"Test Variable 3","type":"real","group":{"code":"tg4","label":"Test Group 4"},"isVariable":true}]'

STATS_REF='{"users":1,"articles":0,"variables":3}'

VARIABLES_HIERARCHY_REF='{"code":"root","groups":[{"code":"tg1","label":"Test Group 1","groups":[{"code":"tg3","label":"Test Group 3","variables":[{"code":"tv1","label":"Test Variable 1","type":"text"}]}]},{"code":"tg2","label":"Test Group 2","groups":[{"code":"tg4","label":"Test Group 4","variables":[{"code":"tv2","label":"Test Variable 2","type":"integer"},{"code":"tv3","label":"Test Variable 3","type":"real"}]}]}]}'

REQUEST_BODY='{"variables":[{"code":"tv1"}],"groupings":[],"coVariables":[{"code":"tv2"},{"code":"tv3"}],"filters":""}'
REQUEST_REF='{"code":"DS1507582826852","date":1.507582826852E12,"variable":["tv1"],"grouping":[],"header":["tv2","tv3"],"data":{"tv1":["val1268","val459","val749","val1191","val394","val1980","val1446","val506","val1814","val358","val481","val1775","val621","val1610","val403","val1882","val379","val1979","val1521","val1894","val1053","val825","val14","val1395","val672","val952","val1508","val988","val237","val1950","val716","val1344","val418","val1916","val1367","val1381","val165","val871","val1488","val841","val142","val1725","val1604","val1013","val1796","val873","val1333","val126","val1185","val1254","val1074","val1747","val526","val533","val1436","val1400","val875","val1291","val974","val1398","val243","val869","val1540","val221","val1778","val1900","val442","val516","val532","val1252","val160","val1057","val1419","val1213","val1236","val301","val1260","val1189","val302","val1420","val1970","val831","val313","val1275","val648","val735","val901","val1823","val1082","val1996","val687","val1816","val556","val1345","val1693","val972","val1627","val141","val774","val1286","val806","val767","val772","val1837","val290","val1461","val860","val132","val872","val934","val576","val1411","val1302","val642","val350","val15","val1296","val1299","val1475","val1978","val1474","val1901","val554","val1182","val1025","val1779","val262","val916","val257","val566","val1477","val102","val1216","val426","val984","val1162","val315","val1547","val496","val22","val573","val130","val1437","val1479","val904","val815","val1056","val54","val1772","val925","val1466","val824","val1981","val1940","val487","val490","val1063","val1541","val417","val1415","val723","val1877","val1598","val623","val1875","val1472","val55","val1780","val1184","val1802","val663","val103","val228","val1822","val498","val557","val312","val1425","val1848","val1337","val1591","val1207","val330","val968","val1359","val1341","val36","val18","val1548","val80","val163","val1899","val27","val530","val1716","val398","val361","val253","val1821","val483"],"tv2":[1268.0,459.0,749.0,1191.0,394.0,1980.0,1446.0,506.0,1814.0,358.0,481.0,1775.0,621.0,1610.0,403.0,1882.0,379.0,1979.0,1521.0,1894.0,1053.0,825.0,14.0,1395.0,672.0,952.0,1508.0,988.0,237.0,1950.0,716.0,1344.0,418.0,1916.0,1367.0,1381.0,165.0,871.0,1488.0,841.0,142.0,1725.0,1604.0,1013.0,1796.0,873.0,1333.0,126.0,1185.0,1254.0,1074.0,1747.0,526.0,533.0,1436.0,1400.0,875.0,1291.0,974.0,1398.0,243.0,869.0,1540.0,221.0,1778.0,1900.0,442.0,516.0,532.0,1252.0,160.0,1057.0,1419.0,1213.0,1236.0,301.0,1260.0,1189.0,302.0,1420.0,1970.0,831.0,313.0,1275.0,648.0,735.0,901.0,1823.0,1082.0,1996.0,687.0,1816.0,556.0,1345.0,1693.0,972.0,1627.0,141.0,774.0,1286.0,806.0,767.0,772.0,1837.0,290.0,1461.0,860.0,132.0,872.0,934.0,576.0,1411.0,1302.0,642.0,350.0,15.0,1296.0,1299.0,1475.0,1978.0,1474.0,1901.0,554.0,1182.0,1025.0,1779.0,262.0,916.0,257.0,566.0,1477.0,102.0,1216.0,426.0,984.0,1162.0,315.0,1547.0,496.0,22.0,573.0,130.0,1437.0,1479.0,904.0,815.0,1056.0,54.0,1772.0,925.0,1466.0,824.0,1981.0,1940.0,487.0,490.0,1063.0,1541.0,417.0,1415.0,723.0,1877.0,1598.0,623.0,1875.0,1472.0,55.0,1780.0,1184.0,1802.0,663.0,103.0,228.0,1822.0,498.0,557.0,312.0,1425.0,1848.0,1337.0,1591.0,1207.0,330.0,968.0,1359.0,1341.0,36.0,18.0,1548.0,80.0,163.0,1899.0,27.0,530.0,1716.0,398.0,361.0,253.0,1821.0,483.0],"tv3":[126.8,45.9,74.9,119.1,39.4,198.0,144.6,50.6,181.4,35.8,48.1,177.5,62.1,161.0,40.3,188.2,37.9,197.9,152.1,189.4,105.3,82.5,1.4,139.5,67.2,95.2,150.8,98.8,23.7,195.0,71.6,134.4,41.8,191.6,136.7,138.1,16.5,87.1,148.8,84.1,14.2,172.5,160.4,101.3,179.6,87.3,133.3,12.6,118.5,125.4,107.4,174.7,52.6,53.3,143.6,140.0,87.5,129.1,97.4,139.8,24.3,86.9,154.0,22.1,177.8,190.0,44.2,51.6,53.2,125.2,16.0,105.7,141.9,121.3,123.6,30.1,126.0,118.9,30.2,142.0,197.0,83.1,31.3,127.5,64.8,73.5,90.1,182.3,108.2,199.6,68.7,181.6,55.6,134.5,169.3,97.2,162.7,14.1,77.4,128.6,80.6,76.7,77.2,183.7,29.0,146.1,86.0,13.2,87.2,93.4,57.6,141.1,130.2,64.2,35.0,1.5,129.6,129.9,147.5,197.8,147.4,190.1,55.4,118.2,102.5,177.9,26.2,91.6,25.7,56.6,147.7,10.2,121.6,42.6,98.4,116.2,31.5,154.7,49.6,2.2,57.3,13.0,143.7,147.9,90.4,81.5,105.6,5.4,177.2,92.5,146.6,82.4,198.1,194.0,48.7,49.0,106.3,154.1,41.7,141.5,72.3,187.7,159.8,62.3,187.5,147.2,5.5,178.0,118.4,180.2,66.3,10.3,22.8,182.2,49.8,55.7,31.2,142.5,184.8,133.7,159.1,120.7,33.0,96.8,135.9,134.1,3.6,1.8,154.8,8.0,16.3,189.9,2.7,53.0,171.6,39.8,36.1,25.3,182.1,48.3]}}'

ARTICLE_BODY='{"status":"closed","content":"test","title":"Test","abstract":"test"}'
ARTICLE_REF='[{"slug":"test","title":"Test","status":"closed","content":"test","createdAt":1474035349482,"createdBy":{"username":"anonymous","fullname":"anonymous","picture":"images/users/default_user.png","languages":[],"roles":[],"votedApps":[]},"tags":[],"abstract":"test"}]'

MODEL_BODY='{"slug":"test","title":"Test","valid":false,"createdAt":1474362803284,"query":{"variables":[{"code":"tv1"}],"filters":"","coVariables":[{"code":"tv2"},{"code":"tv3"}],"groupings":[]},"dataset":{"code":"XXXX","date":1234567,"header":["tv2","tv3"],"grouping":[],"variable":["tv1"]},"config":{"type":"line","height":480,"yAxisVariables":["tv2","tv3"],"xAxisVariable":null,"hasXAxis":true,"title":{"text":"Test"}},"createdBy":{"username":"anonymous","fullname":"anonymous","picture":"images/users/default_user.png","languages":[],"roles":[],"votedApps":[]}}'
MODEL_REF='[{"slug":"test","title":"Test","valid":false,"createdAt":1507583225000,"query":{"variables":[{"code":"tv1"}],"filters":"","coVariables":[{"code":"tv2"},{"code":"tv3"}],"groupings":[]},"dataset":{"code":"XXXX","date":1234000,"header":["tv2","tv3"],"grouping":[],"variable":["tv1"],"data":{"tv1":["val1268","val459","val749","val1191","val394","val1980","val1446","val506","val1814","val358","val481","val1775","val621","val1610","val403","val1882","val379","val1979","val1521","val1894","val1053","val825","val14","val1395","val672","val952","val1508","val988","val237","val1950","val716","val1344","val418","val1916","val1367","val1381","val165","val871","val1488","val841","val142","val1725","val1604","val1013","val1796","val873","val1333","val126","val1185","val1254","val1074","val1747","val526","val533","val1436","val1400","val875","val1291","val974","val1398","val243","val869","val1540","val221","val1778","val1900","val442","val516","val532","val1252","val160","val1057","val1419","val1213","val1236","val301","val1260","val1189","val302","val1420","val1970","val831","val313","val1275","val648","val735","val901","val1823","val1082","val1996","val687","val1816","val556","val1345","val1693","val972","val1627","val141","val774","val1286","val806","val767","val772","val1837","val290","val1461","val860","val132","val872","val934","val576","val1411","val1302","val642","val350","val15","val1296","val1299","val1475","val1978","val1474","val1901","val554","val1182","val1025","val1779","val262","val916","val257","val566","val1477","val102","val1216","val426","val984","val1162","val315","val1547","val496","val22","val573","val130","val1437","val1479","val904","val815","val1056","val54","val1772","val925","val1466","val824","val1981","val1940","val487","val490","val1063","val1541","val417","val1415","val723","val1877","val1598","val623","val1875","val1472","val55","val1780","val1184","val1802","val663","val103","val228","val1822","val498","val557","val312","val1425","val1848","val1337","val1591","val1207","val330","val968","val1359","val1341","val36","val18","val1548","val80","val163","val1899","val27","val530","val1716","val398","val361","val253","val1821","val483"],"tv2":[1268.0,459.0,749.0,1191.0,394.0,1980.0,1446.0,506.0,1814.0,358.0,481.0,1775.0,621.0,1610.0,403.0,1882.0,379.0,1979.0,1521.0,1894.0,1053.0,825.0,14.0,1395.0,672.0,952.0,1508.0,988.0,237.0,1950.0,716.0,1344.0,418.0,1916.0,1367.0,1381.0,165.0,871.0,1488.0,841.0,142.0,1725.0,1604.0,1013.0,1796.0,873.0,1333.0,126.0,1185.0,1254.0,1074.0,1747.0,526.0,533.0,1436.0,1400.0,875.0,1291.0,974.0,1398.0,243.0,869.0,1540.0,221.0,1778.0,1900.0,442.0,516.0,532.0,1252.0,160.0,1057.0,1419.0,1213.0,1236.0,301.0,1260.0,1189.0,302.0,1420.0,1970.0,831.0,313.0,1275.0,648.0,735.0,901.0,1823.0,1082.0,1996.0,687.0,1816.0,556.0,1345.0,1693.0,972.0,1627.0,141.0,774.0,1286.0,806.0,767.0,772.0,1837.0,290.0,1461.0,860.0,132.0,872.0,934.0,576.0,1411.0,1302.0,642.0,350.0,15.0,1296.0,1299.0,1475.0,1978.0,1474.0,1901.0,554.0,1182.0,1025.0,1779.0,262.0,916.0,257.0,566.0,1477.0,102.0,1216.0,426.0,984.0,1162.0,315.0,1547.0,496.0,22.0,573.0,130.0,1437.0,1479.0,904.0,815.0,1056.0,54.0,1772.0,925.0,1466.0,824.0,1981.0,1940.0,487.0,490.0,1063.0,1541.0,417.0,1415.0,723.0,1877.0,1598.0,623.0,1875.0,1472.0,55.0,1780.0,1184.0,1802.0,663.0,103.0,228.0,1822.0,498.0,557.0,312.0,1425.0,1848.0,1337.0,1591.0,1207.0,330.0,968.0,1359.0,1341.0,36.0,18.0,1548.0,80.0,163.0,1899.0,27.0,530.0,1716.0,398.0,361.0,253.0,1821.0,483.0],"tv3":[126.8,45.9,74.9,119.1,39.4,198.0,144.6,50.6,181.4,35.8,48.1,177.5,62.1,161.0,40.3,188.2,37.9,197.9,152.1,189.4,105.3,82.5,1.4,139.5,67.2,95.2,150.8,98.8,23.7,195.0,71.6,134.4,41.8,191.6,136.7,138.1,16.5,87.1,148.8,84.1,14.2,172.5,160.4,101.3,179.6,87.3,133.3,12.6,118.5,125.4,107.4,174.7,52.6,53.3,143.6,140.0,87.5,129.1,97.4,139.8,24.3,86.9,154.0,22.1,177.8,190.0,44.2,51.6,53.2,125.2,16.0,105.7,141.9,121.3,123.6,30.1,126.0,118.9,30.2,142.0,197.0,83.1,31.3,127.5,64.8,73.5,90.1,182.3,108.2,199.6,68.7,181.6,55.6,134.5,169.3,97.2,162.7,14.1,77.4,128.6,80.6,76.7,77.2,183.7,29.0,146.1,86.0,13.2,87.2,93.4,57.6,141.1,130.2,64.2,35.0,1.5,129.6,129.9,147.5,197.8,147.4,190.1,55.4,118.2,102.5,177.9,26.2,91.6,25.7,56.6,147.7,10.2,121.6,42.6,98.4,116.2,31.5,154.7,49.6,2.2,57.3,13.0,143.7,147.9,90.4,81.5,105.6,5.4,177.2,92.5,146.6,82.4,198.1,194.0,48.7,49.0,106.3,154.1,41.7,141.5,72.3,187.7,159.8,62.3,187.5,147.2,5.5,178.0,118.4,180.2,66.3,10.3,22.8,182.2,49.8,55.7,31.2,142.5,184.8,133.7,159.1,120.7,33.0,96.8,135.9,134.1,3.6,1.8,154.8,8.0,16.3,189.9,2.7,53.0,171.6,39.8,36.1,25.3,182.1,48.3]}},"config":{"type":"line","height":480,"yAxisVariables":["tv2","tv3"],"xAxisVariable":null,"hasXAxis":true,"title":{"text":"Test"}},"createdBy":{"username":"anonymous","fullname":"anonymous","picture":"images/users/default_user.png","languages":[],"roles":[],"votedApps":[]}}]'


# Get gateway IP

GATEWAY_IP=$(docker inspect backend-test | grep \"Gateway\":\ \" | sed 's/.*Gateway\":\ \"\([^-]*\)\",/\1/' | head -n 1)


# Test - GET groups

echo "Testing groups API..."
if [ "$(curl -s ${GATEWAY_IP}:65440/services/groups)" != "$GROUPS_REF" ]; then
  echo "Tests failed - failed to load groups"
  exit 1
fi


# Test - GET variables

echo "Testing variables API..."
if [ "$(curl -s ${GATEWAY_IP}:65440/services/variables)" != "$VARIABLES_REF" ]; then
  echo "Tests failed - failed to load variables"
  exit 1
fi


# Test - GET variables hierarchy

if [ "$(curl -s ${GATEWAY_IP}:65440/services/variables/hierarchy)" != "$VARIABLES_HIERARCHY_REF" ]; then
  echo "Tests failed - failed to load variables hierarchy"
  exit 1
fi


# Test - GET stats

echo "Testing stats API..."
if [ "$(curl -s ${GATEWAY_IP}:65440/services/stats)" != "$STATS_REF" ]; then
  echo "Tests failed - failed to load stats"
  exit 1
fi


# Test - POST requests

echo "Testing requests API..."
response=$(curl -s -H "Content-Type: application/json" -X POST -d ${REQUEST_BODY} ${GATEWAY_IP}:65440/services/queries/requests | sed "s/ \+//g")
if [ "${response:52}" != "${REQUEST_REF:52}" ]; then
  echo "Tests failed - failed to post requests"
  exit 1
fi


# Test - POST and GET an article

echo "Testing articles API..."
curl -s -H "Content-Type: application/json" -X POST -d ${ARTICLE_BODY} ${GATEWAY_IP}:65440/services/articles > /dev/null
response=$(curl -s ${GATEWAY_IP}:65440/services/articles | sed "s/\"createdAt\":[0-9]*,//g")
response_ref=$(echo "${ARTICLE_REF//\"createdAt\":*[0-9],/}")
if [ "${response}" != "${response_ref}" ]; then
  echo "Tests failed - failed to save/load article"
  exit 1
fi


# Test - POST and GET a model

echo "Testing models API..."
curl -s -H "Content-Type: application/json" -X POST -d ${MODEL_BODY} ${GATEWAY_IP}:65440/services/models > /dev/null
response=$(curl -s ${GATEWAY_IP}:65440/services/models | sed "s/\"createdAt\":[0-9]*,//g" | sed "s/\"date\":[0-9]*,//g" | sed "s/ \+//g")
response_ref=$(echo "${MODEL_REF}" | sed "s/\"createdAt\":[0-9]*,//g" | sed "s/\"date\":[0-9]*,//g")
if [ "${response}" != "${response_ref}" ]; then
  echo "Tests failed - failed to save/load model"
  exit 1
fi


echo "All tests successfully passed"

exit 0
