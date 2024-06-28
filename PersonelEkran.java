/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stokvepersonel;

/**
 *
 * @author bedir
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.sql.*;

public class PersonelEkran extends JFrame {
    private JTextField txtAd, txtSoyad, txtDepartman, txtMaas, txtAra;
    private JButton btnGuncelle, btnEkle, btnSil, btnTemizle;
    private JTable table;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/x";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public PersonelEkran() {
        setTitle("Personel Yönetimi");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblAd = new JLabel("AD");
        lblAd.setBounds(10, 10, 100, 25);
        add(lblAd);
        txtAd = new JTextField();
        txtAd.setBounds(110, 10, 200, 25);
        add(txtAd);

        JLabel lblSoyad = new JLabel("SOYAD");
        lblSoyad.setBounds(10, 45, 100, 25);
        add(lblSoyad);
        txtSoyad = new JTextField();
        txtSoyad.setBounds(110, 45, 200, 25);
        add(txtSoyad);

        JLabel lblDepartman = new JLabel("DEPARTMAN");
        lblDepartman.setBounds(10, 80, 100, 25);
        add(lblDepartman);
        txtDepartman = new JTextField();
        txtDepartman.setBounds(110, 80, 200, 25);
        add(txtDepartman);

        JLabel lblMaas = new JLabel("MAAS");
        lblMaas.setBounds(10, 115, 100, 25);
        add(lblMaas);
        txtMaas = new JTextField();
        txtMaas.setBounds(110, 115, 200, 25);
        add(txtMaas);

        btnGuncelle = new JButton("GUNCELLE");
        btnGuncelle.setBounds(330, 10, 100, 25);
        add(btnGuncelle);
        btnEkle = new JButton("EKLE");
        btnEkle.setBounds(450, 10, 100, 25);
        add(btnEkle);
        btnSil = new JButton("SIL");
        btnSil.setBounds(330, 45, 100, 25);
        add(btnSil);
        btnTemizle = new JButton("TEMIZLE");
        btnTemizle.setBounds(450, 45, 100, 25);
        add(btnTemizle);

        JLabel lblAra = new JLabel("ARA");
        lblAra.setBounds(10, 150, 100, 25);
        add(lblAra);
        txtAra = new JTextField();
        txtAra.setBounds(110, 150, 200, 25);
        add(txtAra);

        tableModel = new DefaultTableModel(new String[]{"ID", "AD", "SOYAD", "DEPARTMAN", "MAAS"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 185, 760, 360);
        add(scrollPane);

        btnEkle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPersonel();
            }
        });

        btnGuncelle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePersonel();
            }
        });

        btnSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deletePersonel();
            }
        });

        btnTemizle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        txtAra.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                searchPersonel(txtAra.getText());
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                txtAd.setText(tableModel.getValueAt(selectedRow, 1).toString());
                txtSoyad.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtDepartman.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtMaas.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });

        loadPersonel();
    }

    private void addPersonel() {
        String ad = txtAd.getText();
        String soyad = txtSoyad.getText();
        String departman = txtDepartman.getText();
        String maas = txtMaas.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO personeller (ad, soyad, departman, maas) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, ad);
                pstmt.setString(2, soyad);
                pstmt.setString(3, departman);
                pstmt.setString(4, maas);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadPersonel();
    }

    private void updatePersonel() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String ad = txtAd.getText();
        String soyad = txtSoyad.getText();
        String departman = txtDepartman.getText();
        String maas = txtMaas.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE personeller SET ad = ?, soyad = ?, departman = ?, maas = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, ad);
                pstmt.setString(2, soyad);
                pstmt.setString(3, departman);
                pstmt.setString(4, maas);
                pstmt.setInt(5, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadPersonel();
    }

    private void deletePersonel() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;
        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM personeller WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadPersonel();
    }

    private void clearFields() {
        txtAd.setText("");
        txtSoyad.setText("");
        txtDepartman.setText("");
        txtMaas.setText("");
    }

    private void searchPersonel(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter(query));
    }

    private void loadPersonel() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM personeller";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String ad = rs.getString("ad");
                    String soyad = rs.getString("soyad");
                    String departman = rs.getString("departman");
                    String maas = rs.getString("maas");
                    tableModel.addRow(new Object[]{id, ad, soyad, departman, maas});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PersonelEkran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PersonelEkran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PersonelEkran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PersonelEkran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PersonelEkran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
