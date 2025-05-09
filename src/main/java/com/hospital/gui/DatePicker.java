package com.hospital.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import javax.swing.*;

public class DatePicker {
    int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
    int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    JLabel l = new JLabel("", JLabel.CENTER);
    String day = "";
    JDialog d;
    JDialog years;
    JButton[] button = new JButton[49];
    JButton[] buttonYears = new JButton[16];
    JFrame parent;

    public DatePicker(JFrame parent) {
        this.parent = parent;
    }

    public void initiateDialog() {
        d = new JDialog(parent, "Date Picker", true);
        //ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/Calendar.png")));

        String[] header = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
        JPanel p1 = new JPanel(new GridLayout(7, 7));
        p1.setPreferredSize(new Dimension(430, 120));

        l = new JLabel("", JLabel.CENTER);
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        l.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    initiateYearsDialog();
                }
            }
        });

        for (int x = 0; x < button.length; x++) {
            final int selection = x;
            button[x] = new JButton();
            button[x].setFocusPainted(false);
            button[x].setBackground(Color.white);
            if (x > 6) {
                button[x].addActionListener(ae -> {
                    day = button[selection].getActionCommand();
                    d.dispose();
                });
            } else {
                button[x].setText(header[x]);
                button[x].setForeground(Color.red);
            }
            p1.add(button[x]);
        }

        JPanel p2 = new JPanel(new GridLayout(1, 3));
        JButton previous = new JButton("<< Previous");
        previous.addActionListener(ae -> {
            month--;
            displayDate();
        });
        JButton next = new JButton("Next >>");
        next.addActionListener(ae -> {
            month++;
            displayDate();
        });
        p2.add(previous);
        p2.add(l);
        p2.add(next);

        //d.setIconImage(icon.getImage());
        d.add(p1, BorderLayout.CENTER);
        d.add(p2, BorderLayout.SOUTH);
        d.pack();
        d.setLocationRelativeTo(parent);
        displayDate();
        d.setVisible(true);
    }


    public void initiateYearsDialog() {
        years = new JDialog(d, "Select Year", true);
        JPanel p1 = new JPanel(new GridLayout(4, 4));
        p1.setPreferredSize(new Dimension(430, 120));

        buttonYears = new JButton[16];
        for (int x = 0; x < buttonYears.length; x++) {
            final int selection = x;
            buttonYears[x] = new JButton();
            buttonYears[x].setFocusPainted(false);
            buttonYears[x].setBackground(Color.white);
            buttonYears[x].addActionListener(ae -> {
                year = Integer.parseInt(buttonYears[selection].getActionCommand());
                years.dispose();
                displayDate();
            });
            p1.add(buttonYears[x]);
        }

        JPanel p2 = new JPanel(new GridLayout(1, 3));
        JButton previous = new JButton("<< Previous");
        previous.addActionListener(ae -> {
            year -= 16;
            displayYears();
        });
        JButton next = new JButton("Next >>");
        next.addActionListener(ae -> {
            if (year + 16 >= java.util.Calendar.getInstance().get(java.util.Calendar.YEAR))
                year += (java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) - year);
            else
                year += 16;
            displayYears();
        });
        p2.add(previous);
        p2.add(new JLabel());
        p2.add(next);

        years.add(p1, BorderLayout.CENTER);
        years.add(p2, BorderLayout.SOUTH);
        years.pack();
        years.setLocationRelativeTo(d);
        displayYears();
        years.setVisible(true);
    }


    public void displayYears() {
        for (int x = 0; x < buttonYears.length; x++) {
            buttonYears[x].setText(year - (buttonYears.length - 1 - x) + "");
        }
    }

    public void displayDate() {
        for (int x = 7; x < button.length; x++)
            button[x].setText("");
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "MMMM yyyy", Locale.ENGLISH);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, 1);
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
            button[x].setText("" + day);
        l.setText(sdf.format(cal.getTime()));
        d.setTitle("Date Picker");
    }

    public Date getPickedDate() {
        initiateDialog();

        // Check if the day is empty or invalid
        if (day.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please select a valid date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return new Date(); // Return an empty string or handle it appropriately
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar cal = java.util.Calendar.getInstance();

        try {
            cal.set(year, month, Integer.parseInt(day));

            return cal.getTime();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error selecting the date. Please try again.", "Date Error", JOptionPane.ERROR_MESSAGE);
            return new Date(); // Return an empty string if the date is invalid
        }
    }
}

