/*
 * BrawlCardGameAboutBox.java
 */

package brawlcardgame;

import org.jdesktop.application.Action;

public class BrawlCardGameInstructionsBox extends javax.swing.JDialog {
   private static final long serialVersionUID = 1L;

    public BrawlCardGameInstructionsBox(java.awt.Frame parent) {
        super(parent);
        initComponents();
        getRootPane().setDefaultButton(closeButton);
    }

    @Action public void closeInstructionsBox() {
        dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      closeButton = new javax.swing.JButton();
      jScrollPane1 = new javax.swing.JScrollPane();
      jLabel1 = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(brawlcardgame.BrawlCardGameApp.class).getContext().getResourceMap(BrawlCardGameInstructionsBox.class);
      setTitle(resourceMap.getString("title")); // NOI18N
      setLocationByPlatform(true);
      setName("instructionsBox"); // NOI18N

      javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(brawlcardgame.BrawlCardGameApp.class).getContext().getActionMap(BrawlCardGameInstructionsBox.class, this);
      closeButton.setAction(actionMap.get("closeInstructionsBox")); // NOI18N
      closeButton.setText(resourceMap.getString("closeButton.text")); // NOI18N
      closeButton.setName("closeButton"); // NOI18N

      jScrollPane1.setName("jScrollPane1"); // NOI18N
      jScrollPane1.setPreferredSize(new java.awt.Dimension(600, 500));

      jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
      jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
      jLabel1.setName("jLabel1"); // NOI18N
      jScrollPane1.setViewportView(jLabel1);

      org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
         .add(layout.createSequentialGroup()
            .addContainerGap(408, Short.MAX_VALUE)
            .add(closeButton)
            .addContainerGap())
         .add(layout.createSequentialGroup()
            .addContainerGap()
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 464, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
         .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .addContainerGap()
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(closeButton)
            .addContainerGap())
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents
    
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton closeButton;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JScrollPane jScrollPane1;
   // End of variables declaration//GEN-END:variables
    
}