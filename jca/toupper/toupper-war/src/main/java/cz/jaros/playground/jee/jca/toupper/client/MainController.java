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

    private ToUpperConnectionFactory connectionFactory;

    private ToUpperConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            try {
                Context ic = new InitialContext();
                Object factoryObj = ic.lookup("eis/toupper-host");
                logger.info("=== factoryObj: " + factoryObj);
                connectionFactory = (ToUpperConnectionFactory) factoryObj;
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
    public String processInput(String input, Model model) {
        logger.info("Processing input {}.", input);
        ToUpperConnection connection = getConnectionFactory().getConnection();
        String output;
        try {
            output = connection.send(input);
            model.addAttribute("result", output);
        } catch (ToUpperException e) {
            throw new IllegalStateException("Error occurred.", e);
        }
        connection.close();
        return "input";
    }

}
