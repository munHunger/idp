package se.munhunger.idp.injection;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import se.munhunger.idp.dao.ClientDAO;
import se.munhunger.idp.dao.UserDAO;
import se.munhunger.idp.services.ClientService;
import se.munhunger.idp.services.UserService;

public class Binder extends AbstractBinder{
    @Override
    protected void configure() {
        bind(UserDAO.class).to(UserDAO.class);
        bind(UserService.class).to(UserService.class);
        bind(ClientDAO.class).to(ClientDAO.class);
        bind(ClientService.class).to(ClientService.class);
    }
}
