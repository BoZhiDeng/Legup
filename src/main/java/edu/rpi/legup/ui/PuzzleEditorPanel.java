package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.EditorElementController;
import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.IHistoryListener;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.save.ExportFileException;
import edu.rpi.legup.save.InvalidFileFormatException;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.puzzleeditorui.elementsview.ElementFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.io.File;
import java.net.URL;

public class PuzzleEditorPanel extends LegupPanel implements IHistoryListener {

    private final static Logger LOGGER = LogManager.getLogger(PuzzleEditorPanel.class.getName());
    private JMenu[] menus;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JFrame frame;
    private JButton[] buttons;
    JSplitPane splitPanel;
    private JButton[] toolBarButtons;
    private JPanel elementPanel;
    private DynamicView dynamicBoardView;
    private BoardView boardView;
    private TitledBorder boardBorder;
    //private JSplitPane splitPanel, topHalfPanel;
    private FileDialog fileDialog;
    private JMenuItem undo, redo, fitBoardToScreen;
    private ElementFrame elementFrame;
    private JPanel treePanel;
    private LegupUI legupUI;
    private EditorElementController editorElementController;
    final static int[] TOOLBAR_SEPARATOR_BEFORE = {2, 4, 8};

    public PuzzleEditorPanel(FileDialog fileDialog, JFrame frame, LegupUI legupUI) {
        this.fileDialog = fileDialog;
        this.frame = frame;
        this.legupUI = legupUI;
        setLayout(new BorderLayout());
    }
  
      private void savePuzzle() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if (puzzle == null) {
            return;
        }

        if (fileDialog == null) {
            fileDialog = new FileDialog(this.frame);
        }

        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setTitle("Save Proof");
        String curFileName = GameBoardFacade.getInstance().getCurFileName();
        if (curFileName == null) {
            fileDialog.setDirectory(LegupPreferences.getInstance().getUserPref(LegupPreferences.WORK_DIRECTORY));
        }
        else {
            File curFile = new File(curFileName);
            fileDialog.setDirectory(curFile.getParent());
        }
        fileDialog.setVisible(true);

        String fileName = null;
        if (fileDialog.getDirectory() != null && fileDialog.getFile() != null) {
            fileName = fileDialog.getDirectory() + File.separator + fileDialog.getFile();
        }

        if (fileName != null) {
            try {
                PuzzleExporter exporter = puzzle.getExporter();
                if (exporter == null) {
                    throw new ExportFileException("Puzzle exporter null");
                }
                exporter.exportPuzzle(fileName);
            }
            catch (ExportFileException e) {
                e.printStackTrace();
            }
        }
    }

    public DynamicView getDynamicBoardView() {
        return dynamicBoardView;
    }
