## Backend for the MIP portal

[![License](https://img.shields.io/badge/license-AGPL--3.0-blue.svg)](https://www.gnu.org/licenses/agpl-3.0.html)

## Usage in development environment

* Build and run the project (including clean target): `./go.sh`
* Build the project (including clean target): `./build.sh`
* Run the project: `./run.sh`
* Show live logs: `./log.sh` (CTRL+C to quit)
* Stop and remove the running container: `./halt.sh`
* Clean Maven cache, etc: `./clean.sh`

## Usage in building environment

* Build a versioned image: `./captain_build.sh` (only if `$WORKSPACE=$(pwd)`) or `export WORKSPACE=<path-to-workspace> && captain build`
* Build and test an image: `./captain_test.sh` or `captain test`
* Run the latest image with a non-persistent postgres (only use this for testing purpose): `./captain_run.sh`

## Deployment

* See here: https://hub.docker.com/r/hbpmip/portal-backend/

## Generate PDF API documentation on build

Uncomment the following line in src/docker/build/build-in-docker.sh :
`mvn swagger2markup:convertSwagger2markup asciidoctor:process-asciidoc`

## Tips

* Do not forget to set up the CLIENT_SECRET environment variable when deploying.

## License

Copyright © 2016 LREN CHUV

Licensed under the GNU Affero General Public License, Version 3.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [https://www.gnu.org/licenses/agpl-3.0.html](https://www.gnu.org/licenses/agpl-3.0.html)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
