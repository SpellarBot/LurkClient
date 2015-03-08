package com.lcsc.cs.lurkclient.states;

import com.lcsc.cs.lurkclient.protocol.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Student on 2/24/2015.
 */
public class LoginForm implements StateInterface {
    static Logger       logger = Logger.getLogger(LoginForm.class);

    private boolean     endProgram;
    private boolean     finished;
    private State       nextState;

    private Messenger   messenger;

    public LoginForm() {
        this.endProgram = false;
        this.finished   = false;
    }

    //There shouldn't be any parameters for this state.
    public void init(Map<String,String> params, Messenger messenger) {
        this.messenger = messenger;

        //messenger.registerListener();
    }

    public JPanel createState() {
        JPanel panel            = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        JLabel title    = new JLabel("Login");

        Font oldFont    = title.getFont();
        Font newFont    = new Font(oldFont.getFontName(), Font.BOLD, 36);
        title.setFont(newFont);

        c               = new GridBagConstraints();
        c.anchor        = GridBagConstraints.NORTH;
        c.gridy         = 0;
        panel.add(title, c);

        JLabel name     = new JLabel("Player Name");

        oldFont         = name.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 24);
        name.setFont(newFont);

        c               = new GridBagConstraints();
        c.anchor        = GridBagConstraints.WEST;
        c.insets        = new Insets(20,0,0,0);
        c.gridy         = 1;
        panel.add(name, c);

        final JTextField nameTextField    = new JTextField(25);

        oldFont         = nameTextField.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        nameTextField.setFont(newFont);

        c               = new GridBagConstraints();
        c.gridwidth     = GridBagConstraints.REMAINDER;
        c.fill          = GridBagConstraints.HORIZONTAL;
        c.gridy         = 2;
        panel.add(nameTextField, c);

        this.messenger.registerListener(new ResponseListener() {
            @Override
            public void notify(Response response) {
                LoginForm.this.handleLogin(response);
            }
        });


        JButton connectBtn  = new JButton("Login as Player");

        oldFont         = connectBtn.getFont();
        newFont         = new Font(oldFont.getFontName(), Font.PLAIN, 20);
        connectBtn.setFont(newFont);

        //This handles the connecting to the server using the entered host and port.
        connectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String name = nameTextField.getText();
                Command cmd = new Command(CommandType.CONNECT, name);
                LoginForm.this.messenger.sendMessage(cmd);
            }
        });

        c               = new GridBagConstraints();
        c.insets        = new Insets(20,0,0,0);
        c.gridy         = 5;
        panel.add(connectBtn, c);

        return panel;
    }

    private synchronized void handleLogin(Response response) {
        System.out.println("Shut up I'm handling it!");
    }

    public boolean run() {
        logger.debug("Running the Login state!");

        while (!this.finished) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        messenger.clearListeners();

        return this.endProgram;
    }

    public State getNextState() {
        return this.nextState;
    }

    public Map<String,String> getNextStateParams() {
        Map<String, String> params = new HashMap<String, String>();

        return params;
    }

    public void cleanUp() {
        this.messenger.disconnect();
        this.endProgram = true;
        this.finished = true;
    }
}
