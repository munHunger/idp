package se.munhunger.idp;

import org.glassfish.jersey.server.ResourceConfig;
import se.munhunger.idp.injection.Binder;

public class Startup extends ResourceConfig
{
    public Startup() {
        register(new Binder());
        packages("true", "se.munhunger.idp");
    }
}
