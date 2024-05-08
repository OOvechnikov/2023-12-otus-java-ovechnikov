package ru.otus;

import com.google.gson.GsonBuilder;
import lombok.val;
import org.eclipse.jetty.server.Server;
import org.hibernate.cfg.Configuration;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.db.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.db.repository.DataTemplateHibernate;
import ru.otus.db.repository.HibernateUtils;
import ru.otus.db.sessionmanager.TransactionManagerHibernate;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.DbServiceClientImpl;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthServiceImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final String TEMPLATES_DIR = "/templates/";
    private static final int WEB_SERVER_PORT = 8080;

    public static void main(String[] args) throws Exception {
        val configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        val dbUrl = configuration.getProperty("hibernate.connection.url");
        val dbUserName = configuration.getProperty("hibernate.connection.username");
        val dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        val sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        val transactionManager = new TransactionManagerHibernate(sessionFactory);
        val clientTemplate = new DataTemplateHibernate<>(Client.class);
        val dbServiceClient = new DbServiceClientImpl(clientTemplate, transactionManager);

        val userDao = new InMemoryUserDao();
        val gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().setPrettyPrinting().create();
        val templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        val authService = new UserAuthServiceImpl(userDao);

        val usersWebServer = new UsersWebServerWithFilterBasedSecurity(dbServiceClient,
                gson, new Server(WEB_SERVER_PORT), templateProcessor, authService);

        usersWebServer.start();
        usersWebServer.join();
    }

}
