package ModificaPrenotazione;

import Oggetti.ErroreDialog;
import Oggetti.Prenotazione;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ScegliPrenotazioneDialog {
    private ScegliPrenotazioneControl control;
    private List<Prenotazione> prenotazioni;
    private Prenotazione prenotazioneSelezionata;
    private JFrame frame;
    private JButton modificaButton;
    private JPanel panel;
    private JList<Prenotazione> list1;

    public ScegliPrenotazioneDialog(ScegliPrenotazioneControl control, List<Prenotazione> prenotazioni) {
        this.control = control;
        this.prenotazioni = prenotazioni;
        frame = new JFrame("SPRINT - Seleziona la prenotazione da modificare");
        $$$setupUI$$$();
        frame.setContentPane(panel);

        modificaButton.setEnabled(false);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selezionaPrenotazione();
            }
        });
        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!list1.isSelectionEmpty()) {
                    modificaButton.setEnabled(true);
                    prenotazioneSelezionata = prenotazioni.get(list1.getSelectedIndex());
                }


            }
        });
    }

    public void selezionaPrenotazione() {
        if (!list1.isSelectionEmpty()) {
            control.estraiPrenotazione();
        }
    }

    public void dispose() {
        frame.dispose();
    }

    public Prenotazione getPrenotazioneSelezionata() {
        return prenotazioneSelezionata;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        list1.setToolTipText("Selezionare la prenotazione che si desidera modificare e cliccare \"Modifica\"");
        panel.add(list1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        modificaButton = new JButton();
        modificaButton.setText("Modifica");
        panel.add(modificaButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

    private void createUIComponents() {
        DefaultListModel<Prenotazione> listModel = new DefaultListModel<>();
        if (prenotazioni != null) {
            for (Prenotazione p : prenotazioni) {
                listModel.addElement(p);
            }
        } else {
            new ErroreDialog("Nessuna prenotazione modificabile presente");
        }
        list1 = new JList<>(listModel);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
