package net.combase.desktopcrm.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import net.combase.desktopcrm.data.CrmManager;
import net.combase.desktopcrm.domain.Lead;
import net.combase.desktopcrm.domain.Task;

public class TaskTablePanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6149463410211475900L;
	private JTable table;

	/**
	 * Create the panel.
	 */
	public TaskTablePanel()
	{
		setLayout(new BorderLayout(0, 0));


		final TaskTableModel model = new TaskTableModel(new ArrayList<Task>());

		table = new JTable(model);

		// enable button clicks
		table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				int column = table.getColumnModel().getColumnIndexAtX(e.getX());
				int row = e.getY() / table.getRowHeight();

				if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() &&
					column >= 0)
				{
					Object value = table.getValueAt(row, column);
					if (value instanceof JButton)
					{
						((JButton)value).doClick();
					}
				}
			}
		});

		table.setDefaultRenderer(JButton.class, new TableCellRenderer()
		{
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column)
			{
				if (value instanceof JButton)
				{
					return (JButton)value;
				}

				return new JLabel();
			}
		});

		table.getColumnModel().getColumn(1).setMaxWidth(140);
		table.getColumnModel().getColumn(1).setMinWidth(140);
		table.getColumnModel().getColumn(2).setMaxWidth(30);
		table.getColumnModel().getColumn(3).setMaxWidth(30);
		table.getColumnModel().getColumn(4).setMaxWidth(30);
		table.getColumnModel().getColumn(5).setMaxWidth(30);
		table.getColumnModel().getColumn(6).setMaxWidth(30);
		table.getColumnModel().getColumn(7).setMaxWidth(30);
		table.setRowHeight(30);

		add(table.getTableHeader(), BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);

		table.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent me)
			{
				JTable table = (JTable)me.getSource();
				if (me.getClickCount() == 2)
				{
					Point p = me.getPoint();
					int row = table.rowAtPoint(p);
					Task task = model.getTask(row);
					String title = null;
					String parentId = task.getParentId();
					switch (task.getParentType())
					{
						case "Cases" :
							title = CrmManager.getCase(parentId).getTitle();
							break;
						case "Leads" :
							Lead lead = CrmManager.getLead(parentId);
							title = lead.getTitle();
							title += " - " + lead.getAccountName();
							break;
						case "Contacts" :
							title = CrmManager.getContact(parentId).getTitle();
							break;
						case "Accounts" :
							title = CrmManager.getCase(parentId).getTitle();
							break;
						case "Opportunities" :
							title = CrmManager.getOpprtunity(parentId).getTitle();
							break;
						default :
							break;
					}
					StringSelection stringSelection = new StringSelection(title);
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);

				}
			}
		});


		java.util.Timer t = new java.util.Timer(true);
		t.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				List<Task> updatedList = CrmManager.getTaskList();
				System.out.println(updatedList);
				model.update(updatedList);
			}
		}, 500, 120000);
	}

}
