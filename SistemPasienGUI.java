import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class Pasien {

    String id, nama, poli, status;
    int umur;
    double biayaPeriksa, biayaObat, total;
    boolean aktif = true;

    Pasien(String id, String nama, int umur,
           String poli, String status,
           double biayaPeriksa,
           double biayaObat) {

        this.id = id;
        this.nama = nama;
        this.umur = umur;
        this.poli = poli;
        this.status = status;
        this.biayaPeriksa = biayaPeriksa;
        this.biayaObat = biayaObat;

        hitungTotal();
    }

    void hitungTotal() {

        total = biayaPeriksa + biayaObat;
    }
}

public class SistemPasienGUI extends JFrame {

    // ================= DATA =================
    Pasien[] data = new Pasien[200];
    int n = 0;

    // ================= KOMPONEN =================
    JTextField txtID = new JTextField();
    JTextField txtNama = new JTextField();
    JTextField txtUmur = new JTextField();
    JTextField txtPoli = new JTextField();
    JTextField txtStatus = new JTextField();
    JTextField txtPeriksa = new JTextField();
    JTextField txtObat = new JTextField();
    JTextField txtCari = new JTextField();

    JButton btnTambah = new JButton("Tambah");
    JButton btnEdit = new JButton("Edit");
    JButton btnHapus = new JButton("Hapus");
    JButton btnCari = new JButton("Cari Nama");
    JButton btnCariID = new JButton("Cari ID");
    JButton btnSortNama = new JButton("Sort Nama");
    JButton btnSortID = new JButton("Sort ID");
    JButton btnSortTotal = new JButton("Sort Total");
    JButton btnStatistik = new JButton("Statistik");
    JButton btnRefresh = new JButton("Refresh");

    DefaultTableModel model;
    JTable table;

    // ================= CONSTRUCTOR =================
    public SistemPasienGUI() {

        setTitle("Sistem Manajemen Data Pasien");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= LOAD DATA =================
        loadData();

        // ================= TITLE =================
        JLabel title = new JLabel(
                "SISTEM MANAJEMEN DATA PASIEN",
                JLabel.CENTER);

        title.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        28));

        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();

        header.setBackground(
                new Color(33, 150, 243));

        header.setPreferredSize(
                new Dimension(100, 70));

        header.add(title);

        add(header, BorderLayout.NORTH);

        // ================= FORM =================
        JPanel form = new JPanel();

        form.setLayout(
                new GridLayout(9, 2, 10, 10));

        form.setBorder(
                BorderFactory.createTitledBorder(
                        "Form Pasien"));

        form.setPreferredSize(
                new Dimension(300, 100));

        form.add(new JLabel("ID"));
        form.add(txtID);

        form.add(new JLabel("Nama"));
        form.add(txtNama);

        form.add(new JLabel("Umur"));
        form.add(txtUmur);

        form.add(new JLabel("Poli"));
        form.add(txtPoli);

        form.add(new JLabel("Status"));
        form.add(txtStatus);

        form.add(new JLabel("Biaya Periksa"));
        form.add(txtPeriksa);

        form.add(new JLabel("Biaya Obat"));
        form.add(txtObat);

        form.add(btnTambah);
        form.add(btnEdit);

        JPanel kiri = new JPanel(
                new BorderLayout());

        kiri.add(form, BorderLayout.CENTER);

        // ================= TABLE =================
        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Umur");
        model.addColumn("Poli");
        model.addColumn("Status");
        model.addColumn("Total");

        table = new JTable(model);

        table.setRowHeight(25);

        table.setFont(
                new Font("Segoe UI",
                        Font.PLAIN,
                        14));

        JScrollPane scroll =
                new JScrollPane(table);

        // ================= PANEL KANAN =================
        JPanel kanan =
                new JPanel(new BorderLayout());

        JPanel atasKanan =
                new JPanel(new GridLayout(2,1));

        JPanel cariPanel =
                new JPanel(new BorderLayout(10,10));

        cariPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Pencarian"));

        cariPanel.add(txtCari,
                BorderLayout.CENTER);

        JPanel tombolCari =
                new JPanel(new GridLayout(1,2,5,5));

        tombolCari.add(btnCari);
        tombolCari.add(btnCariID);

        cariPanel.add(tombolCari,
                BorderLayout.EAST);

        JPanel tombolPanel =
                new JPanel(new GridLayout(2,3,10,10));

        tombolPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Menu"));

        tombolPanel.add(btnHapus);
        tombolPanel.add(btnSortNama);
        tombolPanel.add(btnSortID);
        tombolPanel.add(btnSortTotal);
        tombolPanel.add(btnStatistik);
        tombolPanel.add(btnRefresh);

        atasKanan.add(cariPanel);
        atasKanan.add(tombolPanel);

        kanan.add(atasKanan,
                BorderLayout.NORTH);

        kanan.add(scroll,
                BorderLayout.CENTER);

        add(kiri, BorderLayout.WEST);
        add(kanan, BorderLayout.CENTER);

        tampilData();

        // ================= EVENT =================
        btnTambah.addActionListener(
                e -> tambahData());

        btnEdit.addActionListener(
                e -> editData());

        btnHapus.addActionListener(
                e -> hapusData());

        btnCari.addActionListener(
                e -> cariNama());

        btnCariID.addActionListener(
                e -> cariID());

        btnSortNama.addActionListener(e -> {

            sortNama();
            tampilData();
        });

        btnSortID.addActionListener(e -> {

            sortID();
            tampilData();
        });

        btnSortTotal.addActionListener(e -> {

            sortTotal();
            tampilData();
        });

        btnStatistik.addActionListener(
                e -> statistik());

        btnRefresh.addActionListener(
                e -> tampilData());

        // ================= KLIK TABEL =================
        table.addMouseListener(
                new MouseAdapter() {

            public void mouseClicked(
                    MouseEvent e) {

                int row =
                        table.getSelectedRow();

                txtID.setText(
                        model.getValueAt(row,0)
                                .toString());

                txtNama.setText(
                        model.getValueAt(row,1)
                                .toString());

                txtUmur.setText(
                        model.getValueAt(row,2)
                                .toString());

                txtPoli.setText(
                        model.getValueAt(row,3)
                                .toString());

                txtStatus.setText(
                        model.getValueAt(row,4)
                                .toString());
            }
        });

        setVisible(true);
    }

    // ================= TAMPIL =================
    void tampilData() {

        model.setRowCount(0);

        for (int i = 0; i < n; i++) {

            if (data[i].aktif) {

                model.addRow(new Object[] {

                        data[i].id,
                        data[i].nama,
                        data[i].umur,
                        data[i].poli,
                        data[i].status,
                        data[i].total
                });
            }
        }
    }

    // ================= TAMBAH =================
    void tambahData() {

        try {

            String id = txtID.getText();
            String nama = txtNama.getText();

            int umur =
                    Integer.parseInt(
                            txtUmur.getText());

            String poli =
                    txtPoli.getText();

            String status =
                    txtStatus.getText();

            double bp =
                    Double.parseDouble(
                            txtPeriksa.getText());

            double bo =
                    Double.parseDouble(
                            txtObat.getText());

            data[n++] = new Pasien(
                    id,
                    nama,
                    umur,
                    poli,
                    status,
                    bp,
                    bo
            );

            simpanData();

            tampilData();

            JOptionPane.showMessageDialog(
                    this,
                    "Data berhasil ditambahkan!");

            clearForm();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Input tidak valid!");
        }
    }

    // ================= EDIT =================
    void editData() {

        String cari = txtID.getText();

        for (int i = 0; i < n; i++) {

            if (data[i].id.equalsIgnoreCase(cari)
                    && data[i].aktif) {

                data[i].nama =
                        txtNama.getText();

                data[i].umur =
                        Integer.parseInt(
                                txtUmur.getText());

                data[i].poli =
                        txtPoli.getText();

                data[i].status =
                        txtStatus.getText();

                data[i].biayaPeriksa =
                        Double.parseDouble(
                                txtPeriksa.getText());

                data[i].biayaObat =
                        Double.parseDouble(
                                txtObat.getText());

                data[i].hitungTotal();

                simpanData();

                tampilData();

                JOptionPane.showMessageDialog(
                        this,
                        "Data berhasil diupdate!");

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Data tidak ditemukan!");
    }

    // ================= HAPUS =================
    void hapusData() {

        String cari = txtID.getText();

        for (int i = 0; i < n; i++) {

            if (data[i].id.equalsIgnoreCase(cari)
                    && data[i].aktif) {

                data[i].aktif = false;

                simpanData();

                tampilData();

                JOptionPane.showMessageDialog(
                        this,
                        "Data berhasil dihapus!");

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Data tidak ditemukan!");
    }

    // ================= CARI NAMA =================
    void cariNama() {

        String cari =
                txtCari.getText()
                        .toLowerCase();

        model.setRowCount(0);

        for (int i = 0; i < n; i++) {

            if (data[i].aktif &&
                    data[i].nama
                            .toLowerCase()
                            .contains(cari)) {

                model.addRow(new Object[] {

                        data[i].id,
                        data[i].nama,
                        data[i].umur,
                        data[i].poli,
                        data[i].status,
                        data[i].total
                });
            }
        }
    }

    // ================= CARI ID =================
    void cariID() {

        String cari = txtCari.getText();

        model.setRowCount(0);

        for (int i = 0; i < n; i++) {

            if (data[i].aktif &&
                    data[i].id
                            .equalsIgnoreCase(cari)) {

                model.addRow(new Object[] {

                        data[i].id,
                        data[i].nama,
                        data[i].umur,
                        data[i].poli,
                        data[i].status,
                        data[i].total
                });
            }
        }
    }

    // ================= SORT ID =================
    void sortID() {

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0;
                 j < n - i - 1;
                 j++) {

                if (data[j].id.compareTo(
                        data[j + 1].id) > 0) {

                    Pasien temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
            }
        }
    }

    // ================= SORT NAMA =================
    void sortNama() {

        for (int i = 0; i < n - 1; i++) {

            int min = i;

            for (int j = i + 1; j < n; j++) {

                if (data[j].nama
                        .compareToIgnoreCase(
                                data[min].nama) < 0) {

                    min = j;
                }
            }

            Pasien temp = data[i];
            data[i] = data[min];
            data[min] = temp;
        }
    }

    // ================= SORT TOTAL =================
    void sortTotal() {

        Arrays.sort(data, 0, n,
                (a,b) ->
                        Double.compare(
                                b.total,
                                a.total));
    }

    // ================= STATISTIK =================
    void statistik() {

        int aktif = 0;
        int hapus = 0;
        double total = 0;

        for (int i = 0; i < n; i++) {

            if (data[i].aktif) {

                aktif++;
                total += data[i].total;

            } else {

                hapus++;
            }
        }

        JOptionPane.showMessageDialog(
                this,

                "===== STATISTIK =====\n\n" +
                "Total Data     : " + n +
                "\nData Aktif     : " + aktif +
                "\nData Terhapus  : " + hapus +
                "\nTotal Biaya    : " + total +
                "\nRata-rata      : " +
                (total / aktif)
        );
    }

    // ================= SIMPAN DATA =================
    void simpanData() {

        try {

            PrintWriter w =
                    new PrintWriter("data.txt");

            for (int i = 0; i < n; i++) {

                w.println(

                        data[i].id + "," +
                        data[i].nama + "," +
                        data[i].umur + "," +
                        data[i].poli + "," +
                        data[i].status + "," +
                        data[i].biayaPeriksa + "," +
                        data[i].biayaObat + "," +
                        data[i].aktif
                );
            }

            w.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal menyimpan data!");
        }
    }

    // ================= LOAD DATA =================
    void loadData() {

        try {

            File file =
                    new File("data.txt");

            // ================= JIKA FILE BELUM ADA =================
            if (!file.exists()) {

                data[n++] = new Pasien("P001","Andi",20,"Umum","Rawat Jalan",100000,50000);
                data[n++] = new Pasien("P002","Budi",25,"Gigi","Rawat Inap",150000,70000);
                data[n++] = new Pasien("P003","Citra",30,"Anak","Rawat Jalan",120000,60000);
                data[n++] = new Pasien("P004","Dewi",28,"Umum","Rawat Inap",200000,80000);
                data[n++] = new Pasien("P005","Eka",35,"Saraf","Rawat Jalan",170000,90000);
                data[n++] = new Pasien("P006","Fajar",40,"Umum","Rawat Inap",250000,100000);
                data[n++] = new Pasien("P007","Gina",22,"Gigi","Rawat Jalan",110000,40000);
                data[n++] = new Pasien("P008","Hadi",50,"Saraf","Rawat Inap",300000,150000);
                data[n++] = new Pasien("P009","Intan",27,"Anak","Rawat Jalan",130000,50000);
                data[n++] = new Pasien("P010","Joko",60,"Umum","Rawat Inap",280000,120000);
                data[n++] = new Pasien("P011","Kevin",32,"THT","Rawat Jalan",160000,70000);
                data[n++] = new Pasien("P012","Lina",26,"Kulit","Rawat Inap",220000,100000);
                data[n++] = new Pasien("P013","Maya",41,"Saraf","Rawat Jalan",180000,90000);
                data[n++] = new Pasien("P014","Nanda",19,"Umum","Rawat Jalan",100000,50000);
                data[n++] = new Pasien("P015","Oscar",55,"Jantung","Rawat Inap",350000,200000);
                data[n++] = new Pasien("P016","Putri",29,"Anak","Rawat Jalan",140000,60000);
                data[n++] = new Pasien("P017","Qori",37,"THT","Rawat Inap",240000,120000);
                data[n++] = new Pasien("P018","Rina",24,"Gigi","Rawat Jalan",130000,50000);
                data[n++] = new Pasien("P019","Sandi",48,"Saraf","Rawat Inap",320000,170000);
                data[n++] = new Pasien("P020","Tina",21,"Kulit","Rawat Jalan",125000,45000);
                data[n++] = new Pasien("P021","Udin",45,"Jantung","Rawat Inap",340000,180000);
                data[n++] = new Pasien("P022","Vina",31,"Umum","Rawat Jalan",150000,65000);
                data[n++] = new Pasien("P023","Wawan",52,"THT","Rawat Inap",260000,130000);
                data[n++] = new Pasien("P024","Xena",27,"Anak","Rawat Jalan",145000,55000);
                data[n++] = new Pasien("P025","Yusuf",39,"Gigi","Rawat Inap",230000,110000);
                data[n++] = new Pasien("P026","Zahra",23,"Kulit","Rawat Jalan",135000,50000);
                data[n++] = new Pasien("P027","Asep",34,"Saraf","Rawat Jalan",190000,95000);
                data[n++] = new Pasien("P028","Bella",28,"Umum","Rawat Inap",210000,85000);
                data[n++] = new Pasien("P029","Cahyo",43,"Jantung","Rawat Inap",360000,210000);
                data[n++] = new Pasien("P030","Dina",20,"Anak","Rawat Jalan",120000,50000);

                simpanData();

                return;
            }

            // ================= LOAD TXT =================
            Scanner baca =
                    new Scanner(file);

            while (baca.hasNextLine()) {

                String line =
                        baca.nextLine();

                if (line.trim().isEmpty())
                    continue;

                String[] d =
                        line.split(",");

                data[n] = new Pasien(

                        d[0],
                        d[1],
                        Integer.parseInt(d[2]),
                        d[3],
                        d[4],
                        Double.parseDouble(d[5]),
                        Double.parseDouble(d[6])
                );

                data[n].aktif =
                        Boolean.parseBoolean(d[7]);

                n++;
            }

            baca.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal memuat data!");
        }
    }

    // ================= CLEAR =================
    void clearForm() {

        txtID.setText("");
        txtNama.setText("");
        txtUmur.setText("");
        txtPoli.setText("");
        txtStatus.setText("");
        txtPeriksa.setText("");
        txtObat.setText("");
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new SistemPasienGUI();
        });
    }
}