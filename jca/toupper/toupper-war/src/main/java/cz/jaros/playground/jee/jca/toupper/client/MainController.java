package cz.jaros.playground.jee.jca.toupper.client;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.jaros.playground.jee.jca.toupper.api.ToUpperConnection;
import cz.jaros.playground.jee.jca.toupper.api.ToUpperConnectionFactory;
import cz.jaros.playground.jee.jca.toupper.api.ToUpperException;

@Controller
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String CF_JNDI_NAME = "java:comp/env/eis/toupper-host";

    private ToUpperConnectionFactory connectionFactory = null;

    private ToUpperConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            try {
                logger.info("Looking up connection factory with JNDI name '" + CF_JNDI_NAME);
                Context ic = new InitialContext();
                Object factoryObj = ic.lookup(CF_JNDI_NAME);
                logger.info("Looked up connection factory: " + factoryObj);
                this.connectionFactory = (ToUpperConnectionFactory) factoryObj;
            } catch (NamingException e) {
                throw new IllegalStateException("Can not lookup connection factory from JNDI registry.", e);
            }
        }
        return connectionFactory;
    }

    @RequestMapping(value="input.html", method=RequestMethod.GET)
    public String renderInputForm() {
        logger.info("Rendering input form.");
        return "input";
    }

    @RequestMapping(value="input.html", method=RequestMethod.POST)
    public String processInput(String input, long timeout, Model model) {
        logger.info("Processing input {}.", input);
        ToUpperConnection connection = getConnectionFactory().getConnection();
        String output;
        try {
            output = connection.send(input, timeout);
            model.addAttribute("result", output);
        } catch (ToUpperException e) {
            throw new IllegalStateException("Error occurred.", e);
        }
        connection.close();
        return "input";
    }

}
