import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.LocalTime;

public class PHTimeWidget extends JFrame {
    private JLabel timeLabel;
    private JLabel taskLabel;
    private Point mouseClickPoint; // To handle dragging
    private static final ZoneId PH_ZONE = ZoneId.of("Asia/Manila");

    public PHTimeWidget() {
        // Widget Setup: Approx 1 inch x 0.5 inch
        setSize(130, 75); 
        setUndecorated(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout());

        // POSITIONING: Upper Center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (this.getWidth() / 2);
        int y = 10; // 10 pixels from the top
        setLocation(x, y);

        // DRAGGING LOGIC (So you can move it if needed)
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { mouseClickPoint = e.getPoint(); }
        });
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseClickPoint.x, currCoords.y - mouseClickPoint.y);
            }
        });

        // Top Panel for Exit Button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        topPanel.setOpaque(false);
        JButton exitBtn = new JButton("×");
        exitBtn.setMargin(new Insets(0, 0, 0, 0));
        exitBtn.setPreferredSize(new Dimension(20, 20));
        exitBtn.setFocusPainted(false);
        exitBtn.setBackground(new Color(60, 60, 60));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBorder(BorderFactory.createEmptyBorder());
        exitBtn.addActionListener(e -> System.exit(0));
        topPanel.add(exitBtn);

        // Labels
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setOpaque(false);

        timeLabel = new JLabel("", SwingConstants.CENTER);
        timeLabel.setForeground(Color.CYAN);
        timeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        taskLabel = new JLabel("Status: OK", SwingConstants.CENTER);
        taskLabel.setForeground(Color.WHITE);
        taskLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));

        centerPanel.add(timeLabel);
        centerPanel.add(taskLabel);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        Timer timer = new Timer(1000, e -> updateClock());
        timer.start();
    }

    private void updateClock() {
        ZonedDateTime phTime = ZonedDateTime.now(PH_ZONE);
        LocalTime now = phTime.toLocalTime();
        timeLabel.setText(phTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        String status = getAlarmStatus(now);
        taskLabel.setText(status);

        if (!status.equals("Stay Productive")) {
            getContentPane().setBackground(new Color(150, 0, 0)); 
        } else {
            getContentPane().setBackground(new Color(30, 30, 30));
        }
    }

    private String getAlarmStatus(LocalTime now) {
        if (isWithinMinute(now, 10, 30)) return "BREAKKY TIME!";
        if (isWithinMinute(now, 12, 0))  return "LUNCH TIME!";
        if (isWithinMinute(now, 15, 0))  return "CLEAN DESK!";
        if (isWithinMinute(now, 17, 0))  return "WRAP UP!";
        if (isWithinMinute(now, 18, 0))  return "SIGN OFF!";
        return "Stay Productive";
    }

    private boolean isWithinMinute(LocalTime now, int hour, int minute) {
        return now.getHour() == hour && now.getMinute() == minute;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PHTimeWidget().setVisible(true));
    }
}
