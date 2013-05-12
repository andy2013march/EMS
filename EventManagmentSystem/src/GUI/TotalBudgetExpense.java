/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import BackEnd.EventSystem.Committee;
import BackEnd.EventSystem.Expense;
import BackEnd.ManagerSystem.MainManager;
import GUI.Dialog.NewExpenseDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sara
 */
public class TotalBudgetExpense extends javax.swing.JPanel {

    /**
     * Creates new form BudgetExpense
     */
    private MainManager manager;
    
    public TotalBudgetExpense() {
        initComponents();
        manager = MainManager.getInstance();
        updateInfo();
    }
    
    public void updateInfo()
    {
        DefaultTableModel model = getTableModel();
        model.setRowCount(0);
        
        if(manager.getEventManager().getSelectedEvent().getCommitteeList() != null)
        {
        for(Committee c : manager.getEventManager().getSelectedEvent().getCommitteeList()){
            for(Expense e : c.getBudget().getExpenseList())
            {
                model.addRow(
                    new Object[]
                    {
                        e.getBUDGET_ITEM_ID(),e.getDescription(),e.getValue()
                    });
            }
        }
        }
        
    }
    
    public DefaultTableModel getTableModel()
    { return (DefaultTableModel)expensesTable.getModel(); }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        expensesLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        expensesTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(370, 600));

        expensesLabel.setFont(new java.awt.Font("Candara", 1, 16)); // NOI18N
        expensesLabel.setText("Expense");

        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 500));

        expensesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Description", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        expensesTable.setPreferredSize(new java.awt.Dimension(384, 407));
        expensesTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(expensesTable);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(expensesLabel)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(expensesLabel)
                .add(12, 12, 12)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 530, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel expensesLabel;
    private javax.swing.JTable expensesTable;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}