package edu.uw.cynetworkbma.internal.inference;

import java.util.*;

import org.cytoscape.model.*;

public class TableReader {

	public static class NamedExpressionMatrix {
		
		public String[] geneNames;
		public double[][] data;
	}
	
	public static class PriorProbabilityMatrix {
		
		public String[] regulatorGeneNames;
		public String[] regulatedGeneNames;
		public double[][] data;
	}
	
	public NamedExpressionMatrix getExpressionMatrix(
			CyTable table, List<CyColumn> columnsToInclude, boolean rowsAreGenes) {
		
		NamedExpressionMatrix m = new NamedExpressionMatrix();
		
		int rowCount = table.getRowCount();
		int columnCount = columnsToInclude.size();
		List<CyRow> rows = table.getAllRows();
		
        if (rowsAreGenes) {
        	m.data = new double[columnCount][];
        	for (int k = 0; k < columnCount; k++) {
        		m.data[k] = new double[rowCount];
        	}
        	
        	m.geneNames = new String[rowCount];
        	for (int j = 0; j < rowCount; j++) {
        		CyRow row = rows.get(j);
        		m.geneNames[j] = row.get(table.getPrimaryKey().getName(), String.class);
        		
        		for (int i = 0; i < columnCount; i++) {
        			m.data[i][j] = row.get(columnsToInclude.get(i).getName(), Double.class);
        		}
        	}
        } else {
        	m.geneNames = new String[columnCount];
        	for (int k = 0; k < columnCount; k++) {
        		m.geneNames[k] = columnsToInclude.get(k).getName();
        	}
        	
        	m.data = new double[rowCount][];
        	for (int i = 0; i < rowCount; i++) {
        		m.data[i] = new double[columnCount];
        		for (int j = 0; j < columnCount; j++) {
        			m.data[i][j] = rows.get(i).get(columnsToInclude.get(j).getName(), Double.class);
        		}
        	}
        }
        
        return m;
	}
	
	public PriorProbabilityMatrix getPriorProbabilityMatrix(CyTable table, String[] genes) {
		
		PriorProbabilityMatrix m = new PriorProbabilityMatrix();
		
		int geneCount = genes.length;
		Set<String> geneSet = new HashSet<String>();
		for (String gene : genes) {
			geneSet.add(gene);
		}
		
		m.data = new double[geneCount][];
		m.regulatorGeneNames = new String[geneCount];
		m.regulatedGeneNames = new String[geneCount];
		
		String primaryKeyName = null;
		int k = 0;
		for (CyColumn column : table.getColumns()) {
			if (column.isPrimaryKey()) {
				primaryKeyName = column.getName();
			} else if (geneSet.contains(column.getName())) {
				m.regulatedGeneNames[k] = column.getName();
				k++;
			}
		}
		
		int i = 0;
		for (CyRow row : table.getAllRows()) {
			String geneName = row.get(primaryKeyName, String.class);
			
			if (geneSet.contains(geneName)) {
				m.regulatorGeneNames[i] = geneName;
				
				m.data[i] = new double[geneCount];
				for (int j = 0; j < geneCount; j++) {
					m.data[i][j] = row.get(m.regulatedGeneNames[j], Double.class);
				}
				
				i++;
			}
		}
		
		return m;
	}
}
