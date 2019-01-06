package ModificaPrenotazione;

import Oggetti.ErroreDialog;
import Oggetti.Prenotazione;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ScegliPrenotazioneDialog {
    private ScegliPrenotazioneControl control;
    private List<Prenotazione> prenotazioni;
    private JFrame frame;
    private JButton modificaButton;
    private JPanel panel;
    private JList list1;

    public ScegliPrenotazioneDialog(ScegliPrenotazioneControl control, List<Prenotazione> prenotazioni) {
        this.control = control;
        this.prenotazioni = prenotazioni;
        frame = new JFrame("Seleziona la prenotazione da modificare");
        $$$setupUI$$$();
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: fai qualcosa
            }
        });
    }

    public void selezionaPrenotazione() {

    }

    public void dispose() {
        frame.dispose();
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
        panel.add(list1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        modificaButton = new JButton();
        modificaButton.setText("Modifica");
        panel.add(modificaButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        DefaultListModel<Prenotazione> listModel = new DefaultListModel<Prenotazione>();
        if (prenotazioni != null) {
            for (Prenotazione p : prenotazioni) {
                listModel.addElement(p);
            }
        } else {
            new ErroreDialog("Nessuna prenotazione modificabile presente");
        }
        list1 = new JList(listModel);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
