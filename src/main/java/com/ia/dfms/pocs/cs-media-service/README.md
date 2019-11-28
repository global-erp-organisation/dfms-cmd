<sup>Master:</sup>[![Master Build Status](https://ewe.builds.sb.karmalab.net/buildStatus/icon?job=cs-media-service-master)](https://ewe.builds.sb.karmalab.net/job/cs-media-service-master/)
<sup>All:</sup>[![All Build Status](https://ewe.builds.sb.karmalab.net/buildStatus/icon?job=cs-media-service-all)](https://ewe.builds.sb.karmalab.net/job/cs-media-service-all/)

## Table of Contents

* [Environments](#environments)
* [Tools](#tools)
* [Primer](#primer)



## Environments<a id="environments"/>

| Environment | Endpoints | Resources |
|-------------|-----------|-----------|
| Test | [us-west-2](http://cs-media-service.us-west-2.test.expedia.com) | [deploy](https://trinity.tools.expedia.com/job/ewetest_deploy-docker-ecs/), [release](https://trinity.tools.expedia.com/job/ewetest_release-docker-ecs/build) |
| Integration | [us-west-2](http://cs-media-service.us-west-2.int.expedia.com) [cname](http://cs-media-service.ewetest.expedia.com) | [deploy](https://trinity.tools.expedia.com/job/ewetest-int_deploy-docker-ecs/build), [release](https://trinity.tools.expedia.com/job/ewetest-int_release-docker-ecs/build) |
| Stress | [us-west-2](http://cs-media-service.us-west-2.stress.expedia.com) |  [deploy](https://trinity.tools.expedia.com/job/ewetest-stress_deploy-docker-ecs/build), [release](https://trinity.tools.expedia.com/job/ewetest-stress_release-docker-ecs/build) |
| Prod PCI | [us-west-1](http://cs-media-service.us-west-1.prod-p.expedia.com) [cname](http://cs-media-service.prod-p.expedia.com) | [deploy](https://trinity.prod-p.expedia.com/job/eweprod-e_deploy-docker-ecs/build), [release](https://trinity.prod-p.expedia.com/job/eweprod-e_release-docker-ecs/build) |


### Tools<a id="tools"/>

Primer: https://primer.tools.expedia.com/dashboard/cs-media-service

Gru: http://gru.tools.expedia.com/apps/cs-media-service

Banzai: http://banzai.tools.expedia.com/#/workflow/cs-media-service

Splunk:
[Test](https://splunk.us-west-2.test.expedia.com/en-US/app/search/flashtimeline?q=search%20index=app%20sourcetype%3Dcs-media-service*%20earliest%3D-60m)
, [Integration](https://splunk.us-west-2.int.expedia.com/en-US/app/search/flashtimeline?q=search%20index=app%20sourcetype%3Dcs-media-service*%20earliest%3D-60m)
, [Production](https://splunk.us-west-1.prod.expedia.com/en-US/app/search/flashtimeline?q=search%20index=app%20sourcetype%3Dcs-media-service*%20earliest%3D-60m)
, [Production PCI](https://splunk.us-west-2.prod-p.expedia.com/en-US/app/search/flashtimeline?q=search%20index=app%20sourcetype%3Dcs-media-service*%20earliest%3D-60m)

Metrics:
[Test](http://hubble.idx.expedmz.com/templates/app.html?nodes=*&env_name=ewetest&title=Node\(s\)%20of%20cs-media-service&role=cs-media-service&app_name=webapp&)
, [Integration](http://hubble.idx.expedmz.com/templates/app.html?nodes=*&env_name=ewetest-int&title=Node\(s\)%20of%20cs-media-service&role=cs-media-service&app_name=webapp&)
, [Production](http://hubble.idx.expedmz.com/templates/app.html?nodes=*&env_name=eweprod&title=Node\(s\)%20of%20cs-media-service&role=cs-media-service&app_name=webapp&)


## Primer<a id="primer"/>

Primer [home](https://primer.tools.expedia.com/).

### Documentation

[AWS Access](https://confluence/x/agMWK)

[AWS - SelfService Jenkins Jobs](https://confluence/display/POS/AWS+%3A+SelfService+Jenkins+Jobs)

[Developing Primer Applications with Docker on Amazon ECS](https://confluence/x/bgI3JQ)

[Investigating Application Issues on Amazon ECS](https://confluence/x/0acMJ)

[ECS Canary / Blue-Green Release process](https://confluence/x/oybZIw)

[Forum](https://forum.tools.expedia.com/category/18/cloud?loggedin)

[Vault: AWS Dynamic secrets and Secure secret storage](https://confluence/x/E2PwK)



=============

* Make sure you have Java 1.8
* Make sure you have maven 3.1.1 or higher

NOTE: If you do not have Expedia maven development setup, copy the settings.xml to ~/.m2/ folder

=============

From your IDE, just compile your project.

```
mvn clean install
```

=============

From your IDE, right-click on the "Starter" class at the root of your Java package hierarchy, and run it directly. 
You should also be able to debug it as easily.

You must add the following line as VM option:
    -Dapplication.home=. -Dproject.name=cs-media-service -Dspring.profiles.active=dev -Dapplication.environment=dev



Alternately, you may configure your IDE to run the following mvn target to run the application

```
mvn spring-boot:run
```

To run the application from command line, you may run one of the following commands after building your application

```
mvn spring-boot:run

mvn exec:exec
```

Open a browser and hit http://localhost:8080/ for service spec or http://localhost:8080/service/hello for sample API


=============


For OS X setup instructions, see: [https://ewegithub.sb.karmalab.net/EWE/docker](https://ewegithub.sb.karmalab.net/EWE/docker)


```
mvn -DchangeNumber=$(git rev-parse HEAD) -DbuildBranch=origin/master clean install
```

```
docker build .
```


```
docker run -e "APP_NAME=cs-media-service" -e "EXPEDIA_ENVIRONMENT=dev" -e "ACTIVE_VERSION=$(git rev-parse HEAD)" -p 8080:8080 $(docker images -q | head -1)
```

Open a browser and hit [http://LOCAL_DOCKER_IP:8080/](http://LOCAL_DOCKER_IP:8080/) (e.g. [http://192.168.99.100:8080](http://192.168.99.100:8080))


=============
Please see [here](http://docs.spring.io/spring-boot/docs/1.1.9.RELEASE/reference/html/common-application-properties.html) for details 

=============
