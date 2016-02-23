package lu.uni.adtool.ui.canvas;

import lu.uni.adtool.tools.Options;
import lu.uni.adtool.tree.GuiNode;
import lu.uni.adtool.tree.Node;
import lu.uni.adtool.tree.NodeTree;
import lu.uni.adtool.tree.SandNode;
import lu.uni.adtool.tree.SandParser;
import lu.uni.adtool.ui.MainController;
import lu.uni.adtool.ui.TermView;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JScrollPane;

import org.abego.treelayout.util.DefaultConfiguration;

// if Type is null then it is the canvas with the original tree
public class SandTreeCanvas<Type> extends AbstractTreeCanvas {
  public SandTreeCanvas(NodeTree tree, MainController mc) {
    super(tree, mc);
    this.labelCounter = tree.getLayout().getLabelCounter(LABEL_PREFIX);
    this.listener = new SandCanvasHandler(this);
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
    this.addKeyListener(listener);
    this.configuration =
        new DefaultConfiguration<Node>(Options.canv_gapBetweenLevels, Options.canv_gapBetweenNodes);
    if (tree != null) {
      this.setFocus(null);
      this.lastFocused = (GuiNode) tree.getRoot(false);
      // create the layout
      this.getSharedExtentProvider().updateTreeSize(tree.getRoot(true));
      this.recalculateLayout();
    }
  }

  /**
   * Adds a child or a counter to the node.
   *
   * @param node
   */
  public void addChild(Node node) {
    Node child = new SandNode(((SandNode) node).getType());
    child.setName(this.getNewLabel());
    tree.addChild(node, child);
    this.notifyAllTreeChanged();
    terms.updateTerms();
  }

  public void setTerms(TermView terms) {
    this.terms = terms;
  }

  public void switchSibling(Node node, boolean onLeft) {
    if (node.getParent() != null) {
      GuiNode newPos = null;
      if (onLeft) {
        newPos = ((GuiNode)node).getLeftSibling();
      }
      else {
        newPos = ((GuiNode)node).getRightSibling();
      }
      if (newPos != null && newPos.getParent() != null) {
        tree.switchSibling(node, newPos);
        this.notifyAllTreeChanged();
        terms.updateTerms();
      }
    }
  }

  /**
   * Adds a sibling to a node on a left or right side.
   *
   * @param node
   * @param onLeft
   *          if true we add sibling to the left.
   */
  public void addSibling(Node node, boolean onLeft) {
    if (node.getParent() != null) {
      SandNode sibling = new SandNode(((SandNode) node).getType());
      sibling.setName(this.getNewLabel());
      tree.addSibling(node, sibling, onLeft);
      this.notifyAllTreeChanged();
      terms.updateTerms();
    }
  }

  /**
   * Gets the label separated into lines
   *
   * @return The array of labels with each line as a separate entry
   */
  public String[] getLabelLines(Node node) {
    return getLabel(node).split("\n");
  }

  /**
   * Returns a text label that is painted for a given node.
   *
   * @param node
   *          label as a text.
   * @return
   */
  public String getLabel(Node node) {
    return node.getName();
  }

  /**
   * Changes the node label.
   *
   * @param node
   *          node for which we change the label.
   * @param label
   *          new label for the node.
   */
  public void setLabel(Node node, String label) {
    tree.setName(node, label);
    this.notifyAllTreeChanged();
    if (node.isLeaf()) {
      this.terms.updateTerms();
    }
  }


  public void repaintAll() {
    controller.getFrame().getDomainFactory().repaintAllDomains(this.getId());
    this.repaint();
  }

  public boolean validLabel(String s) {
    if (s == null) {
      return false;
    }
    if (s.length() == 0) {
      return false;
    }
    SandParser parser = new SandParser();
    SandNode root = parser.parseString(s);
    if (root == null) return false;
    return root.isLeaf();
  }

  public void toggleOp(Node node) {
    ((SandNode) node).toggleOp();
    tree.getLayout().recalculateValues();
    this.repaintAll();
    this.terms.updateTerms();
  }

  /**
   * Removes the subtree with node as root.
   *
   * @param node
   *          root of a subtree.
   */
  public void removeTree(Node node) {
    if (!node.equals(tree.getRoot(true))) {
      if (lastFocused.equals(node)) {
        lastFocused = ((GuiNode) node).getParent(true);
        if (lastFocused == null) {
          lastFocused = (GuiNode) tree.getRoot(true);
        }
      }
      if (focused != null) {
        if (focused.equals(node)) {
          setFocus(((GuiNode) node).getParent(true));
        }
      }
      tree.removeTree(node);
      this.notifyAllTreeChanged();
      this.terms.updateTerms();
    }
  }

  /**
   * Removes all children of a node
   *
   * @param node
   *          node for which we remove children.
   */
  public void removeChildren(Node node) {
    tree.removeAllChildren(node);
    this.notifyAllTreeChanged();
    this.terms.updateTerms();
  }

  /**
   * Returns the root of a tree associated with this canvas
   */
  public void setRoot(SandNode root) { // TODO - update all listeners etc
    this.tree.setRoot(root);
    this.notifyAllTreeChanged();
    this.terms.updateTerms();
    // this.
    // tree.notifyTreeChanged(TreeChangeListener.Change.NODES_CHANGE);
  }

  /**
   * Returns the root of a tree associated with this canvas
   */
  public SandNode getRoot() {
    return (SandNode) tree.getRoot(true);
  }

  public void setScrollPane(JScrollPane pane) {
    this.scrollPane = pane;
    this.scrollPane.addMouseWheelListener(listener);
    this.scrollPane.addComponentListener(listener);
    this.viewPortSize = this.scrollPane.getViewport().getExtentSize();
    this.setScale(1);
  }

  protected Color getFillColor(Node node) {
    return Options.canv_FillColorAtt;
  }

  protected String getNewLabel() {
    labelCounter = labelCounter + 1;
    return LABEL_PREFIX + labelCounter;
  }

  protected SandCanvasHandler listener;
  private int                 labelCounter;
  private TermView            terms;
  private static final long   serialVersionUID = 6626362203605041529L;
  private static final String LABEL_PREFIX     = "N_";
}