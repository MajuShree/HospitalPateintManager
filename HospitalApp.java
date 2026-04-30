package hospital;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * HospitalApp - White & Light Blue themed tabbed Swing GUI
 * MODULE-3: JFrame, JTabbedPane, JTable, JLabel, JTextField,
 *           JButton, Event Handling, Components and Containers (Ch.32-33)
 */
public class HospitalApp extends JFrame {

    // ── Pure White + Light Blue Palette ───────────────────────────────────────
    private static final Color WHITE        = Color.WHITE;
    private static final Color LB_XLIGHT    = new Color(235, 245, 255);   // very light blue page bg
    private static final Color LB_LIGHT     = new Color(207, 226, 255);   // light blue panel/header
    private static final Color LB_MID       = new Color(147, 197, 253);   // mid blue accents
    private static final Color LB_MAIN      = new Color(59,  130, 246);   // primary blue
    private static final Color LB_DARK      = new Color(29,   78, 216);   // dark blue for hover
    private static final Color LB_HEADER    = new Color(219, 234, 254);   // tab header bg
    private static final Color BORDER_BLUE  = new Color(186, 214, 253);   // border colour
    private static final Color TEXT_DARK    = new Color(30,  58, 138);    // dark blue text
    private static final Color TEXT_MID     = new Color(71, 107, 176);    // medium text
    private static final Color TEXT_GRAY    = new Color(100, 116, 139);   // muted text
    private static final Color GREEN        = new Color(22,  163, 74);    // success
    private static final Color GREEN_LT     = new Color(220, 252, 231);
    private static final Color RED          = new Color(220,  38,  38);   // danger
    private static final Color RED_LT       = new Color(254, 226, 226);
    private static final Color AMBER        = new Color(217, 119,   6);
    private static final Color AMBER_LT     = new Color(254, 243, 199);
    private static final Color ROW_ALT      = new Color(240, 248, 255);   // alternate row

    // ── Data ──────────────────────────────────────────────────────────────────
    private PatientManager manager = new PatientManager();

    // ── Header stat labels ────────────────────────────────────────────────────
    private JLabel lblTotal, lblSeniors, lblAvg;

    // ── Tab models / tables ───────────────────────────────────────────────────
    private DefaultTableModel allModel, seniorModel, sortModel, searchModel;

    // ── Add tab ───────────────────────────────────────────────────────────────
    private JTextField addName, addAge, addDisease;
    private JLabel     addFeedback;

    // ── Edit/Delete tab ───────────────────────────────────────────────────────
    private JTextField edtId, edtName, edtAge, edtDisease;
    private JLabel     edtFeedback;

    // ── Search tab ────────────────────────────────────────────────────────────
    private JTextField        searchKeyword;
    private JComboBox<String> searchBy;
    private JLabel            searchInfo;

    // ── Sort tab ──────────────────────────────────────────────────────────────
    private JComboBox<String> sortBy;

    // ── Status bar ────────────────────────────────────────────────────────────
    private JLabel statusLabel;

    // ── Tabs ──────────────────────────────────────────────────────────────────
    private JTabbedPane tabs;

    // =========================================================================
    public HospitalApp() {
        setTitle("Hospital Patient Record Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1160, 750);
        setMinimumSize(new Dimension(980, 640));
        setLocationRelativeTo(null);
        getContentPane().setBackground(LB_XLIGHT);
        setLayout(new BorderLayout());

        add(buildHeader(),    BorderLayout.NORTH);
        add(buildTabs(),      BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        refreshAll();
    }

    // =========================================================================
    //  HEADER  — white background, blue text and pills
    // =========================================================================
    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(WHITE);
        h.setPreferredSize(new Dimension(0, 70));
        h.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, LB_MID),
                new EmptyBorder(0, 24, 0, 24)));

        // Left: cross icon + title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);

        // Blue cross badge
        JLabel cross = new JLabel("✚") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(LB_MAIN);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cross.setForeground(WHITE);
        cross.setFont(new Font("SansSerif", Font.BOLD, 22));
        cross.setOpaque(false);
        cross.setBorder(new EmptyBorder(4, 8, 4, 8));

        JLabel title = label("Hospital Patient Record Manager", 19, TEXT_DARK, Font.BOLD);

        left.add(cross);
        left.add(title);

        // Right: stat pills
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        lblTotal   = statPill("0 Patients",    LB_LIGHT,   TEXT_DARK);
        lblSeniors = statPill("0 Seniors 60+", AMBER_LT,   AMBER);
        lblAvg     = statPill("Avg 0 yrs",     GREEN_LT,   GREEN);
        right.add(lblTotal);
        right.add(lblSeniors);
        right.add(lblAvg);

        h.add(left,  BorderLayout.WEST);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    // =========================================================================
    //  TABS
    // =========================================================================
    private JTabbedPane buildTabs() {
        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBackground(LB_XLIGHT);
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.setBorder(new EmptyBorder(8, 12, 8, 12));
        UIManager.put("TabbedPane.selected",          WHITE);
        UIManager.put("TabbedPane.background",         LB_XLIGHT);
        UIManager.put("TabbedPane.foreground",         TEXT_MID);
        UIManager.put("TabbedPane.selectedForeground", LB_MAIN);

        tabs.addTab("  All Patients  ",  buildAllTab());
        tabs.addTab("  Add Patient  ",   buildAddTab());
        tabs.addTab("  Edit / Delete  ", buildEditTab());
        tabs.addTab("  Search  ",        buildSearchTab());
        tabs.addTab("  Sort & View  ",   buildSortTab());
        tabs.addTab("  Seniors (60+)  ", buildSeniorsTab());
        return tabs;
    }

    // -------------------------------------------------------------------------
    //  TAB 1 — All Patients
    // -------------------------------------------------------------------------
    private JPanel buildAllTab() {
        JPanel p = page();

        // Top bar
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(0, 0, 12, 0));
        top.add(label("All Patient Records", 15, TEXT_DARK, Font.BOLD), BorderLayout.WEST);
        JButton refresh = outlineBtn("⟳  Refresh", LB_MAIN);
        refresh.addActionListener(e -> { refreshAll(); status("Table refreshed.", LB_MAIN); });
        top.add(refresh, BorderLayout.EAST);

        allModel = tableModel();
        JTable t  = patientTable(allModel, true);

        p.add(top,              BorderLayout.NORTH);
        p.add(scrollWrap(t),    BorderLayout.CENTER);
        p.add(legendRow(),      BorderLayout.SOUTH);
        return p;
    }

    // -------------------------------------------------------------------------
    //  TAB 2 — Add Patient
    // -------------------------------------------------------------------------
    private JPanel buildAddTab() {
        JPanel outer = centered();

        JPanel card = whiteCard(480, 380);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        cardHeading(card, "Add New Patient");
        cardSub(card, "Register a new patient by filling in the form below.");
        card.add(Box.createVerticalStrut(18));

        addName    = inputRow(card, "Patient Full Name",     "e.g. Anjali Sharma");
        addAge     = inputRow(card, "Age",                   "e.g. 52");
        addDisease = inputRow(card, "Disease / Condition",   "e.g. Diabetes");
        card.add(Box.createVerticalStrut(6));

        JButton btn = filledBtn("Add Patient", GREEN);
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(9999, 42));
        card.add(btn);

        card.add(Box.createVerticalStrut(10));
        addFeedback = label("", 13, GREEN, Font.PLAIN);
        addFeedback.setAlignmentX(LEFT_ALIGNMENT);
        card.add(addFeedback);

        btn.addActionListener(e -> {
            String nm  = addName.getText().trim();
            String ags = addAge.getText().trim();
            String dis = addDisease.getText().trim();
            if (nm.isEmpty() || ags.isEmpty() || dis.isEmpty()) {
                setFeedback(addFeedback, "All fields are required.", RED); return;
            }
            try {
                int age = Integer.parseInt(ags);
                if (age < 0 || age > 150) throw new NumberFormatException();
                Patient pt = manager.addPatient(nm, age, dis);
                addName.setText(""); addAge.setText(""); addDisease.setText("");
                refreshAll();
                setFeedback(addFeedback, "Patient added! ID: " + pt.getPatientId(), GREEN);
                status("Added: " + pt.getName(), GREEN);
                tabs.setSelectedIndex(0);
            } catch (NumberFormatException ex) {
                setFeedback(addFeedback, "Age must be a number from 0 to 150.", RED);
            }
        });

        outer.add(card);
        return outer;
    }

    // -------------------------------------------------------------------------
    //  TAB 3 — Edit / Delete
    // -------------------------------------------------------------------------
    private JPanel buildEditTab() {
        JPanel outer = centered();

        JPanel card = whiteCard(480, 450);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        cardHeading(card, "Edit or Delete a Patient");
        cardSub(card, "Load a patient by ID, update details, or remove the record.");
        card.add(Box.createVerticalStrut(16));

        // ID row
        JPanel idRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        idRow.setOpaque(false);
        idRow.setAlignmentX(LEFT_ALIGNMENT);
        idRow.setMaximumSize(new Dimension(9999, 38));
        edtId = new JTextField(10); styleInput(edtId);
        JButton loadBtn = outlineBtn("Load", LB_MAIN);
        idRow.add(label("Patient ID:", 13, TEXT_MID, Font.PLAIN));
        idRow.add(edtId);
        idRow.add(loadBtn);
        card.add(idRow);
        card.add(Box.createVerticalStrut(14));

        // Divider
        JSeparator div = new JSeparator();
        div.setForeground(BORDER_BLUE);
        div.setAlignmentX(LEFT_ALIGNMENT);
        div.setMaximumSize(new Dimension(9999, 1));
        card.add(div);
        card.add(Box.createVerticalStrut(14));

        edtName    = inputRow(card, "Name",    "");
        edtAge     = inputRow(card, "Age",     "");
        edtDisease = inputRow(card, "Disease", "");
        card.add(Box.createVerticalStrut(8));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btns.setOpaque(false);
        btns.setAlignmentX(LEFT_ALIGNMENT);
        btns.setMaximumSize(new Dimension(9999, 44));
        JButton updBtn = filledBtn("Update", LB_MAIN);
        JButton delBtn = filledBtn("Delete", RED);
        btns.add(updBtn); btns.add(delBtn);
        card.add(btns);

        card.add(Box.createVerticalStrut(10));
        edtFeedback = label("", 13, LB_MAIN, Font.PLAIN);
        edtFeedback.setAlignmentX(LEFT_ALIGNMENT);
        card.add(edtFeedback);

        // Events
        loadBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(edtId.getText().trim());
                Patient pt = manager.findById(id);
                if (pt == null) {
                    setFeedback(edtFeedback, "No patient found with ID " + id + ".", RED);
                } else {
                    edtName.setText(pt.getName());
                    edtAge.setText(String.valueOf(pt.getAge()));
                    edtDisease.setText(pt.getDisease());
                    setFeedback(edtFeedback, "Loaded: " + pt.getName(), LB_MAIN);
                }
            } catch (NumberFormatException ex) {
                setFeedback(edtFeedback, "Enter a valid numeric Patient ID.", RED);
            }
        });

        updBtn.addActionListener(e -> {
            try {
                int    id  = Integer.parseInt(edtId.getText().trim());
                String nm  = edtName.getText().trim();
                int    age = Integer.parseInt(edtAge.getText().trim());
                String dis = edtDisease.getText().trim();
                if (nm.isEmpty() || dis.isEmpty()) throw new IllegalArgumentException();
                if (manager.updatePatient(id, nm, age, dis)) {
                    refreshAll();
                    setFeedback(edtFeedback, "Patient ID " + id + " updated.", GREEN);
                    status("Updated patient ID " + id, LB_MAIN);
                } else {
                    setFeedback(edtFeedback, "Patient ID " + id + " not found.", RED);
                }
            } catch (Exception ex) {
                setFeedback(edtFeedback, "Please load a valid patient first.", RED);
            }
        });

        delBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(edtId.getText().trim());
                Patient pt = manager.findById(id);
                if (pt == null) {
                    setFeedback(edtFeedback, "No patient found with ID " + id + ".", RED);
                    return;
                }
                int ok = JOptionPane.showConfirmDialog(this,
                        "Delete \"" + pt.getName() + "\" (ID " + id + ")?\nThis cannot be undone.",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (ok == JOptionPane.YES_OPTION) {
                    manager.removePatient(id);
                    refreshAll();
                    edtId.setText(""); edtName.setText(""); edtAge.setText(""); edtDisease.setText("");
                    setFeedback(edtFeedback, "Patient ID " + id + " deleted.", RED);
                    status("Deleted patient ID " + id, RED);
                }
            } catch (NumberFormatException ex) {
                setFeedback(edtFeedback, "Enter a valid numeric Patient ID.", RED);
            }
        });

        outer.add(card);
        return outer;
    }

    // -------------------------------------------------------------------------
    //  TAB 4 — Search
    // -------------------------------------------------------------------------
    private JPanel buildSearchTab() {
        JPanel p = page();

        // Control strip
        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        ctrl.setBackground(WHITE);
        ctrl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_BLUE),
                new EmptyBorder(6, 10, 6, 10)));

        searchKeyword = new JTextField(26); styleInput(searchKeyword);
        searchBy = new JComboBox<>(new String[]{"By Name", "By Disease"});
        searchBy.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchBy.setBackground(WHITE);

        JButton btnSearch = filledBtn("Search", LB_MAIN);
        JButton btnClear  = outlineBtn("Clear", TEXT_GRAY);

        ctrl.add(label("Search:", 13, TEXT_MID, Font.BOLD));
        ctrl.add(searchKeyword);
        ctrl.add(searchBy);
        ctrl.add(btnSearch);
        ctrl.add(btnClear);

        searchModel = tableModel();
        JTable t = patientTable(searchModel, true);

        searchInfo = label("Enter a keyword above and click Search.", 12, TEXT_GRAY, Font.PLAIN);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        south.setOpaque(false);
        south.add(searchInfo);

        btnSearch.addActionListener(e -> {
            String kw = searchKeyword.getText().trim();
            if (kw.isEmpty()) { status("Enter a keyword to search.", AMBER); return; }
            ArrayList<Patient> res = searchBy.getSelectedIndex() == 0
                    ? manager.searchByName(kw) : manager.searchByDisease(kw);
            fill(searchModel, res);
            searchInfo.setText(res.size() + " result(s) for: \"" + kw + "\"");
            status(res.size() + " result(s) found.", LB_MAIN);
        });
        btnClear.addActionListener(e -> {
            searchKeyword.setText("");
            searchModel.setRowCount(0);
            searchInfo.setText("Enter a keyword above and click Search.");
            status("Search cleared.", TEXT_GRAY);
        });
        searchKeyword.addActionListener(e -> btnSearch.doClick());

        p.add(ctrl,          BorderLayout.NORTH);
        p.add(scrollWrap(t), BorderLayout.CENTER);
        p.add(south,         BorderLayout.SOUTH);
        return p;
    }

    // -------------------------------------------------------------------------
    //  TAB 5 — Sort & View
    // -------------------------------------------------------------------------
    private JPanel buildSortTab() {
        JPanel p = page();

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        ctrl.setBackground(WHITE);
        ctrl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_BLUE),
                new EmptyBorder(6, 10, 6, 10)));

        sortBy = new JComboBox<>(new String[]{
                "Patient ID (default)",
                "Age — Ascending",
                "Age — Descending",
                "Name A to Z"
        });
        sortBy.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sortBy.setBackground(WHITE);
        sortBy.setPreferredSize(new Dimension(210, 34));

        JButton btnApply = filledBtn("Apply Sort", LB_MAIN);
        ctrl.add(label("Sort by:", 13, TEXT_MID, Font.BOLD));
        ctrl.add(sortBy);
        ctrl.add(btnApply);

        sortModel = tableModel();
        JTable t = patientTable(sortModel, true);
        fill(sortModel, manager.getSortedById());

        btnApply.addActionListener(e -> {
            ArrayList<Patient> list;
            switch (sortBy.getSelectedIndex()) {
                case 1:  list = manager.getSortedByAge(true);  break;
                case 2:  list = manager.getSortedByAge(false); break;
                case 3:  list = manager.getSortedByName();     break;
                default: list = manager.getSortedById();       break;
            }
            fill(sortModel, list);
            status("Sorted by: " + sortBy.getSelectedItem(), LB_MAIN);
        });

        p.add(ctrl,          BorderLayout.NORTH);
        p.add(scrollWrap(t), BorderLayout.CENTER);
        return p;
    }

    // -------------------------------------------------------------------------
    //  TAB 6 — Seniors (60+)
    // -------------------------------------------------------------------------
    private JPanel buildSeniorsTab() {
        JPanel p = page();

        // Info banner
        JPanel banner = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        banner.setBackground(new Color(255, 247, 230));
        banner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, AMBER),
                new EmptyBorder(6, 12, 6, 12)));
        banner.add(label("Patients above age 60 — handle with extra care and priority.",
                13, new Color(146, 64, 14), Font.BOLD));

        seniorModel = tableModel();
        JTable t = new JTable(seniorModel) {
            public boolean isCellEditable(int r, int c) { return false; }
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? AMBER_LT : new Color(255, 252, 240));
                    c.setForeground(new Color(120, 53, 15));
                }
                return c;
            }
        };
        styleTable(t);

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(0, 0, 10, 0));
        top.add(banner, BorderLayout.NORTH);

        p.add(top,           BorderLayout.NORTH);
        p.add(scrollWrap(t), BorderLayout.CENTER);
        return p;
    }

    // =========================================================================
    //  STATUS BAR
    // =========================================================================
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 5));
        bar.setBackground(LB_LIGHT);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, LB_MID));
        statusLabel = label("Ready.", 12, TEXT_MID, Font.PLAIN);
        bar.add(statusLabel);
        return bar;
    }

    // =========================================================================
    //  DATA HELPERS
    // =========================================================================
    private void refreshAll() {
        fill(allModel,    manager.getSortedById());
        fill(seniorModel, manager.getPatientsAbove60());
        if (sortModel != null) fill(sortModel, manager.getSortedById());
        lblTotal.setText("  " + manager.getTotalCount() + " Patients  ");
        lblSeniors.setText("  " + manager.getSeniorCount() + " Seniors 60+  ");
        lblAvg.setText(String.format("  Avg %.1f yrs  ", manager.getAverageAge()));
    }

    private void fill(DefaultTableModel m, ArrayList<Patient> list) {
        m.setRowCount(0);
        for (Patient pt : list)
            m.addRow(new Object[]{
                    pt.getPatientId(), pt.getName(), pt.getAge(),
                    pt.getDisease(), pt.getAge() > 60 ? "Senior" : "Regular"});
    }

    private void status(String msg, Color c) {
        statusLabel.setText(msg);
        statusLabel.setForeground(c);
    }

    private void setFeedback(JLabel lbl, String msg, Color c) {
        lbl.setText(msg);
        lbl.setForeground(c);
    }

    // =========================================================================
    //  UI FACTORIES
    // =========================================================================

    /** Standard page panel with padding */
    private JPanel page() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(LB_XLIGHT);
        p.setBorder(new EmptyBorder(16, 18, 14, 18));
        return p;
    }

    /** GridBagLayout panel to centre a card */
    private JPanel centered() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(LB_XLIGHT);
        return p;
    }

    /** White card with fixed preferred size and blue border */
    private JPanel whiteCard(int w, int h) {
        JPanel c = new JPanel();
        c.setBackground(WHITE);
        c.setPreferredSize(new Dimension(w, h));
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_BLUE, 1, true),
                new EmptyBorder(24, 28, 24, 28)));
        return c;
    }

    private DefaultTableModel tableModel() {
        return new DefaultTableModel(
                new String[]{"ID", "Name", "Age", "Disease", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    /** Patient table with optional blue/amber colour coding */
    private JTable patientTable(DefaultTableModel model, boolean colourCode) {
        JTable t = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    if (colourCode) {
                        int age = 0;
                        try { age = Integer.parseInt(getValueAt(row, 2).toString()); }
                        catch (Exception ignored) {}
                        c.setBackground(age > 60
                                ? (row % 2 == 0 ? AMBER_LT : new Color(255, 252, 240))
                                : (row % 2 == 0 ? WHITE     : ROW_ALT));
                        c.setForeground(TEXT_DARK);
                    }
                }
                return c;
            }
        };
        styleTable(t);
        return t;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(34);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setGridColor(BORDER_BLUE);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(LB_LIGHT);
        t.setSelectionForeground(LB_DARK);

        JTableHeader hdr = t.getTableHeader();
        hdr.setBackground(LB_LIGHT);
        hdr.setForeground(TEXT_DARK);
        hdr.setFont(new Font("SansSerif", Font.BOLD, 13));
        hdr.setPreferredSize(new Dimension(0, 38));
        hdr.setReorderingAllowed(false);
        hdr.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, LB_MID));

        // Status badge
        t.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                String v = val == null ? "" : val.toString();
                setHorizontalAlignment(CENTER);
                setFont(new Font("SansSerif", Font.BOLD, 12));
                setForeground("Senior".equals(v) ? AMBER : GREEN);
                return this;
            }
        });

        int[] widths = {65, 200, 55, 220, 90};
        for (int i = 0; i < widths.length; i++)
            t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
    }

    private JScrollPane scrollWrap(JTable t) {
        JScrollPane s = new JScrollPane(t);
        s.setBorder(BorderFactory.createLineBorder(BORDER_BLUE, 1));
        s.getViewport().setBackground(WHITE);
        s.getVerticalScrollBar().setBackground(LB_XLIGHT);
        return s;
    }

    /** Vertical form row: bold label + text field inside BoxLayout parent */
    private JTextField inputRow(JPanel parent, String labelText, String tooltip) {
        JLabel lbl = label(labelText, 12, TEXT_MID, Font.BOLD);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(4));

        JTextField tf = new JTextField();
        tf.setMaximumSize(new Dimension(9999, 36));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        if (!tooltip.isEmpty()) tf.setToolTipText(tooltip);
        styleInput(tf);
        parent.add(tf);
        parent.add(Box.createVerticalStrut(12));
        return tf;
    }

    private void styleInput(JTextField tf) {
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBackground(WHITE);
        tf.setForeground(TEXT_DARK);
        tf.setCaretColor(LB_MAIN);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_BLUE, 1, true),
                new EmptyBorder(6, 10, 6, 10)));
    }

    /** Blue filled button - custom painted so Windows LAF cannot override colours */
    private JButton filledBtn(String text, Color bg) {
        final Color[] cur = {bg};
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cur[0]);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setForeground(WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(new EmptyBorder(9, 20, 9, 20));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { cur[0] = bg.darker();  b.repaint(); }
            public void mouseExited(MouseEvent e)  { cur[0] = bg;           b.repaint(); }
        });
        return b;
    }

    /** Outlined button - custom painted so Windows LAF cannot override colours */
    private JButton outlineBtn(String text, Color color) {
        final Color[] bg = {WHITE};
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg[0]);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setForeground(color);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { bg[0] = LB_XLIGHT; b.repaint(); }
            public void mouseExited(MouseEvent e)  { bg[0] = WHITE;     b.repaint(); }
        });
        return b;
    }

    private JLabel label(String text, int size, Color color, int style) {
        JLabel l = new JLabel(text);
        l.setForeground(color);
        l.setFont(new Font("SansSerif", style, size));
        return l;
    }

    /** Rounded stat pill for the header */
    private JLabel statPill(String text, Color bg, Color fg) {
        JLabel l = new JLabel("  " + text + "  ") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(fg.darker());
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setForeground(fg);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(5, 4, 5, 4));
        return l;
    }

    private void cardHeading(JPanel parent, String text) {
        JLabel l = label(text, 16, TEXT_DARK, Font.BOLD);
        l.setAlignmentX(LEFT_ALIGNMENT);
        parent.add(l);
    }

    private void cardSub(JPanel parent, String text) {
        JLabel l = label(text, 12, TEXT_GRAY, Font.PLAIN);
        l.setAlignmentX(LEFT_ALIGNMENT);
        parent.add(Box.createVerticalStrut(4));
        parent.add(l);
    }

    private JPanel legendRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 6));
        p.setOpaque(false);
        p.add(label("Rows:", 11, TEXT_GRAY, Font.PLAIN));
        p.add(legendDot(AMBER_LT,  "Senior (age > 60)"));
        p.add(legendDot(ROW_ALT,   "Regular patient (even row)"));
        p.add(legendDot(WHITE,     "Regular patient (odd row)"));
        return p;
    }

    private JPanel legendDot(Color col, String lbl) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        row.setOpaque(false);
        JPanel swatch = new JPanel();
        swatch.setBackground(col);
        swatch.setPreferredSize(new Dimension(14, 14));
        swatch.setBorder(BorderFactory.createLineBorder(BORDER_BLUE));
        row.add(swatch);
        row.add(label(lbl, 11, TEXT_GRAY, Font.PLAIN));
        return row;
    }

    // =========================================================================
    //  MAIN
    // =========================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new HospitalApp().setVisible(true);
        });
    }
}