FROM munhunger/tomcat:latest

ADD build/*.war /opt/tomcat/webapps/idp.war

CMD /opt/tomcat/bin/catalina.sh run