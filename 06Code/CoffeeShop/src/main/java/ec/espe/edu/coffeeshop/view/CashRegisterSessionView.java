package ec.espe.edu.coffeeshop.view;
import javax.swing.*;
public class CashRegisterSessionView extends JFrame {
    public CashRegisterSessionView() {
        setTitle("Sesiones de Caja Registradora");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new JLabel("Módulo de Sesiones de Caja Registradora", SwingConstants.CENTER));
    }
    public void loadTableData() {
    }
}
