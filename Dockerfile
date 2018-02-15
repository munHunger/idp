FROM munhunger/tomcat:latest

ADD build/idp-0.1.war /opt/tomcat/webapps/idp.war

CMD /opt/tomcat/bin/catalina.sh run