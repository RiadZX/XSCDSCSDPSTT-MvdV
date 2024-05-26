package toolWindow;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.github.riadzx.xscdscsdpsttmvdv.MyBundle;
import services.ScanFileService;
import utils.PsiHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ComplexityWindowFactory implements ToolWindowFactory {

    private static final Logger LOGGER = Logger.getInstance(ComplexityWindowFactory.class);

    public ComplexityWindowFactory() {
    }

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ComplexityWindow myToolWindow = new ComplexityWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(myToolWindow.getContent(), null, false);
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public boolean shouldBeAvailable(Project project) {
        return true;
    }

    public static class ComplexityWindow {

        private final ScanFileService service;
        public static JLabel loadingLabel;
        public static JPanel panel;

        ComplexityWindow(ToolWindow toolWindow) {
            this.service = toolWindow.getProject().getService(ScanFileService.class);
        }

        public JPanel getContent() {
            JBPanel<JBPanel<?>> panel = new JBPanel<>(new BorderLayout());
            JButton button = new JButton("Scan Active File");  // Replace MyBundle.message("scanActiveFile") with a static string for simplicity
            JPanel buttonPanel = new JPanel();
            button.setPreferredSize(new Dimension(100, 30));
            JLabel loadingLabel = new JLabel();
            // Icon loadingIcon = new ImageIcon(getClass().getResource("images/loading.png"));
            // loadingLabel.setIcon(loadingIcon);
            loadingLabel.setText("Loading...");
            loadingLabel.setHorizontalAlignment(JLabel.CENTER);
            loadingLabel.setVisible(false);  // Initially hidden

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Show the loading label
                    service.scanFile(PsiHelper.getCurrentFile());
                }
            });
            buttonPanel.add(button);
            panel.add(loadingLabel, BorderLayout.NORTH);
            panel.add(buttonPanel, BorderLayout.CENTER);
            this.loadingLabel = loadingLabel;
            this.panel=panel;
            return panel;
        }

        public static void displayLoad(){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    loadingLabel.setVisible(true);
                    panel.revalidate();
                    panel.repaint();
                }
            });

        }

        public static void undisplayLoad(){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    loadingLabel.setVisible(false);
                    panel.revalidate();
                    panel.repaint();
                }
            });
        }
    }
}
