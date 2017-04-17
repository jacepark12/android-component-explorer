package com.android.component.explorer;

import com.android.component.explorer.scanner.DirExplorer;
import com.android.component.explorer.scanner.FileHandler;
import com.android.component.explorer.scanner.Filter;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by parkjaesung on 2017. 4. 10..
 */
public class ToolWindowTest implements ToolWindowFactory {
    private JButton button1;
    private JPanel MainPanel;
    private JLabel Label1;
    private JTree tree;

    public void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        System.out.println("creatToolWindow");
        Content content = contentFactory.createContent(MainPanel, "",false);
        toolWindow.getContentManager().addContent(content);

        final VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentRoots();
        System.out.println("length : " + vFiles.length);
        System.out.println("path : " + vFiles[0].getPath());
        System.out.println("canonicalpath : " + vFiles[0].getCanonicalPath());

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Label1.setText("Button Clicked");
                System.out.println("test");
                scanProject(vFiles[0].getCanonicalPath());

                //FileEditor[] editors = FileEditorManager.getInstance(project).openFile(vFiles[0], true);
                new OpenFileDescriptor(project, vFiles[0]).navigate(true);
                //System.out.println("FileEditor length : " + editors.length);
                //editors[0].setState(FileEditorState.INSTANCE);
                VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(new File("/Users/parkjaesung/Documents/workspace/Github/android-viewbinder/src/main/java/com/android/component/explorer/ToolWindowTest.java"));
                FileEditor[] editors = FileEditorManager.getInstance(project).openFile(virtualFile, true);
                editors[0].setState(FileEditorState.INSTANCE);
            }
        });

        //configure tree1
        //create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        //create the child nodes
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
        //add the child nodes to the root node
        root.add(vegetableNode);
        root.add(fruitNode);

        //create the tree by passing in the root node
        tree = (Tree) new JTree(root);
        MainPanel.add(tree);

    }

    private void scanProject(String rootDir){
        System.out.println("scanning project");
        File file = new File(rootDir);

        DirExplorer dirExplorer = new DirExplorer(Filter.getInstance(), FileHandler.getInstance());

        dirExplorer.explore(file);
    }
}
