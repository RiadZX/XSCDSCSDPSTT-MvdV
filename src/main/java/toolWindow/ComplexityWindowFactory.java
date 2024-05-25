package toolWindow;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.github.riadzx.xscdscsdpsttmvdv.MyBundle;
import com.github.riadzx.xscdscsdpsttmvdv.services.MyProjectService;
import services.ScanFileService;
import utils.Convertinator;
import utils.DependencyTree;
import utils.GroupInfo;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ComplexityWindowFactory implements ToolWindowFactory {

    private static final Logger LOGGER = Logger.getInstance(ComplexityWindowFactory.class);

    public ComplexityWindowFactory() {
        LOGGER.warn("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.");
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

    private static class ComplexityWindow {

        private final ScanFileService service;

        ComplexityWindow(ToolWindow toolWindow) {
            this.service = toolWindow.getProject().getService(ScanFileService.class);
        }

        public JPanel getContent() {
            JBPanel<JBPanel<?>> panel = new JBPanel<>();
            JButton button = new JButton(MyBundle.message("scanActiveFile"));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // get the currently opened file
                    var res = service.scanFile(service.getCurrentFile());
                    ///REMOVE THIS------------------------------------------------------------------
//                    var groupInfoTemp = new GroupInfo();
//                    var groupInfoTemp2 = new GroupInfo();
//                    groupInfoTemp.setMethods(List.of(res.get(1)));
//                    groupInfoTemp.setChildren(List.of(groupInfoTemp2));
//                    groupInfoTemp2.setMethods(List.of(res.get(0)));
//                    res.get(0).setTimeComplexityLong("O(heel veel)");
//                    Convertinator.toPrompt(groupInfoTemp);
                    ///ABOVE HERE ------------------------------------------------------------------
                    System.out.println(res);
                    DependencyTree.buildFromPSIElements(res);
                }
            });
            panel.add(button);
            return panel;
        }
    }
}
