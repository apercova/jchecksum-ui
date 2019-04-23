package io.apercova.jencoderui.controller;

import io.apercova.jchecksum.JCheckSum;
import io.apercova.jencoderui.ui.MainView;
import io.apercova.quickcli.Command;
import io.apercova.quickcli.CommandFactory;
import io.apercova.quickcli.exception.CLIArgumentException;
import io.apercova.quickcli.exception.ExecutionException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;

public class MainViewController implements ActionListener {

    public final static String DEF_CHARSET = "UTF-8";
    
    public final static String FILEC_CK = "100";
    public final static String FILEC_AC = "101";
    public final static String ENCODE_AC = "110";
    public final static String DIGEST_AC = "120";
    public final static String COPY_RESULT_AC = "130";
    

    private final MainView view;

    public MainViewController(MainView view) {
        this.view = view;

        this.initAlgs();
        this.initEncoders();
        this.initCharsets();
    }

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
                args = new String[] {
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

    ;
    
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
                args = new String[] {
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

    ;
    
    public void copyResult() {
        StringSelection stringSelection = new StringSelection(view.getTxtTarget().getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        Logger.getLogger(MainViewController.class.getName()).log(Level.FINE, "Result copied to clipboard");
    }

    ;
    
    private void choseFile() {
        int res = this.view.getFcSource().showOpenDialog(view);
        if (res == JFileChooser.APPROVE_OPTION) {
            File selectedFile = this.view.getFcSource().getSelectedFile();
            this.view.getTxtSource().setText(selectedFile.getAbsolutePath());
        }
    }
    
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
    
    private boolean fromFile() {
        return this.view.getChkFile().isSelected();
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        if (FILEC_CK.equals(action.getActionCommand())) {
            this.toggleFileChooser();
        } 
        if (FILEC_AC.equals(action.getActionCommand())) {
            this.choseFile();
        }
        if (ENCODE_AC.equals(action.getActionCommand())) {
            this.encode();
        }
        if (DIGEST_AC.equals(action.getActionCommand())) {
            this.digest();
        }
        if (COPY_RESULT_AC.equals(action.getActionCommand())) {
            this.copyResult();
        }
    }

}
