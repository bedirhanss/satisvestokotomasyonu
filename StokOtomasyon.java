/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stokvepersonel;

/**
 *
 * @author bedir
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.swing.table.TableRowSorter;

public class StokOtomasyon extends JFrame {

    private JTextField tfIsim, tfTur, tfMarka, tfMiktar, tfKayitTarihi, tfBarkod, tfFiyat, tfAra;
    private JButton btnGorselYukle, btnEkle, btnSil, btnSifirla, btnGuncelle;
    private JLabel lblImage;
    private JTable table;
    private DefaultTableModel tableModel;
    private byte[] imageBlob;

    public StokOtomasyon() {
        setTitle("Stok Otomasyon");
        setLayout(new GridLayout(4, 1));

        // Panel for form fields
        JPanel panelForm = new JPanel(new GridLayout(5, 4));
        tfIsim = new JTextField();
        tfTur = new JTextField();
        tfMarka = new JTextField();
        tfMiktar = new JTextField();
        tfKayitTarihi = new JTextField();
        tfBarkod = new JTextField();
        tfFiyat = new JTextField();
        tfAra = new JTextField();
        btnGorselYukle = new JButton("Görsel Yükle");
        btnEkle = new JButton("Ekle");
        btnSil = new JButton("Sil");
        btnSifirla = new JButton("Sıfırla");
        btnGuncelle = new JButton("Güncelle");
        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(100, 100)); // Kare yapmak için boyutunu sabitliyoruz
        lblImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panelForm.add(new JLabel("İsim"));
        panelForm.add(tfIsim);
        panelForm.add(new JLabel("Tür"));
        panelForm.add(tfTur);
        panelForm.add(new JLabel("Resim"));
        panelForm.add(btnGorselYukle);
        panelForm.add(lblImage);
        panelForm.add(btnEkle);
        panelForm.add(new JLabel("Marka"));
        panelForm.add(tfMarka);
        panelForm.add(new JLabel("Miktar"));
        panelForm.add(tfMiktar);
        panelForm.add(new JLabel("Kayıt Tarihi"));
        panelForm.add(tfKayitTarihi);
        panelForm.add(btnSil);
        panelForm.add(new JLabel("Barkod"));
        panelForm.add(tfBarkod);
        panelForm.add(new JLabel("Fiyat"));
        panelForm.add(tfFiyat);
        panelForm.add(btnSifirla);
        panelForm.add(btnGuncelle);

        add(panelForm);

        // Panel for search
        JPanel panelSearch = new JPanel(new BorderLayout());
        panelSearch.add(new JLabel("Ara"), BorderLayout.WEST);
        panelSearch.add(tfAra, BorderLayout.CENTER);
        add(panelSearch);

        // Panel for table
        tableModel = new DefaultTableModel(new String[]{"İsim", "Marka", "Barkod", "Tür", "Miktar", "Fiyat", "Tarih"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table));

        // Action Listeners
        btnGorselYukle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        BufferedImage img = ImageIO.read(file);
                        lblImage.setIcon(new ImageIcon(img.getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH)));
                        imageBlob = imageToBlob(img);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        btnEkle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = DBConnection.getConnection();
                    String sql = "INSERT INTO product (isim, tur, marka, miktar, kayit_tarihi, barkod, fiyat, resim) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    Product product = new Product(tfIsim.getText(), tfTur.getText(), tfMarka.getText(), Integer.parseInt(tfMiktar.getText()), tfKayitTarihi.getText(), tfBarkod.getText(), Double.parseDouble(tfFiyat.getText()), imageBlob);

                    statement.setString(1, product.getIsim());
                    statement.setString(2, product.getTur());
                    statement.setString(3, product.getMarka());
                    statement.setInt(4, product.getMiktar());
                    statement.setString(5, product.getKayitTarihi());
                    statement.setString(6, product.getBarkod());
                    statement.setDouble(7, product.getFiyat());
                    statement.setBytes(8, product.getResim());

                    statement.executeUpdate();
                    statement.close();
                    connection.close();
                    loadTableData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnSil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String barkod = tableModel.getValueAt(selectedRow, 2).toString();
                    try {
                        Connection connection = DBConnection.getConnection();
                        String sql = "DELETE FROM product WHERE barkod = ?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setString(1, barkod);
                        statement.executeUpdate();
                        statement.close();
                        connection.close();
                        loadTableData();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen silinecek bir satır seçin.");
                }
            }
        });

        btnSifirla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfIsim.setText("");
                tfTur.setText("");
                tfMarka.setText("");
                tfMiktar.setText("");
                tfKayitTarihi.setText("");
                tfBarkod.setText("");
                tfFiyat.setText("");
                tfAra.setText("");
                lblImage.setIcon(null);
                imageBlob = null;
            }
        });

        btnGuncelle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String barkod = tableModel.getValueAt(selectedRow, 2).toString();
                    try {
                        Connection connection = DBConnection.getConnection();
                        String sql = "UPDATE product SET isim = ?, tur = ?, marka = ?, miktar = ?, kayit_tarihi = ?, fiyat = ?, resim = ? WHERE barkod = ?";
                        PreparedStatement statement = connection.prepareStatement(sql);

                        Product product = new Product(tfIsim.getText(), tfTur.getText(), tfMarka.getText(), Integer.parseInt(tfMiktar.getText()), tfKayitTarihi.getText(), tfBarkod.getText(), Double.parseDouble(tfFiyat.getText()), imageBlob);

                        statement.setString(1, product.getIsim());
                        statement.setString(2, product.getTur());
                        statement.setString(3, product.getMarka());
                        statement.setInt(4, product.getMiktar());
                        statement.setString(5, product.getKayitTarihi());
                        statement.setDouble(6, product.getFiyat());
                        statement.setBytes(7, product.getResim());
                        statement.setString(8, product.getBarkod());

                        statement.executeUpdate();
                        statement.close();
                        connection.close();
                        loadTableData();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen güncellenecek bir satır seçin.");
                }
            }
        });

        // Add KeyListener for search functionality
        tfAra.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchQuery = tfAra.getText().toLowerCase();
                filterTableData(searchQuery);
            }
        });

        // Load initial data
        loadTableData();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        // Ekranın ortasında çalışmasını sağlamak için bu satırı ekleyin:
        setLocationRelativeTo(null);
    }

    private void loadTableData() {
        try {
            Connection connection = DBConnection.getConnection();
            String sql = "SELECT * FROM product";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            tableModel.setRowCount(0); // Clear existing data
            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getString("isim"),
                    resultSet.getString("marka"),
                    resultSet.getString("barkod"),
                    resultSet.getString("tur"),
                    resultSet.getInt("miktar"),
                    resultSet.getDouble("fiyat"),
                    resultSet.getString("kayit_tarihi")
                };
                tableModel.addRow(row);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void filterTableData(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    private byte[] imageToBlob(BufferedImage img) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        return baos.toByteArray();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StokOtomasyon();
        });
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
            .addGap(0, 672, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 625, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

