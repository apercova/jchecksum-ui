package com.github.apercova.jchecksumui.controller;

import com.github.apercova.jchecksum.JCheckSum;
import com.github.apercova.jchecksumui.ui.MainView;
import com.github.apercova.quickcli.Command;
import com.github.apercova.quickcli.CommandFactory;
import com.github.apercova.quickcli.exception.CLIArgumentException;
import com.github.apercova.quickcli.exception.ExecutionException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;

/**
 * Main view controller class
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0.1904
 *
 */
public class MainViewController implements ActionListener, WindowStateListener {

    public static final String MESSAGE_BOUNDLE = "com.github.apercova.jchecksumui.l18n.messages";
    public final static String DEF_LOGGER_CONF = "/com/github/apercova/jchecksumui/logging/config.properties";
    public final static String DEF_CHARSET = "UTF-8";
    public final static String CMD_FILEC_CK = "100";
    public final static String CMD_FILEC = "101";
    public final static String CMD_ENCODE = "110";
    public final static String CMD_DIGEST = "111";
    public final static String CMD_COPY_SOURCE = "120";
    public final static String CMD_CLEAR_SOURCE = "121";
    public final static String CMD_COPY_RESULT = "130";
    public final static String CMD_CLEAR_RESULT = "131";

    private final MainView view;

    public MainViewController(MainView view) {
        this.view = view;
        this.configLogging();
        this.initAlgs();
        this.initEncoders();
        this.initCharsets();
    }

    /**
     * Configure logging options
     */
    private void configLogging() {
        try {
            LogManager.getLogManager().readConfiguration(
                    this.getClass().getResourceAsStream(DEF_LOGGER_CONF));
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Initialize algorithm list.
     */
    private void initAlgs() {
        try {
            String[] args = {"-la"};
            Writer writer = new StringWriter();
            Command<Void> command = CommandFactory.create(args, JCheckSum.class, writer);
            List<String> elements = new ArrayList<String>();
            command.execute();
            Scanner scanner = new Scanner(writer.toString());
            int ln = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                ln++;
                if (ln > 2) {
                    elements.add(line);
                }
            }
            this.view.getCmbAlg().setModel(new DefaultComboBoxModel(elements.toArray()));
        } catch (CLIArgumentException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Initialize charset list.
     */
    private void initCharsets() {
        try {
            String[] args = {"-lc"};
            Writer writer = new StringWriter();
            Command<Void> command = CommandFactory.create(args, JCheckSum.class, writer);
            List<String> elements = new ArrayList<String>();
            command.execute();
            Scanner scanner = new Scanner(writer.toString());
            int ln = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                ln++;
                if (ln > 1) {
                    elements.add(line);
                }
            }
            this.view.getCmbCharset().setModel(new DefaultComboBoxModel(elements.toArray()));
            this.view.getCmbCharset().setSelectedItem(DEF_CHARSET);
        } catch (CLIArgumentException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Initialize encoder list.
     */
    private void initEncoders() {
        try {
            String[] args = {"-le"};
            Writer writer = new StringWriter();
            Command<Void> command = CommandFactory.create(args, JCheckSum.class, writer);
            List<String> elements = new ArrayList<String>();
            command.execute();
            Scanner scanner = new Scanner(writer.toString());
            int ln = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                ln++;
                if (ln > 1) {
                    StringTokenizer st = new StringTokenizer(line, ":");
                    if (st.hasMoreTokens()) {
                        elements.add(st.nextToken().trim());
                    }
                }
            }
            this.view.getCmbEncoding().setModel(new DefaultComboBoxModel(elements.toArray()));
        } catch (CLIArgumentException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Process digest action on source value based on current configuration.
     */
    public void digest() {
        Logger.getLogger(MainViewController.class.getName()).log(Level.FINE, "Digest source");
        try {
            String[] args = {
                "-t", this.view.getTxtSource().getText(),
                "-a", String.valueOf(this.view.getCmbAlg().getSelectedItem()),
                "-e", String.valueOf(this.view.getCmbEncoding().getSelectedItem()),
                "-cs", String.valueOf(this.view.getCmbCharset().getSelectedItem())
            };
            if (this.fromFile()) {
                args = new String[]{
                    "-f", this.view.getTxtSource().getText(),
                    "-a", String.valueOf(this.view.getCmbAlg().getSelectedItem()),
                    "-e", String.valueOf(this.view.getCmbEncoding().getSelectedItem()),
                    "-cs", String.valueOf(this.view.getCmbCharset().getSelectedItem())
                };
            }

            Writer writer = new StringWriter();
            Command<Void> command = CommandFactory.create(args, JCheckSum.class, writer);
            command.execute();
            this.view.getTxtTarget().setText(writer.toString());

        } catch (CLIArgumentException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Process encode action on source value based on current configuration.
     */
    public void encode() {
        Logger.getLogger(MainViewController.class.getName()).log(Level.FINE, "Encode source");
        try {
            String[] args = {
                "-t", this.view.getTxtSource().getText(),
                "-e", String.valueOf(this.view.getCmbEncoding().getSelectedItem()),
                "-cs", String.valueOf(this.view.getCmbCharset().getSelectedItem()),
                "-eo"
            };
            if (this.fromFile()) {
                args = new String[]{
                    "-f", this.view.getTxtSource().getText(),
                    "-e", String.valueOf(this.view.getCmbEncoding().getSelectedItem()),
                    "-cs", String.valueOf(this.view.getCmbCharset().getSelectedItem()),
                    "-eo"
                };
            }
            Writer writer = new StringWriter();
            Command<Void> command = CommandFactory.create(args, JCheckSum.class, writer);
            command.execute();
            this.view.getTxtTarget().setText(writer.toString());

        } catch (CLIArgumentException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Copy source text to clipboard.
     */
    private void copySource() {
        StringSelection stringSelection = new StringSelection(view.getTxtSource().getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        Logger.getLogger(MainViewController.class.getName()).log(Level.FINE, "Source copied to clipboard");
    }

    /**
     * Copy result text to clipboard.
     */
    private void copyResult() {
        StringSelection stringSelection = new StringSelection(view.getTxtTarget().getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        Logger.getLogger(MainViewController.class.getName()).log(Level.FINE, "Result copied to clipboard");
    }

    /**
     * Clena source text.
     */
    private void cleanSource() {
        this.view.getTxtSource().setText("");
        Logger.getLogger(MainViewController.class.getName()).log(Level.FINE, "Source cleaned");
    }

    /**
     * Clean result text.
     */
    private void cleanResult() {
        this.view.getTxtTarget().setText("");
        Logger.getLogger(MainViewController.class.getName()).log(Level.FINE, "Result cleaned");
    }

    /**
     * Opens a {@link JFileChooser } dialog to select a file.
     */
    private void choseFile() {
        int res = this.view.getFcSource().showOpenDialog(view);
        if (res == JFileChooser.APPROVE_OPTION) {
            File selectedFile = this.view.getFcSource().getSelectedFile();
            this.view.getTxtSource().setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Process change on file checkbox.
     */
    private void toggleFileChooser() {
        this.view.getBtnFileChooser().setEnabled(false);
        this.view.getTxtSource().setEnabled(true);
        if (this.view.getChkFile().isSelected()) {
            this.view.getTxtSource().setText("");
            this.view.getTxtTarget().setText("");
            this.view.getBtnFileChooser().setEnabled(true);
            this.view.getTxtSource().setEnabled(false);
            this.choseFile();
        }
    }

    /**
     * Returns {@code true} if source value comes froma file. {
     *
     * @ code false} otherwise.
     * @return {@code true} if source value comes froma file. {
     * @ code false} otherwise.
     */
    private boolean fromFile() {
        return this.view.getChkFile().isSelected();
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        if (CMD_FILEC_CK.equals(action.getActionCommand())) {
            this.toggleFileChooser();
        }
        if (CMD_FILEC.equals(action.getActionCommand())) {
            this.choseFile();
        }
        if (CMD_ENCODE.equals(action.getActionCommand())) {
            this.encode();
        }
        if (CMD_DIGEST.equals(action.getActionCommand())) {
            this.digest();
        }
        if (CMD_COPY_SOURCE.equals(action.getActionCommand())) {
            this.copySource();
        }
        if (CMD_COPY_RESULT.equals(action.getActionCommand())) {
            this.copyResult();
        }
        if (CMD_CLEAR_SOURCE.equals(action.getActionCommand())) {
            this.cleanSource();
        }
        if (CMD_CLEAR_RESULT.equals(action.getActionCommand())) {
            this.cleanResult();
        }
    }

    @Override
    public void windowStateChanged(WindowEvent evt) {
        if (evt.equals(WindowEvent.WINDOW_CLOSING)) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.FINE, "Clossing app ...");
        }
    }

}
